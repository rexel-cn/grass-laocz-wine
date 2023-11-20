package com.rexel.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.config.PulseUrlConfig;
import com.rexel.common.core.domain.entity.SysTenant;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.JsonUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.pulse.PulseHttpRequestUtil;
import com.rexel.common.utils.pulse.PulseHttpResponseUtil;
import com.rexel.system.domain.GrassReduceInfo;
import com.rexel.system.domain.dto.ReduceDataDTO;
import com.rexel.system.domain.dto.TimeSeriesDataDTO;
import com.rexel.system.domain.dto.TimeSeriesWindowDTO;
import com.rexel.system.domain.vo.*;
import com.rexel.system.mapper.GrassReduceInfoMapper;
import com.rexel.system.mapper.SysDictDataMapper;
import com.rexel.system.mapper.SysTenantMapper;
import com.rexel.system.service.IGrassAnalysisService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 趋势分析Service业务层处理
 *
 * @author ids
 * @date 2022-11-01
 */
@Slf4j
@Service
public class GrassAnalysisServiceImpl implements IGrassAnalysisService {
    @Autowired
    private GrassReduceInfoMapper grassReduceInfoMapper;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private PulseUrlConfig pulseUrlConfig;

    private final static String DEFAULT_FUNC = "last";
    private final static String DEFAULT_INTERVAL = "1h";
    private final static String CHART_DATA = "chartData";
    private final static String TABLE_DATA = "tableData";
    private final static String TIME = "time";
    private final static String SERIES = "series";
    private final static String NAME = "name";
    private final static String TYPE = "type";
    private final static String CONNECT_NULLS = "connectNulls";
    private final static String DATA = "data";
    private final static String LINE = "line";
    private final static String PROP = "prop";
    private final static String PROP_NAME = "propName";
    private final static String HAS_SORT = "hasSort";
    private final static String DATA_LIST = "dataList";
    private final static String COLUMN_LIST = "columnList";
    private final static String ORIGINAL_VALUE = "0";
    private final static String REDUCE_OPEN = "open";

    /**
     * 查询趋势分析数据
     *
     * @param vo 查询参数
     * @return 结果
     */
    @Override
    public JSONObject queryTimeSeries(TrendAnalysisVo vo) {
        // 检查请求参数
        checkParam(vo);

        // 查询所有租户
        Map<String, SysTenant> tenantMap = getTenantMap();

        // 设置租户信息
        setTenantInfo(vo, tenantMap);

        // 测点重复判断
        setDuplicate(vo);

        // 查询时序数据
        Map<String, List<ResultData>> tenantDataMap = queryTenantData(vo, tenantMap);

        // 数据对齐处理
        List<ResultData> alignDataList = doDataAlign(tenantDataMap);

        // 生成图表数据
        JSONObject chartData = convertToChartData(vo, alignDataList);

        // 转换表格数据
        JSONObject tableData = convertToTableData(alignDataList);

        // 返回结果数据
        JSONObject result = new JSONObject();
        result.put(CHART_DATA, chartData);
        if (!vo.isHideTableData()) {
            result.put(TABLE_DATA, tableData);
        }
        return result;
    }

    /**
     * 数据时间对齐
     *
     * @param tenantMap tenantMap
     */
    private List<ResultData> doDataAlign(Map<String, List<ResultData>> tenantMap) {
        // 生成时间合集
        List<String> allTimeList = new ArrayList<>();
        List<String> allPointList = new ArrayList<>();
        for (Map.Entry<String, List<ResultData>> entry : tenantMap.entrySet()) {
            List<ResultData> dataList = entry.getValue();
            for (ResultData resultData : dataList) {
                if (!allTimeList.contains(resultData.getTime())) {
                    allTimeList.add(resultData.getTime());
                }
                if (!allPointList.contains(resultData.getPointName())) {
                    allPointList.add(resultData.getPointName());
                }
            }
        }

        // 数据格式转换
        Map<String, ResultData> dataMap = new HashMap<>(1);
        tenantMap.forEach((tenantId, list) -> list.forEach(d -> dataMap.put(d.getKey(), d)));

        // 遍历所有时间及显示测点
        List<ResultData> result = new ArrayList<>();
        for (String t : allTimeList) {
            for (String p : allPointList) {
                result.add(tryGetResultData(t, p, dataMap));
            }
        }

        // 数据重新排序
        Comparator<ResultData> byTime = Comparator.comparing(ResultData::getTime);
        Comparator<ResultData> byPoint = Comparator.comparing(ResultData::getPointName).reversed();
        result.sort(byTime.thenComparing(byPoint));
        return result;
    }

