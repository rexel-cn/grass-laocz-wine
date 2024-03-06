package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 陶坛与批次实时关系Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczBatchPotteryMappingMapper extends BaseMapper<LaoczBatchPotteryMapping> {
    /**
     * 查询陶坛与批次实时关系列表
     *
     * @param laoczBatchPotteryMapping 陶坛与批次实时关系
     * @return 陶坛与批次实时关系集合
     */
    List<LaoczBatchPotteryMapping> selectLaoczBatchPotteryMappingList(LaoczBatchPotteryMapping laoczBatchPotteryMapping);

    /**
     * 批量新增陶坛与批次实时关系
     *
     * @param laoczBatchPotteryMappingList 陶坛与批次实时关系列表
     * @return 结果
     */
    int batchLaoczBatchPotteryMapping(List<LaoczBatchPotteryMapping> laoczBatchPotteryMappingList);

}
