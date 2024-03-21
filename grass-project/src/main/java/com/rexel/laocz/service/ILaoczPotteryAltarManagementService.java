package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.vo.CurrentWineIndustryVO;
import com.rexel.laocz.domain.vo.PotteryAltarInformationVO;
import com.rexel.laocz.domain.vo.PotteryAltarVo;
import com.rexel.laocz.domain.vo.PotteryPullDownFrameVO;

import java.text.ParseException;
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

    /**
     * 查询陶坛下拉框
     *
     * @param fireZoneId 防火区ID
     * @return
     */
    List<PotteryPullDownFrameVO> selectPotteryPullDownFrameList(Long fireZoneId);

    /**
     * 获取陶坛信息
     *
     * @param potteryAltarId 主键ID
     * @return
     */
    PotteryAltarInformationVO selectPotteryAltarInformation(Long potteryAltarId);

    /**
     * 获取当前陶坛酒液
     *
     * @param potteryAltarId 主键ID
     * @return
     */
    CurrentWineIndustryVO selectCurrentWineIndustry(Long potteryAltarId) throws ParseException;

    List<PotteryAltarVo> selectLaoczPotteryAltarManagementListDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    PotteryAltarVo selectLaoczPotteryAltarManagement(Long potteryAltarId);

    boolean addPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    boolean updateByIdWithPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement);
}
