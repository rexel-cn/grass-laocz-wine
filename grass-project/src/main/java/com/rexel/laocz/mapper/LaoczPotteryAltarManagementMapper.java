package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.dto.WineEntryPotteryAltarDTO;
import com.rexel.laocz.domain.vo.CurrentWineIndustryVO;
import com.rexel.laocz.domain.vo.OverviewVo;
import com.rexel.laocz.domain.vo.PotteryAltarInformationVO;
import com.rexel.laocz.domain.vo.PotteryAltarVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 陶坛管理Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczPotteryAltarManagementMapper extends BaseMapper<LaoczPotteryAltarManagement> {
    /**
     * 查询陶坛管理列表
     *
     * @param laoczPotteryAltarManagement 陶坛管理
     * @return 陶坛管理集合
     */
    List<LaoczPotteryAltarManagement> selectLaoczPotteryAltarManagementList(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    /**
     * 批量新增陶坛管理
     *
     * @param laoczPotteryAltarManagementList 陶坛管理列表
     * @return 结果
     */
    int batchLaoczPotteryAltarManagement(List<LaoczPotteryAltarManagement> laoczPotteryAltarManagementList);

    /**
     * 获取陶坛信息
     *
     * @param potteryAltarId 主键ID
     * @return
     */
    PotteryAltarInformationVO setPotteryAltarInformation(Long potteryAltarId);

    CurrentWineIndustryVO setCurrentWineIndustry(Long potteryAltarId);

    List<PotteryAltarVo> selectWineEntryPotteryAltarList(WineEntryPotteryAltarDTO wineEntryPotteryAltarDTO);

    List<PotteryAltarVo> selectLaoczPotteryAltarManagementListDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement);
}
