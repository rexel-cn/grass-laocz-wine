package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.vo.OneLineChartVO;
import com.rexel.system.domain.SysTenantDetail;
import com.rexel.system.domain.vo.FrontNumberCountVO;
import com.rexel.system.domain.vo.tenant.UserAssetVO;

import java.util.List;

/**
 * 租户(公司)详情Service接口
 *
 * @author grass-service
 * @date 2023-03-01
 */
public interface ISysTenantDetailService extends IService<SysTenantDetail> {

    /**
     * 查询租户(公司)详情列表
     *
     * @param sysTenantDetail 租户(公司)详情
     * @return 租户(公司)详情集合
     */
    List<SysTenantDetail> selectSysTenantDetailList(SysTenantDetail sysTenantDetail);

    /**
     * 获取详情
     * @return
     */
    SysTenantDetail getDetail();

    /**
     * 用户与设备统计
     * @return
     */
    UserAssetVO getUserAssetInfo();

    /**
     * 物联设备与测点情况统计
     *
     * @return
     */
    FrontNumberCountVO getDevicePointInfo();

    /**
     * 报警统计图表
     *
     * @param type
     * @return
     */
    OneLineChartVO getAlarmChart(int type);

    /**
     * 更新详情 更新公司介绍，公司文本标题
     *
     * @param sysTenantDetail
     * @return
     */
    Boolean updateDetail(SysTenantDetail sysTenantDetail);

    /**
     * 更新详情  更新公司介绍图片
     *
     * @param sysTenantDetail
     * @return
     */
    Boolean updateIntroduce(SysTenantDetail sysTenantDetail);
}
