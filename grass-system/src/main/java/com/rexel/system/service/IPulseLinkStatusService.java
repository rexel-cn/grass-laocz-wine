package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.PulseLinkStatus;

import java.util.List;

/**
 * 设备状态Service接口
 *
 * @author grass-service
 * @date 2022-08-16
 */
public interface IPulseLinkStatusService extends IService<PulseLinkStatus> {

    /**
     * 查询设备状态列表
     *
     * @param pulseLinkStatus 设备状态
     * @return 设备状态集合
     */
    List<PulseLinkStatus> selectPulseLinkStatusList(PulseLinkStatus pulseLinkStatus);

    /**
     * 根据 连接id删除连接记录
     *
     * @param linkId
     * @return
     */
    Integer deleteByLinkId(String linkId);
}