    /**
     * 获取并填补空数据
     *
     * @param time time
     * @param pointName pointName
     * @param dataMap dataMap
     * @return ResultData
     */
    private ResultData tryGetResultData(String time, String pointName, Map<String, ResultData> dataMap) {
        String key = time + "," + pointName;
        if (dataMap.containsKey(key)) {
            return dataMap.get(key);
        } else {
            ResultData result = new ResultData();
            result.setTime(time);
            result.setPointName(pointName);
            result.setPointValue("");
            return result;
        }
    }

    /**
     * 参数检查
     *
     * @param vo vo
     */
    private void checkParam(TrendAnalysisVo vo) {
        if (StringUtils.isEmpty(vo.getFrom())) {
            throw new CustomException("请指定开始时间");
        }
        if (StringUtils.isEmpty(vo.getTo())) {
            throw new CustomException("请指定结束时间");
        }
        if (vo.getPointList().size() <= 0) {
            throw new CustomException("请指定查询测点");
        }

        // 检查限制条件
        checkLimit(vo);
    }

    /**
     * 检查限制条件
     *
     * @param vo vo
     */
    private void checkLimit(TrendAnalysisVo vo) {
        if (!isOriginalValue(vo.getInterval())) {
            return;
        }

        Date from = strToDate(vo.getFrom());
        Date to = strToDate(vo.getTo());

        //开始时间与结束时间不能一样
        if (from != null && to != null && from.getTime() == to.getTime()) {
            throw new CustomException("开始时间与结束时间不能一样。");
        }

        Date limit = DateUtils.addDays(to, -1);
        if (from != null && from.before(limit)) {
            throw new CustomException("查询原始值时，开始时间与结束时间不能超过1天。");
        }
    }

    /**
     * 查询租户集合
     *
     * @return 结果
     */
    private Map<String, SysTenant> getTenantMap() {
        List<SysTenant> list = sysTenantMapper.selectList(null);
        Map<String, SysTenant> result = new HashMap<>();
        if (list != null) {
            list.forEach(info -> result.put(info.getTenantId(), info));
        }
        return result;
    }

    /**
     * 同步租户名称
     *
     * @param vo vo
     * @param tenantMap tenantMap
     */
    private void setTenantInfo(TrendAnalysisVo vo, Map<String, SysTenant> tenantMap) {
        for (TrendPointInfo pointInfo : vo.getPointList()) {
            SysTenant sysTenant = tenantMap.get(pointInfo.getTenantId());
            pointInfo.setTenantName(sysTenant.getTenantName());
            pointInfo.setTenantEngName(sysTenant.getEngName());
        }
    }

