package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczPump;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 泵管理Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczPumpMapper extends BaseMapper<LaoczPump> {
    /**
     * 查询泵管理列表
     *
     * @param laoczPump 泵管理
     * @return 泵管理集合
     */
    List<LaoczPump> selectLaoczPumpList(LaoczPump laoczPump);

    /**
     * 批量新增泵管理
     *
     * @param laoczPumpList 泵管理列表
     * @return 结果
     */
    int batchLaoczPump(List<LaoczPump> laoczPumpList);

}
