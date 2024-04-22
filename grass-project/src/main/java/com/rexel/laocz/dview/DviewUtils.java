package com.rexel.laocz.dview;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.rexel.common.exception.CustomException;
import com.rexel.common.exception.DviewException;
import com.rexel.common.utils.CaffeineUtil;
import com.rexel.common.utils.DictUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.dview.api.ShortGetVarNameAndIndex;
import com.rexel.dview.api.ShortGetVarValue;
import com.rexel.dview.api.ShortGetVarValueByIndex;
import com.rexel.dview.api.ShortUpdateValueByIndex;
import com.rexel.dview.pojo.DViewVarInfo;
import com.rexel.dview.pojo.DviewCommondResultData;
import com.rexel.laocz.dview.domain.DviewPointDTO;
import com.rexel.laocz.dview.domain.EdgeConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rexel.dview.cons.Constants.VAR_TYPE_MAP;


/**
 * @ClassName DviewServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2023/2/16 11:46
 **/
@Slf4j
@Component
public class DviewUtils {
    /**
     * 是否自动启动 dview采集、DCS状态刷新、输送监控
     * true:自动启动，false:不自动启动
     */
    private static final boolean IS_AUTO_RUNNING = true;
    private static final String DVIEW_ADDRESS = "dview_address";
    private static final String DVIEW_IP = "ip";
    private static final String DVIEW_PORT = "port";
    private static final String[] INDEX_TYPE = VAR_TYPE_MAP.keySet().toArray(new String[0]);
    private static final String[] POINT_TYPE = VAR_TYPE_MAP.keySet().toArray(new String[0]);
    private static final String ZERO = "{0}";
    /**
     * 缓存测点索引，定时更新,缓解io,多线程map
     * key:测点类型
     * value: Key 测点索引,V 测点名称
     */
    private static final ConcurrentHashMap<String, ConcurrentHashMap<Integer, String>> indexMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> reversedIndexMap = new ConcurrentHashMap<>();
    /**
     * 缓存测点实时值，定时更新,缓解io,多线程map
     * key:测点类型
     * value: Key 测点id,V 测点值
     */
    private static final ConcurrentHashMap<String, Map<String, Object>> pointValueMap = new ConcurrentHashMap<>();
    private static final AtomicInteger errorCount = new AtomicInteger(0);
    private static volatile EdgeConf edgeConf;
    private static volatile ShortGetVarNameAndIndex getVarNameAndIndex;
    private static volatile ShortGetVarValueByIndex varValueApi;
    private static volatile ShortGetVarValue shortGetVarValue;
    private static volatile ShortUpdateValueByIndex updateValueByIndex;

    private static void ensureInitialized() {
        if (edgeConf == null || getVarNameAndIndex == null || varValueApi == null || shortGetVarValue == null || updateValueByIndex == null) {
            throw new CustomException("dview尚未初始化，请稍后再试");
        }
    }

    /**
     * 获取dview地址
     */
    public synchronized static void refreshCache() throws IOException {
        String ip = getIp();
        String port = getPort();
        if (StrUtil.isEmpty(ip) || StrUtil.isEmpty(port)) {
            log.error("[下发测点]：请配置dview地址");
            // 可选：执行恢复操作，或者返回
            return;
        }
        if (edgeConf == null || !ip.equals(edgeConf.getHost()) || !port.equals(edgeConf.getPort().toString())) {
            edgeConf = new EdgeConf();
            edgeConf.setHost(ip);
            edgeConf.setPort(Integer.valueOf(port));
            apiRefresh(ip, port);
        }
    }

    private static void apiRefresh(String ip, String port) throws IOException {
        getVarNameAndIndex = new ShortGetVarNameAndIndex(ip, Integer.parseInt(port));
        varValueApi = new ShortGetVarValueByIndex(ip, Integer.parseInt(port));
        shortGetVarValue = new ShortGetVarValue(ip, Integer.parseInt(port));
        updateValueByIndex = new ShortUpdateValueByIndex(ip, Integer.parseInt(port));
        cachePointIndex();
    }