    /**
     * 测点重复判断
     *
     * @param vo vo
     */
    private void setDuplicate(TrendAnalysisVo vo) {
        List<TrendPointInfo> pointInfoList = vo.getPointList();

        // 测点名称集合, Map<pointName, List>
        Map<String, List<TrendPointInfo>> pointNameMap = new HashMap<>();

        // 租户内测点集合, Map<tenantId, Map<pointId, List>>
        Map<String, Map<String, List<TrendPointInfo>>> inTenantMap = new HashMap<>();

        // 租户间测点集合, Map<pointId, Map<tenantId, List>>
        Map<String, Map<String, List<TrendPointInfo>>> overTenantMap = new HashMap<>();

        // 遍历所有请求参数
        for (TrendPointInfo pointInfo : pointInfoList) {
            String tenantId = pointInfo.getTenantId();
            String pointId = pointInfo.getPointId();
            String pointName = pointInfo.getPointName();

            // 测点名称集合
            if (!pointNameMap.containsKey(pointName)) {
                pointNameMap.put(pointName, new ArrayList<>());
            }
            pointNameMap.get(pointName).add(pointInfo);

            // 租户内测点集合
            if (!inTenantMap.containsKey(tenantId)) {
                inTenantMap.put(tenantId, new HashMap<>());
            }
            if (!inTenantMap.get(tenantId).containsKey(pointId)) {
                inTenantMap.get(tenantId).put(pointId, new ArrayList<>());
            }
            inTenantMap.get(tenantId).get(pointId).add(pointInfo);

            // 租户间测点集合
            if (!overTenantMap.containsKey(pointId)) {
                overTenantMap.put(pointId, new HashMap<>());
            }
            if (!overTenantMap.get(pointId).containsKey(tenantId)) {
                overTenantMap.get(pointId).put(tenantId, new ArrayList<>());
            }
            overTenantMap.get(pointId).get(tenantId).add(pointInfo);
        }

        // 设置测点重复状态
        for (TrendPointInfo pointInfo : pointInfoList) {
            String tenantId = pointInfo.getTenantId();
            String pointId = pointInfo.getPointId();
            String pointName = pointInfo.getPointName();

            pointInfo.setDuplicatePointName(false);
            pointInfo.setDuplicateInTenant(false);
            pointInfo.setDuplicateOverTenant(false);

            // 检查测点名称重复
            if (pointNameMap.containsKey(pointName)) {
                if (pointNameMap.get(pointName).size() > 1) {
                    pointInfo.setDuplicatePointName(true);
                }
            }

            // 检查租户内测点重复
            if (inTenantMap.containsKey(tenantId)) {
                if (inTenantMap.get(tenantId).containsKey(pointId)) {
                    if (inTenantMap.get(tenantId).get(pointId).size() > 1) {
                        pointInfo.setDuplicateInTenant(true);
                    }
                }
            }

            // 检查租户间测点重复
            if (overTenantMap.containsKey(pointId)) {
                if (overTenantMap.get(pointId).size() > 1) {
                    pointInfo.setDuplicateOverTenant(true);
                }
            }
        }
    }

    /**
     * 查询时序数据
     *
     * @param vo vo
     * @param tenantMap tenantMap
     * @return 结果
     */
    private Map<String, List<ResultData>> queryTenantData(TrendAnalysisVo vo, Map<String, SysTenant> tenantMap) {
        // 设置默认参数
        String func = getFunction(vo);
        String from = getFromTime(vo);
        String to = getToTime(vo);
        String interval = getInterval(vo);

        // 是否使用预聚合数据
        boolean isUseReduce = isUseReduce(interval);

        // 构建租户设备
        Map<String, Map<String, List<TrendPointInfo>>> tenantDeviceMap = getTenantDeviceMap(vo);

        // 遍历所有租户
        Map<String, List<ResultData>> result = new HashMap<>();
        for (Map.Entry<String, Map<String, List<TrendPointInfo>>> tenantEntry : tenantDeviceMap.entrySet()) {
            String tenantId = tenantEntry.getKey();
            SysTenant sysTenant = tenantMap.get(tenantId);
            String bucketId = sysTenant.getBucketId();

            // 遍历物联设备
            for (Map.Entry<String, List<TrendPointInfo>> deviceEntry : tenantEntry.getValue().entrySet()) {
                String deviceId = deviceEntry.getKey();
                List<TrendPointInfo> pointList = deviceEntry.getValue();

                List<ResultData> dataList;
                if (isOriginalValue(interval)) {
                    dataList = doQueryData(from, to, bucketId, deviceId, pointList);
                } else {
                    if (isUseReduce) {
                        dataList = doQueryReduceData(from, to, bucketId, deviceId, func, interval, pointList);
                    } else {
                        dataList = doQueryWindow(from, to, bucketId, deviceId, func, interval, pointList);
                    }
                }

                if (dataList.size() > 0) {
                    result.put(tenantId, dataList);
                }
            }
        }
        return result;
    }

