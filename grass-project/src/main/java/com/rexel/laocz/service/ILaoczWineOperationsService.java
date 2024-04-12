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
}
