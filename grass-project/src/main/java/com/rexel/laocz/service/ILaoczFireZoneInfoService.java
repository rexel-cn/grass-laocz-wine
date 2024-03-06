package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczFireZoneInfo;

import java.util.List;

/**
 * 防火区信息Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczFireZoneInfoService extends IService<LaoczFireZoneInfo> {

    /**
     * 查询防火区信息列表
     *
     * @param laoczFireZoneInfo 防火区信息
     * @return 防火区信息集合
     */
    List<LaoczFireZoneInfo> selectLaoczFireZoneInfoList(LaoczFireZoneInfo laoczFireZoneInfo);

}
