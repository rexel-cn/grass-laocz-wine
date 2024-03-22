package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczPump;
import com.rexel.laocz.domain.vo.LaoczPumpVo;
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
    public List<LaoczPumpVo> selectLaoczPumpList(LaoczPump laoczPump) {
        List<LaoczPumpVo> laoczPumps = baseMapper.selectLaoczPumpList(laoczPump);
        return laoczPumps;
    }

    @Override
    public LaoczPumpVo getPumpDetail(Long pumpId) {
        List<LaoczPumpVo> laoczPumpVos = baseMapper.selectPumpDetails(pumpId);
        LaoczPumpVo laoczPumpVo = new LaoczPumpVo();
        BeanUtil.copyProperties(laoczPumpVos.get(0),laoczPumpVo);


        return laoczPumpVo;
    }

}