    /**
     * 批量查询测点值
     *
     * @param pointInfoList 测点信息批量
     * @throws IOException 网络异常
     */
    public static List<DViewVarInfo> queryPointValue(List<DviewPointDTO> pointInfoList) throws IOException {
        errorCount();
        ensureInitialized();
        pointInfoList = distinct(pointInfoList);
        //返回的集合
        List<DViewVarInfo> dViewVarInfoList = new ArrayList<>(pointInfoList.size());
        //根据type分组
        Map<String, List<DviewPointDTO>> pointMap = pointInfoList.stream().collect(Collectors.groupingBy(DviewPointDTO::getPointType));
        for (Map.Entry<String, List<DviewPointDTO>> entry : pointMap.entrySet()) {
            //type
            String k = entry.getKey();
            //查询的测点DTO
            List<DviewPointDTO> v = entry.getValue();
            //根据type查询测点索引
            if (!reversedIndexMap.containsKey(k)) {
                throw new CustomException("此类型{}在测点下标不存在", k);
            }
            ConcurrentHashMap<String, Integer> stringIntegerConcurrentHashMap = reversedIndexMap.get(k);
            Byte b = VAR_TYPE_MAP.get(k);
            //根据测点id查询测点索引
            List<Object> indexList = v.stream().map(dviewPointDTO -> stringIntegerConcurrentHashMap.get(dviewPointDTO.getPointId())).collect(Collectors.toList());
            //查询对应索引的值
            Map<Integer, DViewVarInfo> dataMap = varValueApi.execute(b, 0, indexList);
            if (MapUtil.isNotEmpty(dataMap)) {
                if (indexMap.containsKey(k)) {
                    throw new CustomException("此类型{}在测点下标不存在", k);
                }
                ConcurrentHashMap<Integer, String> integerStringConcurrentHashMap = indexMap.get(k);
                dataMap.forEach((m, n) -> {
                    n.setType(k);
                    n.setName(integerStringConcurrentHashMap.get(n.getIndex()));
                    dViewVarInfoList.add(n);
                });
            }
        }
        checkDviewValue(dViewVarInfoList.stream().map(DViewVarInfo::getValue).map(Object::toString).collect(Collectors.toList()));
        return dViewVarInfoList;
    }

    /**
     * @param pointId   测点id
     * @param pointType 测点类型
     * @return 测点值
     */
    public static String queryPointValue(String pointId, String pointType) throws IOException {
        errorCount();
        ensureInitialized();
        if (!reversedIndexMap.containsKey(pointType)) {
            throw new CustomException("此类型{}在测点下标不存在", pointType);
        }
        if (!reversedIndexMap.get(pointType).containsKey(pointId)) {
            throw new CustomException("此类型{}测点{}不存在", pointType, pointId);
        }
        //获取 ip  地址
        Map<Integer, DViewVarInfo> dataMap = varValueApi.execute(VAR_TYPE_MAP.get(pointType), 0, Collections.singletonList(reversedIndexMap.get(pointType).get(pointId)));
        if (MapUtil.isNotEmpty(dataMap)) {
            String string = dataMap.values().stream().findFirst().orElseThrow(() -> new IllegalArgumentException("[测点不存在]")).getValue().toString();
            checkDviewValue(string);
            return string;
        }
        throw new IllegalArgumentException("[测点不存在]");
    }

    /**
     * @param pointId   测点id
     * @param pointType 测点类型
     * @return 测点值
     */
    public static String queryCachePointValue(String pointId, String pointType) throws IOException {
        errorCount();
        ensureInitialized();
        String value = null;
        if (pointValueMap.containsKey(pointType)) {
            Map<String, Object> stringObjectMap = pointValueMap.get(pointType);
            if (stringObjectMap.containsKey(pointId)) {
                value = stringObjectMap.get(pointId).toString();
            }
        }
        if (StrUtil.isEmpty(value)) {
            value = queryPointValue(pointId, pointType);
        }
        checkDviewValue(value);
        return value;
    }

    private static void checkDviewValue(String value) {
        if (ZERO.equals(value)) {
            throw new DviewException("边缘网关网络异常");
        }
    }

    private static void checkDviewValue(List<String> value) {
        for (String s : value) {
            if (ZERO.equals(s)) {
                throw new DviewException("边缘网关网络异常");
            }
        }
    }

