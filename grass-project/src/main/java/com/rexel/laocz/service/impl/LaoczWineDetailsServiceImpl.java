package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import com.rexel.laocz.service.ILaoczWineDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流转过程Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Service
public class LaoczWineDetailsServiceImpl extends ServiceImpl<LaoczWineDetailsMapper, LaoczWineDetails> implements ILaoczWineDetailsService {


    /**
     * 查询流转过程列表
     *
     * @param laoczWineDetails 流转过程
     * @return 流转过程
     */
    @Override
    public List<LaoczWineDetails> selectLaoczWineDetailsList(LaoczWineDetails laoczWineDetails) {
        return baseMapper.selectLaoczWineDetailsList(laoczWineDetails);
    }

}
