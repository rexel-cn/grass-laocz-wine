package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.vo.PotteryAltarVo;

import java.util.List;

/**
 * 陶坛管理Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczPotteryAltarManagementService extends IService<LaoczPotteryAltarManagement> {

    /**
     * 查询陶坛管理列表
     *
     * @param laoczPotteryAltarManagement 陶坛管理
     * @return 陶坛管理集合
     */
    List<LaoczPotteryAltarManagement> selectLaoczPotteryAltarManagementList(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    List<PotteryAltarVo> selectLaoczPotteryAltarManagementListDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    PotteryAltarVo selectLaoczPotteryAltarManagement(Long potteryAltarId);

    boolean addPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    boolean updateByIdWithPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement);
}
