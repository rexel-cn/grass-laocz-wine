package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczNetworkHistory;
import com.rexel.laocz.domain.dto.LaoczNetworkHistoryDTO;

import java.util.List;

/**
 * 老村长网络设备报警Service接口
 *
 * @author grass-service
 * @date 2024-06-13
 */
public interface ILaoczNetworkHistoryService extends IService<LaoczNetworkHistory> {

    /**
     * 查询老村长网络设备报警列表
     *
     * @param laoczNetworkHistory 老村长网络设备报警
     * @return 老村长网络设备报警集合
     */
    List<LaoczNetworkHistory> selectLaoczNetworkHistoryList(LaoczNetworkHistoryDTO laoczNetworkHistory);

    /**
     * 新增老村长网络设备报警 忽略租户
     *
     * @param laoczNetworkHistory 老村长网络设备报警
     * @return 结果
     */
    Boolean insertLaoczNetworkHistory(LaoczNetworkHistory laoczNetworkHistory);


}
