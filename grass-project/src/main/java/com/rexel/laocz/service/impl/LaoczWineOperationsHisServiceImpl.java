package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczWineOperationsHis;
import com.rexel.laocz.mapper.LaoczWineOperationsHisMapper;
import com.rexel.laocz.service.ILaoczWineOperationsHisService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 酒操作业务历史Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-26
 */
@Service
public class LaoczWineOperationsHisServiceImpl extends ServiceImpl<LaoczWineOperationsHisMapper, LaoczWineOperationsHis> implements ILaoczWineOperationsHisService {


    /**
     * 查询酒操作业务历史列表
     *
     * @param laoczWineOperationsHis 酒操作业务历史
     * @return 酒操作业务历史
     */
    @Override
    public List<LaoczWineOperationsHis> selectLaoczWineOperationsHisList(LaoczWineOperationsHis laoczWineOperationsHis) {
        return baseMapper.selectLaoczWineOperationsHisList(laoczWineOperationsHis);
    }

}
