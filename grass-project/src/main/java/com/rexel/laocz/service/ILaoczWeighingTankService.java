package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWeighingTank;

import java.util.List;

/**
 * 称重罐管理Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczWeighingTankService extends IService<LaoczWeighingTank> {

    /**
     * 查询称重罐管理列表
     *
     * @param laoczWeighingTank 称重罐管理
     * @return 称重罐管理集合
     */
    List<LaoczWeighingTank> selectLaoczWeighingTankList(LaoczWeighingTank laoczWeighingTank);

}
