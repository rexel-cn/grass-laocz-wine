package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczAreaInfo;

import java.util.List;

/**
 * 场区信息Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczAreaInfoService extends IService<LaoczAreaInfo> {

    /**
     * 查询场区信息列表
     *
     * @param laoczAreaInfo 场区信息
     * @return 场区信息集合
     */
    List<LaoczAreaInfo> selectLaoczAreaInfoList(LaoczAreaInfo laoczAreaInfo);

}
