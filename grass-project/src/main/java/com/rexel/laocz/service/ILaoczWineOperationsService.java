package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWineOperations;
import com.rexel.laocz.domain.dto.MatterDetailDTO;
import com.rexel.laocz.domain.dto.WineEntryApplyParamDTO;
import com.rexel.laocz.domain.dto.WineOperationDTO;
import com.rexel.laocz.domain.vo.MatterDetailVO;
import com.rexel.laocz.domain.vo.MatterVO;
import com.rexel.laocz.domain.vo.WineDetailVO;

import java.util.List;

/**
 * 酒操作业务Service接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
public interface ILaoczWineOperationsService extends IService<LaoczWineOperations> {
    /**
     * 获取我的事项
     *
     * @return 我的事项列表
     */
    List<MatterVO> getMatterVOList(WineOperationDTO wineOperationDTO);

    /**
     * 获取我的事项详情
     *
     * @param wineOperationsId 酒操作业务表 主键
     * @return 我的事项详情列表
     */
    List<MatterDetailVO> getMatterDetailVOList(MatterDetailDTO wineOperationsId);

    /**
     * 根据事项和陶坛编号获取对应事项详情
     *
     * @param wineOperationsId   事项id
     * @param potteryAltarNumber 陶坛编号
     * @return 事项详情Id
     */
    MatterDetailVO qrCodeScanMatterDetail(Long wineOperationsId, String potteryAltarNumber);


    /**
     * 获取入酒详情
     *
     * @param wineDetailsId 酒业务操作详情id
     * @return 入酒详情
     */
    WineDetailVO getWineDetailVO(Long wineDetailsId);

    /**
     * 设置称重罐
     *
     * @param weighingTank 称重罐
     */
    Boolean setWeighingTank(WineEntryApplyParamDTO weighingTank);

    /**
     * 确认审批失败(审批失败确认后业务处理)
     * @param wineOperationsId 酒操作业务表 主键
     * @return 结果
     */
    Boolean confirmApprovalFailed(Long wineOperationsId);

    /**
     * 确认审批状态
     * @param busy 业务id
     * @param status 状态
     * @return 结果
     */
    Boolean confirmApprovalStatus(String busy, Integer status);
}
