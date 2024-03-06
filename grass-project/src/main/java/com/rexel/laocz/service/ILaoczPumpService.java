package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczPump;

import java.util.List;

/**
 * 泵管理Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczPumpService extends IService<LaoczPump> {

    /**
     * 查询泵管理列表
     *
     * @param laoczPump 泵管理
     * @return 泵管理集合
     */
    List<LaoczPump> selectLaoczPumpList(LaoczPump laoczPump);

}
