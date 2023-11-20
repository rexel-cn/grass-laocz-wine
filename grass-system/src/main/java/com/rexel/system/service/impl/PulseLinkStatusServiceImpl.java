package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.PulseLinkStatus;
import com.rexel.system.mapper.PulseLinkStatusMapper;
import com.rexel.system.service.IPulseLinkStatusService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备状态Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Service
public class PulseLinkStatusServiceImpl extends ServiceImpl<PulseLinkStatusMapper, PulseLinkStatus> implements IPulseLinkStatusService {


    /**
     * 查询设备状态列表
     *
     * @param pulseLinkStatus 设备状态
     * @return 设备状态
     */
    @Override
    public List<PulseLinkStatus> selectPulseLinkStatusList(PulseLinkStatus pulseLinkStatus) {
        return baseMapper.selectPulseLinkStatusList(pulseLinkStatus);
    }

    /**
     * 根据 连接id删除连接记录
     *
     * @param linkId
     * @return
     */
    @Override
    public Integer deleteByLinkId(String linkId) {
        return baseMapper.deleteByLinkId(linkId);
    }

}
