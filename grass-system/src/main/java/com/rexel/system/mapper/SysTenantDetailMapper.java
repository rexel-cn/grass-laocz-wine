package com.rexel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.SysTenantDetail;
import com.rexel.system.domain.vo.tenant.UserAssetVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 租户(公司)详情Mapper接口
 *
 * @author grass-service
 * @date 2023-03-01
 */
@Repository
public interface SysTenantDetailMapper extends BaseMapper<SysTenantDetail> {
    /**
     * 查询租户(公司)详情列表
     *
     * @param sysTenantDetail 租户(公司)详情
     * @return 租户(公司)详情集合
     */
    List<SysTenantDetail> selectSysTenantDetailList(SysTenantDetail sysTenantDetail);

    /**
     * 批量新增租户(公司)详情
     *
     * @param sysTenantDetailList 租户(公司)详情列表
     * @return 结果
     */
    int batchSysTenantDetail(List<SysTenantDetail> sysTenantDetailList);

    UserAssetVO getUserAssetInfo(String tenantId);

}
