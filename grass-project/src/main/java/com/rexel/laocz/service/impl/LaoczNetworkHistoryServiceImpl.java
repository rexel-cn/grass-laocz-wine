package com.rexel.laocz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczNetworkHistory;
import com.rexel.laocz.domain.dto.LaoczNetworkHistoryDTO;
import com.rexel.laocz.mapper.LaoczNetworkHistoryMapper;
import com.rexel.laocz.service.ILaoczNetworkHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 老村长网络设备报警Service业务层处理
 *
 * @author grass-service
 * @date 2024-06-13
 */
@Service
public class LaoczNetworkHistoryServiceImpl extends ServiceImpl<LaoczNetworkHistoryMapper, LaoczNetworkHistory> implements ILaoczNetworkHistoryService {


    /**
     * 查询老村长网络设备报警列表
     *
     * @param laoczNetworkHistory 老村长网络设备报警
     * @return 老村长网络设备报警
     */
    @Override
    public List<LaoczNetworkHistory> selectLaoczNetworkHistoryList(LaoczNetworkHistoryDTO laoczNetworkHistory) {
        return baseMapper.selectLaoczNetworkHistoryList(laoczNetworkHistory);
    }

    /**
     * 新增老村长网络设备报警
     *
     * @param laoczNetworkHistory 老村长网络设备报警
     * @return 结果
     */
    @Override
    public Boolean insertLaoczNetworkHistory(LaoczNetworkHistory laoczNetworkHistory) {
        if (StrUtil.isEmpty(laoczNetworkHistory.getTenantId())) {
            log.error("新增老村长网络设备报警失败,租户ID为空");
            return false;
        }
        return baseMapper.insertLaoczNetworkHistory(laoczNetworkHistory) > 0;
    }

}
