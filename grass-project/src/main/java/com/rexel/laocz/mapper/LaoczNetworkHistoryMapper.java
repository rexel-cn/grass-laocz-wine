package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczNetworkHistory;
import com.rexel.laocz.domain.dto.LaoczNetworkHistoryDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 老村长网络设备报警Mapper接口
 *
 * @author grass-service
 * @date 2024-06-13
 */
@Repository
public interface LaoczNetworkHistoryMapper extends BaseMapper<LaoczNetworkHistory> {
    /**
     * 查询老村长网络设备报警列表
     *
     * @param laoczNetworkHistory 老村长网络设备报警
     * @return 老村长网络设备报警集合
     */
    List<LaoczNetworkHistory> selectLaoczNetworkHistoryList(LaoczNetworkHistoryDTO laoczNetworkHistory);

    /**
     * 批量新增老村长网络设备报警
     *
     * @param laoczNetworkHistoryList 老村长网络设备报警列表
     * @return 结果
     */
    int batchLaoczNetworkHistory(List<LaoczNetworkHistory> laoczNetworkHistoryList);

    /**
     * 新增老村长网络设备报警 忽略租户
     *
     * @param laoczNetworkHistory 老村长网络设备报警
     * @return 结果
     */
    @InterceptorIgnore(tenantLine = "true")
    int insertLaoczNetworkHistory(LaoczNetworkHistory laoczNetworkHistory);
}
