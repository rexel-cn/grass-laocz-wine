package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczSamplingHistority;

import java.util.List;

/**
 * 取样记录
 * Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczSamplingHistorityService extends IService<LaoczSamplingHistority> {

    /**
     * 查询取样记录
     * 列表
     *
     * @param laoczSamplingHistority 取样记录
     * @return 取样记录
     * 集合
     */
    List<LaoczSamplingHistority> selectLaoczSamplingHistorityList(LaoczSamplingHistority laoczSamplingHistority);

}
