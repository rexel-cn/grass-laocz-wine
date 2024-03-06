package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczLiquorTransferHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 倒坛记录Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczLiquorTransferHistoryMapper extends BaseMapper<LaoczLiquorTransferHistory> {
    /**
     * 查询倒坛记录列表
     *
     * @param laoczLiquorTransferHistory 倒坛记录
     * @return 倒坛记录集合
     */
    List<LaoczLiquorTransferHistory> selectLaoczLiquorTransferHistoryList(LaoczLiquorTransferHistory laoczLiquorTransferHistory);

    /**
     * 批量新增倒坛记录
     *
     * @param laoczLiquorTransferHistoryList 倒坛记录列表
     * @return 结果
     */
    int batchLaoczLiquorTransferHistory(List<LaoczLiquorTransferHistory> laoczLiquorTransferHistoryList);

}