    public static List<DViewVarInfo> queryCachePointValue(List<DviewPointDTO> pointInfoList) throws IOException {
        errorCount();
        ensureInitialized();
        pointInfoList = distinct(pointInfoList);
        //返回的集合
        List<DViewVarInfo> dViewVarInfoList = new ArrayList<>(pointInfoList.size());
        for (DviewPointDTO dviewPointDTO : pointInfoList) {
            String value = queryCachePointValue(dviewPointDTO.getPointId(), dviewPointDTO.getPointType());
            DViewVarInfo dViewVarInfo = new DViewVarInfo();
            dViewVarInfo.setName(dviewPointDTO.getPointId());
            dViewVarInfo.setValue(value);
            dViewVarInfo.setType(dviewPointDTO.getPointType());
            dViewVarInfoList.add(dViewVarInfo);
        }
        checkDviewValue(dViewVarInfoList.stream().map(DViewVarInfo::getValue).map(Object::toString).collect(Collectors.toList()));
        return dViewVarInfoList;


    }

    /**
     * 修改测点值
     *
     * @param pointId   测点id
     * @param pointType 测点类型
     * @param value     测点值
     */
    public static void writePointValue(String pointId, String pointType, String value) {
        errorCount();
        ensureInitialized();
        //处理线程打断
        if (Thread.currentThread().isInterrupted()) {
            log.info("[下发测点]：线程打断，不下发测点");
            return;
        }
        if (StringUtils.isEmpty(pointId) || StringUtils.isEmpty(pointType)) {
            throw new IllegalArgumentException("[测点信息不全]");
        }
        if (!reversedIndexMap.containsKey(pointType)) {
            throw new CustomException("此类型{}在测点下标不存在", pointType);
        }
        DViewVarInfo dViewVarInfo = new DViewVarInfo();
        dViewVarInfo.setName(pointId);
        dViewVarInfo.setValue(value);
        dViewVarInfo.setType(pointType);
        dViewVarInfo.setIndex(reversedIndexMap.get(pointType).get(pointId));
        DviewCommondResultData execute = updateValueByIndex.execute(VAR_TYPE_MAP.get(pointType), Collections.singletonList(dViewVarInfo));
        if (execute == null || execute.getStatusCode() != 0) {
            throw new IllegalArgumentException("[修改测点值失败]");
        }
    }

    /**
     * 批量修改测点值
     *
     * @param pointInfoList 测点信息
     */
    public static void writePointValue(List<DviewPointDTO> pointInfoList) {
        errorCount();
        ensureInitialized();
        //处理线程打断
        if (Thread.currentThread().isInterrupted()) {
            log.info("[下发测点]：线程打断，不下发测点");
            return;
        }
        //根据type分组
        Map<String, List<DviewPointDTO>> pointMap = pointInfoList.stream().collect(Collectors.groupingBy(DviewPointDTO::getPointType));
        pointMap.forEach((type, dtos) -> {
            Byte b = VAR_TYPE_MAP.get(type);
            List<DViewVarInfo> collect = dtos.stream().map(dviewPointDTO -> {
                String pointId = dviewPointDTO.getPointId();
                DViewVarInfo dViewVarInfo = new DViewVarInfo();
                dViewVarInfo.setName(pointId);
                dViewVarInfo.setValue(dviewPointDTO.getPointValue());
                dViewVarInfo.setType(type);
                if (!reversedIndexMap.containsKey(type) || !reversedIndexMap.get(type).containsKey(pointId)) {
                    throw new CustomException("此类型{}在测点下标不存在", type);
                }
                dViewVarInfo.setIndex(reversedIndexMap.get(type).get(pointId));
                return dViewVarInfo;
            }).collect(Collectors.toList());
            DviewCommondResultData execute = updateValueByIndex.execute(b, collect);
            if (execute == null || execute.getStatusCode() != 0) {
                throw new IllegalArgumentException("[修改测点值失败]");
            }
        });
    }

