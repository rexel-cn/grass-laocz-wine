package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczNetwork;
import com.rexel.laocz.domain.vo.NetWorkVO;

import java.util.List;


/**
 * 老村长网络设备Service接口
 *
 * @author grass-service
 * @date 2024-06-11
 */
public interface ILaoczNetworkService extends IService<LaoczNetwork> {
    /**
     * 查询网络设备列表
     *
     * @return 网络设备
     */
    List<NetWorkVO> selectNetWorkList(String networkName, String ipAddr);

    /**
     * 新增网络设备
     *
     * @param network 网络设备
     * @return 结果
     */
    Boolean addNetWork(LaoczNetwork network);

    /**
     * 修改网络设备
     *
     * @param network 网络设备
     * @return 结果
     */
    Boolean updateNetWork(LaoczNetwork network);

    /**
     * 删除网络设备
     *
     * @param id
     * @return
     */
    Boolean deleteNetWork(Long id);

    /**
     * 判断网络是否可达
     *
     * @param ipAddr ip地址
     * @return 结果
     */
    Boolean netWorkIsReachable(String ipAddr);
}
