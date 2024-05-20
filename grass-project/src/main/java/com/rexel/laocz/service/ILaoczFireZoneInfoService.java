package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.vo.FireZoneInfoVo;
import com.rexel.laocz.domain.vo.TreePullDownVO;

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
    List<FireZoneInfoVo> selectLaoczFireZoneInfoList(LaoczFireZoneInfo laoczFireZoneInfo);

    /**
     * 保存防火区信息
     *
     * @param laoczFireZoneInfo 防火区信息
     * @return 返回
     */
    Boolean saveFireZoneInfo(LaoczFireZoneInfo laoczFireZoneInfo);

    /**
     * 修改防火区信息
     *
     * @param laoczFireZoneInfo 防火区信息
     * @return 返回
     */
    Boolean updateFireZoneInfo(LaoczFireZoneInfo laoczFireZoneInfo);

    /**
     * 树状下拉框
     *
     * @return
     */
    List<TreePullDownVO> selectTreePullDown();

    Long findFireZoneId(String areaName, String fireZoneName);

    /**
     * 删除防火区信息
     *
     * @param fireZoneId 防火区id
     * @return 返回
     */
    Boolean deleteLaoczFireZoneInfoById(Long fireZoneId);
}
