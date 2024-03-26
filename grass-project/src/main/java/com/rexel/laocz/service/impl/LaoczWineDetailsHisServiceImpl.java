package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczWineDetailsHis;
import com.rexel.laocz.mapper.LaoczWineDetailsHisMapper;
import com.rexel.laocz.service.ILaoczWineDetailsHisService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 酒操作业务详情历史Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-26
 */
@Service
public class LaoczWineDetailsHisServiceImpl extends ServiceImpl<LaoczWineDetailsHisMapper, LaoczWineDetailsHis> implements ILaoczWineDetailsHisService {


    /**
     * 查询酒操作业务详情历史列表
     *
     * @param laoczWineDetailsHis 酒操作业务详情历史
     * @return 酒操作业务详情历史
     */
    @Override
    public List<LaoczWineDetailsHis> selectLaoczWineDetailsHisList(LaoczWineDetailsHis laoczWineDetailsHis) {
        return baseMapper.selectLaoczWineDetailsHisList(laoczWineDetailsHis);
    }

}
