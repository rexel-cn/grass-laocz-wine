package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczLiquorManagement;
import com.rexel.laocz.domain.vo.LiquorVo;

import java.util.List;

/**
 * 酒品管理Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczLiquorManagementService extends IService<LaoczLiquorManagement> {

    /**
     * 查询酒品管理列表
     *
     * @param laoczLiquorManagement 酒品管理
     * @return 酒品管理集合
     */
    List<LaoczLiquorManagement> selectLaoczLiquorManagementList(LaoczLiquorManagement laoczLiquorManagement);

    boolean importPoint(List<LiquorVo> liquorVos);
}
