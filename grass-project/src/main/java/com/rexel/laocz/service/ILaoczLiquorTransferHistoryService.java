package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczLiquorTransferHistory;

import java.util.List;

/**
 * 倒坛记录Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczLiquorTransferHistoryService extends IService<LaoczLiquorTransferHistory> {

    /**
     * 查询倒坛记录列表
     *
     * @param laoczLiquorTransferHistory 倒坛记录
     * @return 倒坛记录集合
     */
    List<LaoczLiquorTransferHistory> selectLaoczLiquorTransferHistoryList(LaoczLiquorTransferHistory laoczLiquorTransferHistory);

}
