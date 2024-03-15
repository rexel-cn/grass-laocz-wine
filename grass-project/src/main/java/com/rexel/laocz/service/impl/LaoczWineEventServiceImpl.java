package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczWineEvent;
import com.rexel.laocz.mapper.LaoczWineEventMapper;
import com.rexel.laocz.service.ILaoczWineEventService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作酒事件Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Service
public class LaoczWineEventServiceImpl extends ServiceImpl<LaoczWineEventMapper, LaoczWineEvent> implements ILaoczWineEventService {


    /**
     * 查询操作酒事件列表
     *
     * @param laoczWineEvent 操作酒事件
     * @return 操作酒事件
     */
    @Override
    public List<LaoczWineEvent> selectLaoczWineEventList(LaoczWineEvent laoczWineEvent) {
        return baseMapper.selectLaoczWineEventList(laoczWineEvent);
    }

}
