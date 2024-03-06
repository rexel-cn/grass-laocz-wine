package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczStoringHistory;

import java.util.List;

/**
 * 入酒记录Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczStoringHistoryService extends IService<LaoczStoringHistory> {

    /**
     * 查询入酒记录列表
     *
     * @param laoczStoringHistory 入酒记录
     * @return 入酒记录集合
     */
    List<LaoczStoringHistory> selectLaoczStoringHistoryList(LaoczStoringHistory laoczStoringHistory);

}
