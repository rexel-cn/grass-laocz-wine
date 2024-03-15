package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczWineEvent;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作酒事件Mapper接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Repository
public interface LaoczWineEventMapper extends BaseMapper<LaoczWineEvent> {
    /**
     * 查询操作酒事件列表
     *
     * @param laoczWineEvent 操作酒事件
     * @return 操作酒事件集合
     */
    List<LaoczWineEvent> selectLaoczWineEventList(LaoczWineEvent laoczWineEvent);

    /**
     * 批量新增操作酒事件
     *
     * @param laoczWineEventList 操作酒事件列表
     * @return 结果
     */
    int batchLaoczWineEvent(List<LaoczWineEvent> laoczWineEventList);

}
