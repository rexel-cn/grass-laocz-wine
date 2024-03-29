package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczLiquorBatch;

import java.util.List;

/**
 * 酒液批次相关信息Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczLiquorBatchService extends IService<LaoczLiquorBatch> {

    /**
     * 查询酒液批次相关信息列表
     *
     * @param laoczLiquorBatch 酒液批次相关信息
     * @return 酒液批次相关信息集合
     */
    List<LaoczLiquorBatch> selectLaoczLiquorBatchList(LaoczLiquorBatch laoczLiquorBatch);


    /**
     * 出酒时，酒液批次下拉框，只显示有酒并且是存储的批次
     *
     * @return
     */
    List<LaoczLiquorBatch> wineOutLaoczLiquorBatchList();

}
