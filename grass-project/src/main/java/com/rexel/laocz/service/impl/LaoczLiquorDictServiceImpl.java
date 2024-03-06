package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczLiquorDict;
import com.rexel.laocz.mapper.LaoczLiquorDictMapper;
import com.rexel.laocz.service.ILaoczLiquorDictService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 酒品字典Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorDictServiceImpl extends ServiceImpl<LaoczLiquorDictMapper, LaoczLiquorDict> implements ILaoczLiquorDictService {


    /**
     * 查询酒品字典列表
     *
     * @param laoczLiquorDict 酒品字典
     * @return 酒品字典
     */
    @Override
    public List<LaoczLiquorDict> selectLaoczLiquorDictList(LaoczLiquorDict laoczLiquorDict) {
        return baseMapper.selectLaoczLiquorDictList(laoczLiquorDict);
    }

}
