package com.rexel.laocz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.utils.spring.SpringUtils;
import com.rexel.laocz.constant.LaoczCommonConstants;
import com.rexel.laocz.domain.LaoczNetwork;
import com.rexel.laocz.domain.LaoczNetworkHistory;
import com.rexel.laocz.domain.vo.NetWorkVO;
import com.rexel.laocz.enums.NetWorkEnum;
import com.rexel.laocz.mapper.LaoczNetworkMapper;
import com.rexel.laocz.service.ILaoczNetworkHistoryService;
import com.rexel.laocz.service.ILaoczNetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 老村长网络设备Service业务层处理
 *
 * @author grass-service
 * @date 2024-06-11
 */
@Service
@Slf4j
public class LaoczNetworkServiceImpl extends ServiceImpl<LaoczNetworkMapper, LaoczNetwork> implements ILaoczNetworkService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private ILaoczNetworkHistoryService iLaoczNetworkHistoryService;

    /**
     * 查询网络设备列表
     *
     * @param networkName 网络设备名 模糊查询
     * @param ipAddr      ip地址 模糊查询
     * @return 网络设备
     */
    @Override

    public List<NetWorkVO> selectNetWorkList(String networkName, String ipAddr) {
        List<LaoczNetwork> laoczNetworks = SpringUtils.getBean(getClass()).selectLaoczNetworkList(networkName, ipAddr);
        return laoczNetworks.stream().map(network -> {
            NetWorkVO netWorkVO = new NetWorkVO();
            netWorkVO.setId(network.getId());
            netWorkVO.setNetworkName(network.getNetworkName());
            netWorkVO.setIpAddr(network.getIpAddr());
            Boolean reachable = redisCache.getCacheObject(LaoczCommonConstants.LAOCZ_NETWORK + network.getIpAddr());
            netWorkVO.setConnectStatus(NetWorkEnum.getMessage(reachable));
            return netWorkVO;
        }).collect(Collectors.toList());
    }

    /**
     * 查询网络设备列表 带缓存
     *
     * @param networkName 网络设备名 模糊查询
     * @param ipAddr      ip地址 模糊查询
     * @return 网络设备
     */
    @Cacheable(value = LaoczCommonConstants.LAOCZ_NETWORK_DATA, key = "#networkName+'_'+#ipAddr")
    public List<LaoczNetwork> selectLaoczNetworkList(String networkName, String ipAddr) {
        LaoczNetwork laoczNetwork = new LaoczNetwork();
        laoczNetwork.setNetworkName(networkName);
        laoczNetwork.setIpAddr(ipAddr);
        return baseMapper.selectLaoczNetworkList(laoczNetwork);
    }

    /**
     * 新增网络设备
     *
     * @param network 网络设备
     * @return 结果
     */
    @Override
    @CacheEvict(value = LaoczCommonConstants.LAOCZ_NETWORK_DATA, allEntries = true)
    public Boolean addNetWork(LaoczNetwork network) {
        return save(network);
    }

    /**
     * 修改网络设备
     *
     * @param network 网络设备
     * @return 结果
     */
    @Override
    @CacheEvict(value = LaoczCommonConstants.LAOCZ_NETWORK_DATA, allEntries = true)
    public Boolean updateNetWork(LaoczNetwork network) {
        return updateById(network);
    }

    /**
     * 删除网络设备
     *
     * @param id
     * @return
     */
    @Override
    @CacheEvict(value = LaoczCommonConstants.LAOCZ_NETWORK_DATA, allEntries = true)
    public Boolean deleteNetWork(Long id) {
        return removeById(id);
    }

    /**
     * 查询所有网络设备
     *
     * @return 网络设备
     */
    @Cacheable(value = LaoczCommonConstants.LAOCZ_NETWORK_DATA, key = "#root.methodName")
    public List<LaoczNetwork> selectAllLaoczNetwork() {
        return baseMapper.selectAllLaoczNetwork();
    }


    /**
     * 刷新网络设备状态缓存 10s一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void refreshstatusCache() {
        List<LaoczNetwork> laoczNetworks = SpringUtils.getBean(getClass()).selectAllLaoczNetwork();
        for (LaoczNetwork laoczNetwork : laoczNetworks) {
            threadPoolTaskExecutor.execute(() -> {
                String ipAddr = laoczNetwork.getIpAddr();
                boolean reachable = netWorkIsReachable(ipAddr);
                String redisKey = LaoczCommonConstants.LAOCZ_NETWORK + ipAddr;
                Boolean cacheObject = redisCache.getCacheObject(redisKey);
                redisCache.setCacheObject(LaoczCommonConstants.LAOCZ_NETWORK + ipAddr, reachable, 60, TimeUnit.SECONDS);

                //如果有缓存且缓存状态与当前不一致，并且还是不可达，那么保存历史记录，或者缓存为空，当前不可达
                if ((cacheObject != null && (cacheObject != reachable && !reachable)) || (cacheObject == null && !reachable)) {
                    saveHistory(laoczNetwork);
                }
            });
        }
    }

    /**
     * 检查网络设备是否可达
     *
     * @param ipAddr ip地址
     */
    public Boolean netWorkIsReachable(String ipAddr) {
        if (StrUtil.isEmpty(ipAddr)) {
            throw new IllegalArgumentException("网络设备ip地址不能为空");
        }
        boolean reachable = false;
        try {
            InetAddress address = InetAddress.getByName(ipAddr);
            reachable = address.isReachable(3000);
        } catch (IOException e) {
            log.error("网络设备尝试连接失败，ip:{},异常:{}", ipAddr, e.getMessage());
        }
        return reachable;
    }

    /**
     * 保存历史记录
     *
     * @param network 网络设备
     */
    private void saveHistory(LaoczNetwork network) {
        LaoczNetworkHistory laoczNetworkHistory = new LaoczNetworkHistory();
        laoczNetworkHistory.setTenantId(network.getTenantId());
        laoczNetworkHistory.setNetworkName(network.getNetworkName());
        laoczNetworkHistory.setIpAddr(network.getIpAddr());
        laoczNetworkHistory.setContent("设备名称：" + network.getNetworkName() + "，ip地址：" + network.getIpAddr() + "，连接超时");
        iLaoczNetworkHistoryService.insertLaoczNetworkHistory(laoczNetworkHistory);
    }
}
