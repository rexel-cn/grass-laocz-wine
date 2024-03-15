package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWineDetails;

import java.util.List;

/**
 * 流转过程Service接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
public interface ILaoczWineDetailsService extends IService<LaoczWineDetails> {

    /**
     * 查询流转过程列表
     *
     * @param laoczWineDetails 流转过程
     * @return 流转过程集合
     */
    List<LaoczWineDetails> selectLaoczWineDetailsList(LaoczWineDetails laoczWineDetails);

}
