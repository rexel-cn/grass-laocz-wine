package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.dto.WineEntryPotteryAltarDTO;
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
     * 查询陶坛管理列表详细信息
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
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
    /**
     * 编辑回显,通过Id查询陶坛管理详情
     */
    PotteryAltarVo selectLaoczPotteryAltarManagement(Long potteryAltarId);
    /**
     * 新增陶坛
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    boolean addPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement);
    /**
     * 修改陶坛
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    boolean updateByIdWithPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement);


    /**
     * 入酒时，陶坛列表过滤查询
     *
     * @param wineEntryPotteryAltarDTO 入酒，陶坛筛选DTO 防火区筛选，陶坛编号筛选，已选陶坛过滤去除
     * @return 陶坛列表
     */
    List<PotteryAltarVo> wineEntryPotteryAltarList(WineEntryPotteryAltarDTO wineEntryPotteryAltarDTO);


}
