package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczOutLiquorHistory;

import java.util.List;

/**
 * 出酒记录Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczOutLiquorHistoryService extends IService<LaoczOutLiquorHistory> {

    /**
     * 查询出酒记录列表
     *
     * @param laoczOutLiquorHistory 出酒记录
     * @return 出酒记录集合
     */
    List<LaoczOutLiquorHistory> selectLaoczOutLiquorHistoryList(LaoczOutLiquorHistory laoczOutLiquorHistory);

}
