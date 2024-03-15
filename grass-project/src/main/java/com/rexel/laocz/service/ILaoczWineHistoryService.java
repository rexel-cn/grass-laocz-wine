package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWineHistory;

import java.util.List;

/**
 * 酒历史Service接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
public interface ILaoczWineHistoryService extends IService<LaoczWineHistory> {

    /**
     * 查询酒历史列表
     *
     * @param laoczWineHistory 酒历史
     * @return 酒历史集合
     */
    List<LaoczWineHistory> selectLaoczWineHistoryList(LaoczWineHistory laoczWineHistory);

}
