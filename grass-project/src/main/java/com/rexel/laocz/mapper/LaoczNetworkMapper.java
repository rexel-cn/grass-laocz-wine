package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczNetwork;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 老村长网络设备Mapper接口
 *
 * @author grass-service
 * @date 2024-06-11
 */
@Repository
public interface LaoczNetworkMapper extends BaseMapper<LaoczNetwork> {
    /**
     * 查询老村长网络设备列表
     *
     * @param laoczNetwork 老村长网络设备
     * @return 老村长网络设备集合
     */
    List<LaoczNetwork> selectLaoczNetworkList(LaoczNetwork laoczNetwork);

    /**
     * 批量新增老村长网络设备
     *
     * @param laoczNetworkList 老村长网络设备列表
     * @return 结果
     */
    int batchLaoczNetwork(List<LaoczNetwork> laoczNetworkList);

    /**
     * 查询所有老村长网络设备 忽略租户
     *
     * @return 结果
     */
    @InterceptorIgnore(tenantLine = "true")
    List<LaoczNetwork> selectAllLaoczNetwork();
}
