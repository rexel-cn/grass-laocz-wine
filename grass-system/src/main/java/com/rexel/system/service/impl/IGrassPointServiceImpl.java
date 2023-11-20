package com.rexel.system.service.impl;/**
 * @Author 董海
 * @Date 2022/7/20 10:06
 * @version 1.0
 */

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.config.PulseUrlConfig;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.enums.QtyStatus;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.ExecutorTransactionUtil;
import com.rexel.common.utils.PageUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.pulse.PulseHttpRequestUtil;
import com.rexel.common.utils.pulse.PulseHttpResponseUtil;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.dto.GrassAssetPointDTO;
import com.rexel.system.domain.dto.PulsePointCurveDTO;
import com.rexel.system.domain.dto.PulsePointQueryDTO;
import com.rexel.system.domain.dto.TimeSeriesLastDTO;
import com.rexel.system.domain.vo.PointQueryVO;
import com.rexel.system.domain.vo.PointTimeDataVO;
import com.rexel.system.domain.vo.TimeSeriesLastVO;
import com.rexel.system.domain.vo.common.NumberCountVO;
import com.rexel.system.mapper.GrassPointInfoMapper;
import com.rexel.system.service.IGrassAssetService;
import com.rexel.system.service.IGrassPointService;
import com.rexel.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName IGrassPointServiceImpl
 * @Description IGrassPointServiceImpl
 * @Author Hai.Dong
 * @Date 2022/7/20 10:06
 **/
@Service
public class IGrassPointServiceImpl extends ServiceImpl<GrassPointInfoMapper, GrassPointInfo> implements IGrassPointService {
    private static final String POINT_TYPE_DICT = "point_type";
    private static final String POINT_UNIT_DICT = "point_unit";

    @Autowired
    private GrassPointInfoMapper pointInfoMapper;
    @Autowired
    private PulseUrlConfig pulseUrlConfig;
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private IGrassAssetService grassAssetService;


    /**
     * newList,oldList=删除数据
     *
     * @param firstArrayList  第一集合
     * @param secondArrayList 第二集合
     */
    private static void findDiff(List<GrassPointInfo> firstArrayList, List<GrassPointInfo> secondArrayList) {
        Map<String, GrassPointInfo> firstPointInfoMap = secondArrayList.stream().
                collect(Collectors.toMap(grassPointInfo -> grassPointInfo.getDeviceId() + grassPointInfo.getPointId(), Function.identity()));
        firstArrayList.forEach(grassPointInfo -> {
            String deviceId = grassPointInfo.getDeviceId();
            String pointId = grassPointInfo.getPointId();
            if (ObjectUtil.isEmpty(firstPointInfoMap.get(deviceId + pointId))) {
                throw new ServiceException("数据不一致");
            }
        });
    }

    /**
     * oldList,newList=修改数据    导入用
     *
     * @param oldArrayList 旧数据
     * @param newArrayList 新数据
     * @return 修改的数据
     */
    private static List<GrassPointInfo> importSameList(List<GrassPointInfo> oldArrayList, List<GrassPointInfo> newArrayList) {
        Map<String, GrassPointInfo> firstPointInfoMap = oldArrayList.stream().
                collect(Collectors.toMap(grassPointInfo -> grassPointInfo.getDeviceId() + grassPointInfo.getPointId(), Function.identity()));
        List<GrassPointInfo> pointInfos = new ArrayList<>();
        newArrayList.forEach(grassPointInfo -> {
            String deviceId = grassPointInfo.getDeviceId();
            String pointId = grassPointInfo.getPointId();
            String key = deviceId + pointId;
            if (firstPointInfoMap.containsKey(key)) {
                GrassPointInfo pointInfo = firstPointInfoMap.get(key);
                String oldValue = pointInfo.getDeviceId()
                        + pointInfo.getDeviceName()
                        + pointInfo.getPointId()
                        + pointInfo.getPointType()
                        + pointInfo.getPointUnit()
                        + pointInfo.getInMax()
                        + pointInfo.getInMin()
                        + pointInfo.getOutMax()
                        + pointInfo.getOutMin();
                String newValue = grassPointInfo.getDeviceId()
                        + grassPointInfo.getDeviceName()
                        + grassPointInfo.getPointId()
                        + grassPointInfo.getPointType()
                        + grassPointInfo.getPointUnit()
                        + grassPointInfo.getInMax()
                        + grassPointInfo.getInMin()
                        + grassPointInfo.getOutMax()
                        + grassPointInfo.getOutMin();
                if (!oldValue.equals(newValue)) {
                    pointInfos.add(grassPointInfo);
                }
            }
        });
        return pointInfos;
    }


