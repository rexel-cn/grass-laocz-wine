package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;

import java.util.List;

/**
 * 陶坛与批次实时关系Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczBatchPotteryMappingService extends IService<LaoczBatchPotteryMapping> {

    /**
     * 查询陶坛与批次实时关系列表
     *
     * @param laoczBatchPotteryMapping 陶坛与批次实时关系
     * @return 陶坛与批次实时关系集合
     */
    List<LaoczBatchPotteryMapping> selectLaoczBatchPotteryMappingList(LaoczBatchPotteryMapping laoczBatchPotteryMapping);

}