    /**
     * 是否使用预聚合数据
     *
     * @param interval interval
     * @return 结果
     */
    private boolean isUseReduce(String interval) {
        // 数据源配置
        String status = sysDictDataMapper.selectDictLabel("use_reduce", "status");
        if (StringUtils.isEmpty(status) || !REDUCE_OPEN.equals(status)) {
            return false;
        }

        // 查询预聚合信息
        List<GrassReduceInfo> reduceList = grassReduceInfoMapper.selectGrassReduceInfoList(null);
        if (reduceList == null || reduceList.size() <= 0) {
            return false;
        }

        for (GrassReduceInfo reduceInfo : reduceList) {
            if (interval.equals(reduceInfo.getInterval())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为原始值
     *
     * @param interval interval
     * @return 结果
     */
    private boolean isOriginalValue(String interval) {
        return ORIGINAL_VALUE.equals(interval) || StringUtils.isEmpty(interval);
    }

    /**
     * 转换为图表数据格式
     *
     * @param vo vo
     * @param alignDataList alignDataList
     * @return 结果
     */
    private JSONObject convertToChartData(TrendAnalysisVo vo, List<ResultData> alignDataList) {
        // 按照租户转换结果数据
        JSONArray tenantArray = new JSONArray();
        List<String> timeList = new ArrayList<>();
        Map<String, List<String>> valueMap = new HashMap<>(1);
        for (ResultData resultData : alignDataList) {
            String time = resultData.getTime();
            String pointName = resultData.getPointName();
            if (!timeList.contains(time)) {
                timeList.add(time);
            }
            if (!valueMap.containsKey(pointName)) {
                valueMap.put(pointName, new ArrayList<>());
            }
            valueMap.get(pointName).add(resultData.getPointValue());
        }

        JSONArray seriesArray = new JSONArray();
        valueMap.forEach((pointName, values) -> {
            JSONObject seriesJson = new JSONObject();
            seriesJson.put(NAME, pointName);
            seriesJson.put(TYPE, LINE);
            seriesJson.put(DATA, values);
            seriesJson.put(CONNECT_NULLS, vo.isConnectNulls());
            seriesArray.add(seriesJson);
        });

        JSONObject tenantJson = new JSONObject();
        tenantJson.put(TIME, timeList);
        tenantJson.put(SERIES, seriesArray);
        tenantArray.add(tenantJson);

        // 合并所有租户数据
        JSONObject result = new JSONObject();
        result.put(TIME, new JSONArray());
        result.put(SERIES, new JSONArray());
        boolean existTime = false;
        for (int i = 0; i < tenantArray.size(); i++) {
            JSONObject deviceJson = tenantArray.getJSONObject(i);
            if (!existTime) {
                result.getJSONArray(TIME).addAll(deviceJson.getJSONArray(TIME));
                existTime = true;
            }
            result.getJSONArray(SERIES).addAll(deviceJson.getJSONArray(SERIES));
        }

        return result;
    }

    /**
     * 转换为表格数据格式
     *
     * @param alignDataList alignDataList
     * @return 结果
     */
    private JSONObject convertToTableData(List<ResultData> alignDataList) {
        // 测点与名称集合
        Map<String, String> pointMap = new HashMap<>();
        for (ResultData resultData : alignDataList) {
            String pointId = resultData.getPointId();
            String pointName = resultData.getPointName();
            if (StringUtils.isEmpty(pointId)) {
                continue;
            }
            if (!pointMap.containsKey(pointId)) {
                pointMap.put(pointId, pointName);
            }
        }

        // 时间与数据集合
        Map<String, JSONObject> timeDataMap = new HashMap<>();
        for (ResultData resultData : alignDataList) {
            String time = resultData.getTime();
            String pointId = resultData.getPointId();
            if (StringUtils.isEmpty(pointId)) {
                continue;
            }
            if (!timeDataMap.containsKey(time)) {
                timeDataMap.put(time, new JSONObject());
            }
            JSONObject dataJson = timeDataMap.get(time);
            dataJson.put(resultData.getPointName(), resultData.pointValue);
        }

        // 构建表格数据头
        JSONArray columnList = new JSONArray();
        pointMap.forEach((pointId, pointName) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PROP, pointName);
            jsonObject.put(PROP_NAME, pointName);
            jsonObject.put(HAS_SORT, false);
            columnList.add(jsonObject);
        });

        // 构建表格数据体
        JSONArray dataList = new JSONArray();
        timeDataMap.forEach((time, dataJson) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(TIME, time);
            jsonObject.putAll(dataJson);
            dataList.add(jsonObject);
        });

        // 按照降序排序
        JSONObject result = new JSONObject();
        result.put(COLUMN_LIST, columnList);
        result.put(DATA_LIST, JsonUtils.jsonArraySort(dataList, TIME, true));
        return result;
    }