    /**
     * 用于对象去重
     *
     * @param keyExtractor 需要去重的属性
     * @param <T>          对象
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        //记录已有对象或者属性
        ConcurrentSkipListMap<Object, Boolean> skipListMap = new ConcurrentSkipListMap<>();
        //获取对象的属性值,且使用putIfAbsent判断存在则不添加到map而且返回数值不存在则添加返回null,value恒定为true
        //JSONObject.toJSONString(keyExtractor.apply(t)) 是为了解决null参数和对象比较的问题
        //在Stream distinct()中使用了支持null为key的hashSet来进行处理 java/util/stream/DistinctOps.java:90  但是没有解决对象比较的问题
        //所以虽然序列化消耗性能但是也没有更好的办法
        return t -> skipListMap.putIfAbsent(JSON.toJSONString(keyExtractor.apply(t)), Boolean.TRUE) == null;
    }

    private static List<DviewPointDTO> distinct(List<DviewPointDTO> pointInfoList) {
        return pointInfoList.stream().filter(distinctByKey(dviewPointDTO -> Stream.of(dviewPointDTO.getPointId(), dviewPointDTO.getPointType()).toArray())).collect(Collectors.toList());
    }

    public synchronized static void cachePointIndex() throws IOException {
        if (checkSocket(edgeConf.getHost(), edgeConf.getPort())) {
            return;
        }
        for (String type : INDEX_TYPE) {
            Byte typeByte = VAR_TYPE_MAP.get(type);
            try {
                Map<Integer, String> map = getVarNameAndIndex.execute(typeByte, 0, 0);
                if (CollectionUtil.isNotEmpty(map)) {
                    indexMap.put(type, new ConcurrentHashMap<>(map));
                    reversedIndexMap.put(type, reverseMap(map));
                }
            } catch (IOException e) {
                errorCount.getAndIncrement();
                log.error("缓存测点索引失败,类型：{}", type, e);
                // 可以考虑重试或其他错误处理机制
                throw e;
            }
        }
    }

    private static ConcurrentHashMap<String, Integer> reverseMap(Map<Integer, String> map) {
        ConcurrentHashMap<String, Integer> reversedMap = new ConcurrentHashMap<>();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            reversedMap.put(entry.getValue(), entry.getKey());
        }
        return reversedMap;
    }

    private static String getIp() {
        String ip;
        //先命中咖啡因
        String caffeKey = DVIEW_ADDRESS + DVIEW_IP;
        if (CaffeineUtil.getIndexCache(caffeKey)) {
            ip = CaffeineUtil.getStingCache(caffeKey);
        } else {
            ip = DictUtils.getDictValue(DVIEW_ADDRESS, DVIEW_IP);
        }
        return ip;
    }

    private static String getPort() {
        String port;
        String caffeKey = DVIEW_ADDRESS + DVIEW_PORT;
        if (CaffeineUtil.getIndexCache(caffeKey)) {
            port = CaffeineUtil.getStingCache(caffeKey);
        } else {
            port = DictUtils.getDictValue(DVIEW_ADDRESS, DVIEW_PORT);
        }
        return port;
    }

    private static void errorCount() {
        if (errorCount.get() > 3) {
            throw new DviewException("内部通信异常");
        }
    }

    private static boolean checkSocket(String ip, int port) {
        if (errorCount.get() > 3) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, port), 200);
                errorCount.set(0);
                log.info("dview连接恢复正常");
                return false;
            } catch (IOException e) {
                log.error("dview连接异常");
                return true;
            }
        }
        return false;

    }

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void cachePointValue() {
        if (!IS_AUTO_RUNNING) {
            return;
        }
        if (checkSocket(edgeConf.getHost(), edgeConf.getPort())) {
            return;
        }
        for (String type : POINT_TYPE) {
            try {
                if (!indexMap.containsKey(type)) {
                    cachePointIndex();
                    continue;
                }
                ConcurrentHashMap<Integer, String> map = indexMap.get(type);
                Map<Integer, DViewVarInfo> execute = shortGetVarValue.execute(VAR_TYPE_MAP.get(type), 0, 0, 0);
                if (MapUtil.isNotEmpty(execute)) {
                    Map<String, Object> kv = new ConcurrentHashMap<>();
                    execute.forEach((key, value) -> {
                        String indexName = map.get(key);
                        if (indexName != null) {
                            kv.put(indexName, value.getValue());
                        }
                    });
                    pointValueMap.put(type, kv);
                }
            } catch (IOException e) {
                errorCount.getAndIncrement();
                log.error("缓存测点值失败");
                pointValueMap.forEach((s, stringObjectMap) -> pointValueMap.remove(s));
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void ScheduledRefresh() {
        if (!IS_AUTO_RUNNING) {
            return;
        }
        try {
            refreshCache();
        } catch (IOException e) {
            log.error("定时刷新dview地址并更新缓存失败", e);
        }
    }
}
