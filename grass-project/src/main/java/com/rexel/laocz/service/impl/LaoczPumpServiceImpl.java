package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczPump;
import com.rexel.laocz.mapper.LaoczPumpMapper;
import com.rexel.laocz.service.ILaoczPumpService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 泵管理Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczPumpServiceImpl extends ServiceImpl<LaoczPumpMapper, LaoczPump> implements ILaoczPumpService {


    /**
     * 查询泵管理列表
     *
     * @param laoczPump 泵管理
     * @return 泵管理
     */
    @Override
    public List<LaoczPump> selectLaoczPumpList(LaoczPump laoczPump) {
        return baseMapper.selectLaoczPumpList(laoczPump);
    }

}