    /**
     * 转换数据结构
     *
     * @param deviceId  deviceId
     * @param pointId   pointId
     * @param time      time
     * @param value     value
     * @param pointList pointList
     * @return 结果
     */
    private ResultData convertToResultData(
            String deviceId, String pointId, String time, Double value, List<TrendPointInfo> pointList) {
        TrendPointInfo pointInfo = getOnePointInfo(deviceId, pointId, pointList);
        if (pointInfo == null) {
            log.error("pointInfo is null. pointId={}", pointId);
            return null;
        }

        String displayName;
        if (StringUtils.isEmpty(pointInfo.getPointName())) {
            displayName = pointInfo.getPointId();
        } else {
            displayName = pointInfo.getPointId() + "(" + pointInfo.getPointName() + ")";
        }
        if (pointInfo.isDuplicateInTenant()) {
            String assetName = pointInfo.getAssetName();
            if (StringUtils.isNotEmpty(assetName)) {
                displayName = "[" + assetName + "]" + displayName;
            }
        }
        if (pointInfo.isDuplicateOverTenant()) {
            String tenantEngName = pointInfo.getTenantEngName();
            if (StringUtils.isNotEmpty(tenantEngName)) {
                displayName = "[" + tenantEngName + "]" + displayName;
            }
        }

        // 结果数据
        ResultData resultData = new ResultData();
        resultData.setTenantId(pointInfo.getTenantId());
        resultData.setTime(time);
        resultData.setPointId(pointId);
        resultData.setPointValue(value.toString());
        resultData.setPointName(displayName);
        return resultData;
    }

    /**
     * 执行原始数据查询
     *
     * @param from      from
     * @param to        to
     * @param bucketId  bucketId
     * @param deviceId  deviceId
     * @param pointList pointList
     * @return 结果
     */
    private List<ResultData> doQueryData(
            String from, String to, String bucketId, String deviceId, List<TrendPointInfo> pointList) {
        List<String> pointIdList = new ArrayList<>();
        pointList.forEach(info -> {
            if (!pointIdList.contains(info.getPointId())) {
                pointIdList.add(info.getPointId());
            }
        });

        TimeSeriesDataDTO query = new TimeSeriesDataDTO();
        query.setBucketId(bucketId);
        query.setDeviceId(deviceId);
        query.setStartTime(from);
        query.setStopTime(to);
        query.setPointIdList(pointIdList);

        List<ResultData> resultList = new ArrayList<>();
        String url = pulseUrlConfig.getTimeSeriesData();
        String resp = PulseHttpRequestUtil.sendPostJson(url, JSON.toJSONString(query));
        List<TimeSeriesDataVO> list = PulseHttpResponseUtil.pretreatmentResultsList(resp, TimeSeriesDataVO.class);
        if (list == null) {
            return resultList;
        }

        for (TimeSeriesDataVO v : list) {
            resultList.add(convertToResultData(v.getDeviceId(), v.getPointId(), v.getTime(), v.getValue(), pointList));
        }
        return resultList;
    }

