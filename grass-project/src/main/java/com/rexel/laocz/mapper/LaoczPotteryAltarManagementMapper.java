package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
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

}
