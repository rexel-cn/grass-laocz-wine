package com.rexel.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.system.domain.GrassReduceInfo;
import com.rexel.system.domain.vo.*;

/**
 * 预聚合信息Service接口
 *
 * @author grass-service
 * @date 2023-04-27
 */
public interface IGrassReduceInfoService extends IService<GrassReduceInfo> {
    /**
     * 查询预聚合信息列表
     *
     * @param grassReduceInfo 预聚合信息
     * @return 预聚合信息集合
     */
    List<GrassReduceInfo> selectGrassReduceInfoList(GrassReduceInfo grassReduceInfo);

    /**
     * 更新预聚合信息
     *
     * @param list list
     */
    void updateGrassReduceInfo(List<GrassReduceInfo> list);

    /**
     * 人工执行预聚合
     *
     * @param grassReduceExecuteVO grassReduceExecuteVO
     */
    AjaxResult doManualExecuteReduce(GrassReduceExecuteVO grassReduceExecuteVO);

    /**
     * 人工执行预聚合
     *
     * @param grassReduceExecuteOneVO grassReduceExecuteOneVO
     */
    AjaxResult doManualExecuteOneReduce(GrassReduceExecuteOneVO grassReduceExecuteOneVO);

    /**
     * 删除预聚合数据
     *
     * @param grassReduceDeleteVO grassReduceDeleteVO
     * @return AjaxResult
     */
    AjaxResult doDeleteReduceData(GrassReduceDeleteVO grassReduceDeleteVO);

    /**
     * 删除预聚合数据
     *
     * @param grassReduceDeleteOneVO grassReduceDeleteOneVO
     * @return AjaxResult
     */
    AjaxResult doDeleteReduceDataOne(GrassReduceDeleteOneVO grassReduceDeleteOneVO);

    /**
     * 删除预聚合数据
     *
     * @return AjaxResult
     */
    AjaxResult doDeleteReduceDataAll();

    /**
     * 查询预聚合结果状态
     *
     * @param grassReduceQueryPlanVO grassReduceQueryPlanVO
     * @return AjaxResult
     */
    AjaxResult queryPlanStatusList(GrassReduceQueryPlanVO grassReduceQueryPlanVO);
}