    /**
     * 执行聚合数查询
     *
     * @param from      from
     * @param to        to
     * @param bucketId  bucketId
     * @param deviceId  deviceId
     * @param func      func
     * @param interval  interval
     * @param pointList pointList
     * @return 结果
     */
    private List<ResultData> doQueryWindow(
            String from, String to, String bucketId, String deviceId,
            String func, String interval, List<TrendPointInfo> pointList) {
        List<String> pointIdList = new ArrayList<>();
        pointList.forEach(info -> {
            if (!pointIdList.contains(info.getPointId())) {
                pointIdList.add(info.getPointId());
            }
        });

        TimeSeriesWindowDTO query = new TimeSeriesWindowDTO();
        query.setBucketId(bucketId);
        query.setDeviceId(deviceId);
        query.setStartTime(from);
        query.setStopTime(to);
        query.setFuncList(Collections.singletonList(func));
        query.setFillType(0);
        query.setEvery(interval);
        query.setPointIdList(pointIdList);

        List<ResultData> resultList = new ArrayList<>();
        String url = pulseUrlConfig.getTimeSeriesWindow();
        String resp = PulseHttpRequestUtil.sendPostJson(url, JSON.toJSONString(query));
        List<TimeSeriesWindowVO> list = PulseHttpResponseUtil.pretreatmentResultsList(resp, TimeSeriesWindowVO.class);
        if (list == null) {
            return resultList;
        }

        for (TimeSeriesDataVO v : list) {
            resultList.add(convertToResultData(v.getDeviceId(), v.getPointId(), v.getTime(), v.getValue(), pointList));
        }
        return resultList;
    }

    /**
     * 执行聚合数查询
     *
     * @param from      from
     * @param to        to
     * @param bucketId  bucketId
     * @param deviceId  deviceId
     * @param func      func
     * @param interval  interval
     * @param pointList pointList
     * @return 结果
     */
    private List<ResultData> doQueryReduceData(
            String from, String to, String bucketId, String deviceId,
            String func, String interval, List<TrendPointInfo> pointList) {
        List<String> pointIdList = new ArrayList<>();
        pointList.forEach(info -> {
            if (!pointIdList.contains(info.getPointId())) {
                pointIdList.add(info.getPointId());
            }
        });

        ReduceDataDTO query = new ReduceDataDTO();
        query.setBucketId(bucketId);
        query.setDeviceId(deviceId);
        query.setStartTime(from);
        query.setStopTime(to);
        query.setFuncList(Collections.singletonList(func));
        query.setPointIdList(pointIdList);
        query.setReduceName(interval);

        List<ResultData> resultList = new ArrayList<>();
        String url = pulseUrlConfig.getReduceData();
        String resp = PulseHttpRequestUtil.sendPostJson(url, JSON.toJSONString(query));
        List<ReduceDataVO> list = PulseHttpResponseUtil.pretreatmentResultsList(resp, ReduceDataVO.class);
        if (list == null) {
            return resultList;
        }

        for (ReduceDataVO v : list) {
            resultList.add(convertToResultData(v.getDeviceId(), v.getPointId(), v.getTime(), v.getValue(), pointList));
        }
        return resultList;
    }

    /**
     * 获取单个测点数据
     *
     * @param deviceId  deviceId
     * @param pointId   pointId
     * @param pointList pointList
     * @return 结果
     */
    private TrendPointInfo getOnePointInfo(String deviceId, String pointId, List<TrendPointInfo> pointList) {
        for (TrendPointInfo pointInfo : pointList) {
            if (deviceId.equals(pointInfo.getDeviceId()) && pointId.equals(pointInfo.getPointId())) {
                return pointInfo;
            }
        }
        return null;
    }

