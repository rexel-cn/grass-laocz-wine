package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczLiquorBatch;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒液批次相关信息Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczLiquorBatchMapper extends BaseMapper<LaoczLiquorBatch> {
    /**
     * 查询酒液批次相关信息列表
     *
     * @param laoczLiquorBatch 酒液批次相关信息
     * @return 酒液批次相关信息集合
     */
    List<LaoczLiquorBatch> selectLaoczLiquorBatchList(LaoczLiquorBatch laoczLiquorBatch);

    /**
     * 批量新增酒液批次相关信息
     *
     * @param laoczLiquorBatchList 酒液批次相关信息列表
     * @return 结果
     */
    int batchLaoczLiquorBatch(List<LaoczLiquorBatch> laoczLiquorBatchList);

    /**
     * 出酒时，酒液批次下拉框，只显示有酒并且是存储的批次
     *
     * @return 酒液批次相关信息
     */
    List<LaoczLiquorBatch> wineOutLaoczLiquorBatchList();
}