    @Override
    public List<PointTimeDataVO> selectByAsset(GrassAssetPointDTO grassAssetPointDTO) {
        return baseMapper.selectByAsset(grassAssetPointDTO);
    }

    @Override
    public List<GrassPointInfo> selectByAssetId(Long id) {
        return pointInfoMapper.selectByAssetId(id);
    }

    /**
     * 查询测点列表
     *
     * @param pointQueryDTO
     * @return
     */
    @Override
    public List<PointQueryVO> getListPage(PulsePointQueryDTO pointQueryDTO) {
        List<PointQueryVO> list = pointInfoMapper.getList(pointQueryDTO);

        pointDisplayNameConvert(list);
        return list;
    }

    /**
     * 查询测点列表
     *
     * @param pointQueryDTO
     * @return
     */
    @Override
    public List<PointQueryVO> getListPageRealValue(PulsePointQueryDTO pointQueryDTO) {
        getAssetIds(pointQueryDTO);
        List<PointQueryVO> listPage;
        try {
            PageUtils.startPage();
            listPage = pointInfoMapper.getList(pointQueryDTO);
            pointDisplayNameConvert(listPage);
        } finally {
            PageUtils.clearPage();
        }

        //获取测点实时值和 测点状态
        pointQtyQueryToPulse(listPage);

        return listPage;
    }

    /**
     * 查询测点的 qty  状态
     *
     * @return
     */
    private List<PointQueryVO> pointQtyQueryToPulse(List<PointQueryVO> listPage) {
        //获取测点实时值和 测点状态
        if (CollectionUtil.isNotEmpty(listPage)) {
            Map<String, List<String>> map = listPage.stream().collect(Collectors.groupingBy(PointQueryVO::getDeviceId,
                    Collectors.mapping(PointQueryVO::getPointId, Collectors.toList())));
            List<TimeSeriesLastVO> timeSeriesLastVOS = new ArrayList<>();
            map.forEach((k, v) -> {
                TimeSeriesLastDTO timeSeriesLastDTO = new TimeSeriesLastDTO();
                timeSeriesLastDTO.setDeviceId(k);
                timeSeriesLastDTO.setPointIdList(v);

                List<TimeSeriesLastVO> voList = PulseHttpResponseUtil
                        .pretreatmentResultsList(PulseHttpRequestUtil
                                .sendPostJson(pulseUrlConfig.getTimeSeriesBatch(), JSON.toJSONString(timeSeriesLastDTO)), TimeSeriesLastVO.class);
                if (CollectionUtil.isNotEmpty(voList)) {
                    timeSeriesLastVOS.addAll(voList);
                }
            });
            Map<String, TimeSeriesLastVO> stringTimeSeriesLastVOMap = timeSeriesLastVOS.stream().
                    collect(Collectors.toMap(timeSeriesLastVO ->
                            timeSeriesLastVO.getDeviceId() + timeSeriesLastVO.getPointId(), Function.identity()));
            listPage.forEach(pointQueryVO -> {
                String key = pointQueryVO.getDeviceId() + pointQueryVO.getPointId();
                if (stringTimeSeriesLastVOMap.containsKey(key)) {
                    TimeSeriesLastVO timeSeriesLastVO = stringTimeSeriesLastVOMap.get(key);
                    pointQueryVO.setPointValue(timeSeriesLastVO.getValue());
                    pointQueryVO.setLastTime(timeSeriesLastVO.getTime());
                    //数据质量
                    String qty = timeSeriesLastVO.getQty();
                    if (StringUtils.isNotEmpty(qty)) {
                        pointQueryVO.setQty(QtyStatus.getInfo(qty));
                    }else {
                        pointQueryVO.setQty(QtyStatus.DISABLE.getInfo());
                    }
                }
            });
        }
        return listPage;
    }

