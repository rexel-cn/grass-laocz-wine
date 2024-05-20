package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.vo.AreaVo;
import com.rexel.laocz.domain.vo.FireZoneInfoVo;

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

    List<AreaVo> dropDown();

    List<LaoczFireZoneInfo> getByIdWithfireZoneName(Long id);

    /**
     * 新增场区
     * @param laoczAreaInfo 场区信息
     * @return 返回
     */
    boolean addLaoczAreaInfo(LaoczAreaInfo laoczAreaInfo);

    /**
     * 修改场区
     *
     * @param laoczAreaInfo 场区信息
     * @return 返回
     */
    Boolean updateLaoczAreaInfo(LaoczAreaInfo laoczAreaInfo);
    /**
     * 联查全部防火区+场区
     */
    List<FireZoneInfoVo> getAreaFire();

    /**
     * 根据id删除
     *
     * @param fireZoneId 防火区id
     * @return 返回
     */
    Boolean deleteLaoczAreaInfoById(Long fireZoneId);
}