    /**
     * 获取设备单位的点位列表
     *
     * @return 点位列表
     */
    private Map<String, Map<String, List<TrendPointInfo>>> getTenantDeviceMap(TrendAnalysisVo trendAnalysisVo) {
        Map<String, Map<String, List<TrendPointInfo>>> tenantMap = new HashMap<>(1);
        for (TrendPointInfo point : trendAnalysisVo.getPointList()) {
            String tenantId = point.getTenantId();
            String deviceId = point.getDeviceId();
            if (!tenantMap.containsKey(tenantId)) {
                tenantMap.put(tenantId, new HashMap<>(1));
            }
            Map<String, List<TrendPointInfo>> deviceMap = tenantMap.get(tenantId);
            if (!deviceMap.containsKey(deviceId)) {
                deviceMap.put(deviceId, new ArrayList<>());
            }
            deviceMap.get(deviceId).add(point);
        }
        return tenantMap;
    }

    /**
     * 取得开始时间
     *
     * @param trendAnalysisVo 参数
     * @return 开始时间
     */
    private String getFromTime(TrendAnalysisVo trendAnalysisVo) {
        String from = trendAnalysisVo.getFrom();
        if (StringUtils.isEmpty(from)) {
            Date defaultFrom = DateUtils.addDays(new Date(), -1);
            return DateUtils.dateToStr(defaultFrom, DateUtils.YYYY_MM_DD_HH_MM_SS_SSS);
        }
        return DateUtils.dateToStr(strToDate(from), DateUtils.YYYY_MM_DD_HH_MM_SS_SSS);
    }

    /**
     * 取得结束时间
     *
     * @param trendAnalysisVo 参数
     * @return 结束时间
     */
    private String getToTime(TrendAnalysisVo trendAnalysisVo) {
        String to = trendAnalysisVo.getTo();
        if (StringUtils.isEmpty(to)) {
            return DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS_SSS);
        }
        return DateUtils.dateToStr(strToDate(to), DateUtils.YYYY_MM_DD_HH_MM_SS_SSS);
    }

    /**
     * 取得聚合功能列表
     *
     * @param trendAnalysisVo 参数
     * @return 功能列表
     */
    private String getFunction(TrendAnalysisVo trendAnalysisVo) {
        if (StringUtils.isEmpty(trendAnalysisVo.getFunc())) {
            return DEFAULT_FUNC;
        } else {
            return trendAnalysisVo.getFunc();
        }
    }

    /**
     * 取得时间间隔
     *
     * @param trendAnalysisVo trendAnalysisVo
     * @return 结果
     */
    private String getInterval(TrendAnalysisVo trendAnalysisVo) {
        String interval = trendAnalysisVo.getInterval();
        if (StringUtils.isEmpty(interval)) {
            return DEFAULT_INTERVAL;
        }
        return interval;
    }

    /**
     * 转换时间格式
     *
     * @param str str
     * @return 结果
     */
    private Date strToDate(String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.YYYY_MM_DD_HH_MM_SS_SSS);
            return sdf.parse(str);
        } catch (ParseException ignored) {
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.YYYY_MM_DD_HH_MM_SS);
            return sdf.parse(str);
        } catch (ParseException ignored) {
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.YYYY_MM_DD);
            return sdf.parse(str);
        } catch (ParseException ignored) {
        }
        return null;
    }

    /**
     * 结果数据对象
     */
    @Data
    private static class ResultData {
        /**
         * 租户ID
         */
        private String tenantId;
        /**
         * 时间
         */
        private String time;
        /**
         * 测点ID
         */
        private String pointId;
        /**
         * 测点名称
         */
        private String pointName;
        /**
         * 测点值
         */
        private String pointValue;

        /**
         * 获取数据值
         *
         * @return key
         */
        public String getKey() {
            return time + "," + pointName;
        }
    }
}