    private void getAssetIds(PulsePointQueryDTO pointQueryDTO) {
        List<String> assetIdsByAssetTypeId = grassAssetService.getAssetIdsByAssetTypeId(pointQueryDTO.getAssetTypeId());
        pointQueryDTO.setAssetIds(assetIdsByAssetTypeId);
    }

    /**
     * 修改测点信息
     *
     * @param grassPointInfo
     * @return
     */
    @Override
    public Boolean updateByGrassPointInfo(GrassPointInfo grassPointInfo) {
        return baseMapper.updateGrassPointInfoById(grassPointInfo);
    }

    /**
     * 数据曲线
     *
     * @param pointCurveDTO
     * @return
     */
    @Override
    public AjaxResult curve(PulsePointCurveDTO pointCurveDTO) {
        String bucketId = SecurityUtils.getLoginUser().getBucketId();
        if (StringUtils.isEmpty(bucketId)) {
            throw new ServiceException("当前用户没有指定存储空间");
        }
        pointCurveDTO.setBucketId(bucketId);
        JSONObject jsonObject = PulseHttpResponseUtil
                .pretreatmentResultsObject(PulseHttpRequestUtil
                        .sendPostJson(pulseUrlConfig.getGetPointData(), JSON.toJSONString(pointCurveDTO)));
        return AjaxResult.success(jsonObject);
    }

    /**
     * 导入
     *
     * @param newPointList
     * @return
     */
    @Override
    public AjaxResult importPoint(List<GrassPointInfo> newPointList) {
        //根据deviceId,pointId查询
        List<GrassPointInfo> oldPointList = list();
        //检查并转换导入测点的type,和unit
        excelCheck(oldPointList, newPointList);
        //筛选需要修改的数据
        List<GrassPointInfo> updateList = importSameList(oldPointList, newPointList);
        Map<String, GrassPointInfo> grassPointInfoMap = oldPointList.stream().
                collect(Collectors.toMap(grassPointInfo -> grassPointInfo.getDeviceId() + grassPointInfo.getPointId(), Function.identity()));
        if (CollectionUtil.isNotEmpty(updateList)) {
            updateList.forEach(grassPointInfo -> {
                GrassPointInfo pointInfo = grassPointInfoMap.get(grassPointInfo.getDeviceId() + grassPointInfo.getPointId());
                if (ObjectUtil.isNotEmpty(pointInfo)) {
                    grassPointInfo.setId(pointInfo.getId());
                }
            });
            ExecutorTransactionUtil.execute(updateList, grassPointInfos ->
                    pointInfoMapper.updateGrassPointInfoBatchById(grassPointInfos));
        }
        return AjaxResult.success();
    }


    /**
     * 导出
     *
     * @return
     */
    @Override
    public List<PointQueryVO> export(PulsePointQueryDTO pointQueryDTO) {
        getAssetIds(pointQueryDTO);
        return pointInfoMapper.getList(pointQueryDTO);
    }

