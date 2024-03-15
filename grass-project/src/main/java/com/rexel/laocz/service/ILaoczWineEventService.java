package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWineEvent;

import java.util.List;

/**
 * 操作酒事件Service接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
public interface ILaoczWineEventService extends IService<LaoczWineEvent> {

    /**
     * 查询操作酒事件列表
     *
     * @param laoczWineEvent 操作酒事件
     * @return 操作酒事件集合
     */
    List<LaoczWineEvent> selectLaoczWineEventList(LaoczWineEvent laoczWineEvent);

}
