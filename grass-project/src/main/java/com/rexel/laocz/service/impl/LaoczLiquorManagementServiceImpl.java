package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczLiquorManagement;
import com.rexel.laocz.mapper.LaoczLiquorManagementMapper;
import com.rexel.laocz.service.ILaoczLiquorManagementService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 酒品管理Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorManagementServiceImpl extends ServiceImpl<LaoczLiquorManagementMapper, LaoczLiquorManagement> implements ILaoczLiquorManagementService {


    /**
     * 查询酒品管理列表
     *
     * @param laoczLiquorManagement 酒品管理
     * @return 酒品管理
     */
    @Override
    public List<LaoczLiquorManagement> selectLaoczLiquorManagementList(LaoczLiquorManagement laoczLiquorManagement) {
        return baseMapper.selectLaoczLiquorManagementList(laoczLiquorManagement);
    }

}