    @Override
    public List<PointQueryVO> dropDown(PulsePointQueryDTO pointQueryDTO) {
        if (StrUtil.isEmpty(pointQueryDTO.getDeviceId()) && StrUtil.isEmpty(pointQueryDTO.getAssetId())) {
            return new ArrayList<>();
        }
        List<GrassPointInfo> pointInfos = pointInfoMapper.dropDown(pointQueryDTO);
        List<PointQueryVO> pointQueryVOS = BeanUtil.copyToList(pointInfos, PointQueryVO.class);

        pointDisplayNameConvert(pointQueryVOS);
        return pointQueryVOS;
    }

    /**
     * 测点id+测点名称 拼接
     *
     * @param pointInfos
     */
    private void pointDisplayNameConvert(List<PointQueryVO> pointInfos) {
        pointInfos.forEach(pointInfo -> {
            //如果测点名称为空，拼接结果为测点id
            //如果测点名称不为空，拼接结果为测点id+测点名称
            String pointDisplayName = pointInfo.getPointId();
            if (StrUtil.isNotEmpty(pointInfo.getPointName())) {
                pointDisplayName = pointInfo.getPointId() + "(" + pointInfo.getPointName() + ")";
            }
            pointInfo.setDisplayName(pointDisplayName);
        });
    }

    /**
     * 根据设备id查询数量
     *
     * @param
     * @return
     */
    @Override
    public Integer selectCount() {
        return pointInfoMapper.selectCount();
    }

    @Override
    public List<GrassPointInfo> selectByDeviceIdAndPointId(List<GrassPointInfo> grassPointInfos) {

        return pointInfoMapper.selectByDeviceIdAndPointId(grassPointInfos);
    }

    @Override
    public List<GrassPointInfo> selectByTag(GrassPointTagInfo grassPointTagInfo) {
        return pointInfoMapper.selectByTag(grassPointTagInfo);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

    @Override
    public NumberCountVO pointStatisticalByTenantId() {
        List<GrassPointInfo> list = this.list();

        List<PointQueryVO> pointQueryList = pointQtyQueryToPulse(BeanUtil.copyToList(list, PointQueryVO.class));
        NumberCountVO numberCountVO = new NumberCountVO();
        numberCountVO.setTotal(pointQueryList.size());

        AtomicInteger onlineCount = new AtomicInteger();
        AtomicInteger offlineCount = new AtomicInteger();
        pointQueryList.forEach(a -> {
            if (a.getQty().equals(QtyStatus.OK.getInfo())) {
                onlineCount.getAndIncrement();
            } else {
                offlineCount.getAndIncrement();
            }
        });
        numberCountVO.setOnlineNumber(onlineCount.get());
        numberCountVO.setOfflineNumber(offlineCount.get());
        return numberCountVO;
    }
    /**
     * 检查并转换导入测点的type,和unit
     *
     * @param oldArrayList
     * @param newArrayList
     */
    private void excelCheck(List<GrassPointInfo> oldArrayList, List<GrassPointInfo> newArrayList) {
        //需要删除的数据
        findDiff(newArrayList, oldArrayList);
        //检查单位和类型，并替换
        Map<String, String> pointTypeDictMap = dictTypeService.selectDictMapByType(POINT_TYPE_DICT, null);
        Map<String, String> pointUnitDictMap = dictTypeService.selectDictMapByType(POINT_UNIT_DICT, null);
        for (int i = 0; i < newArrayList.size(); i++) {
            GrassPointInfo grassPointInfo = newArrayList.get(i);
            String pointType = pointTypeDictMap.get(grassPointInfo.getPointType());
            String pointUnit = pointUnitDictMap.get(grassPointInfo.getPointUnit());
            if (StringUtils.isEmpty(pointType)) {
                throw new ServiceException("第" + (i + 1) + "行请检查是否存在测点类型");
            }
            if (StringUtils.isEmpty(pointUnit)) {
                throw new ServiceException("第" + (i + 1) + "行请检查是否存在测点单位");
            }
            grassPointInfo.setPointType(pointType);
            grassPointInfo.setPointUnit(pointUnit);
        }
    }
}
