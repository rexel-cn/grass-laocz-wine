package com.rexel.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.vo.OneLineChartVO;
import com.rexel.system.domain.SysTenantDetail;
import com.rexel.system.domain.vo.FrontNumberCountVO;
import com.rexel.system.domain.vo.tenant.AlarmVO;
import com.rexel.system.domain.vo.tenant.UserAssetVO;
import com.rexel.system.mapper.SysTenantDetailMapper;
import com.rexel.system.service.IGrassAlarmHistoryService;
import com.rexel.system.service.IGrassDeviceInfoService;
import com.rexel.system.service.ISysTenantDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 租户(公司)详情Service业务层处理
 *
 * @author grass-service
 * @date 2023-03-01
 */
@Service
public class SysTenantDetailServiceImpl extends ServiceImpl<SysTenantDetailMapper, SysTenantDetail> implements ISysTenantDetailService {

    @Autowired
    private IGrassDeviceInfoService deviceInfoService;
    @Autowired
    private IGrassPointServiceImpl pointService;
    @Autowired
    private IGrassAlarmHistoryService alarmHistoryService;


    /**
     * 查询租户(公司)详情列表
     *
     * @param sysTenantDetail 租户(公司)详情
     * @return 租户(公司)详情
     */
    @Override
    public List<SysTenantDetail> selectSysTenantDetailList(SysTenantDetail sysTenantDetail) {
        return baseMapper.selectSysTenantDetailList(sysTenantDetail);
    }

    @Override
    public SysTenantDetail getDetail() {
        SysTenantDetail one = getOne(null);
        return one == null ? new SysTenantDetail() : one;
    }

    @Override
    public UserAssetVO getUserAssetInfo() {
        return baseMapper.getUserAssetInfo(SecurityUtils.getTenantId());
    }

    @Override
    public FrontNumberCountVO getDevicePointInfo() {
        return FrontNumberCountVO.builder()
                //物联设备查询 统计
                .device(deviceInfoService.deviceStatisticalByTenantId())
                //测点数量查询
                .point(pointService.pointStatisticalByTenantId())
                .build();
    }

    @Override
    public OneLineChartVO getAlarmChart(int type) {
        List<AlarmVO> alarmCountList = alarmHistoryService.selectAlarmCountList(type);
        OneLineChartVO oneLineChartVO = new OneLineChartVO();
        oneLineChartVO.setChartTitle("报警数量统计");
        oneLineChartVO.setUnit("条");
        List<String> yAxis = new ArrayList<>();


        Map<String, Integer> collect = alarmCountList.stream().collect(Collectors.toMap(AlarmVO::getAlarmTime, AlarmVO::getCount));
        List<String> xaisList = initAlarmCountXais(type);
        xaisList.forEach(a -> {
            yAxis.add(collect.containsKey(a) ? collect.get(a).toString() : "0");
        });

        oneLineChartVO.setXAxis(xaisList);
        oneLineChartVO.setYAxis(yAxis);
        return oneLineChartVO;
    }

    /**
     * 更新详情 更新公司介绍，公司文本标题
     *
     * @param sysTenantDetail
     * @return
     */
    @Override
    public Boolean updateDetail(SysTenantDetail sysTenantDetail) {
        return lambdaUpdate()
                .eq(sysTenantDetail.getId() != null, SysTenantDetail::getId, sysTenantDetail.getId())
                .set(SysTenantDetail::getCompanyProfile, sysTenantDetail.getCompanyProfile())
                .set(SysTenantDetail::getTextTitle, sysTenantDetail.getTextTitle()).update();
    }

    /**
     * 更新详情  更新公司介绍图片
     *
     * @param sysTenantDetail
     * @return
     */
    @Override
    public Boolean updateIntroduce(SysTenantDetail sysTenantDetail) {
        //验证url 必须存在
        checkUrl(sysTenantDetail.getIntroduceUrl());
        return lambdaUpdate()
                .eq(sysTenantDetail.getId() != null, SysTenantDetail::getId, sysTenantDetail.getId())
                .set(StrUtil.isNotEmpty(sysTenantDetail.getIntroduceUrl()), SysTenantDetail::getIntroduceUrl, sysTenantDetail.getIntroduceUrl())
                .update();
    }

    private void checkUrl(String introduceUrl) {
        if (StrUtil.isEmpty(introduceUrl)) {
            throw new RuntimeException("请上传图片成功后再保存");
        }
    }

    private List<String> initAlarmCountXais(int type) {
        if (type == 1) {
            return DateUtils.getLast7days(DateUtils.MM_DD);
        } else if (type == 2) {
            return DateUtils.getLast6Months();
        }
        return null;
    }
}
