package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczSamplingHistority;
import com.rexel.laocz.domain.LaoczSamplingHistorityVO;
import com.rexel.laocz.mapper.LaoczSamplingHistorityMapper;
import com.rexel.laocz.service.ILaoczSamplingHistorityService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 取样记录
 * Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczSamplingHistorityServiceImpl extends ServiceImpl<LaoczSamplingHistorityMapper, LaoczSamplingHistority> implements ILaoczSamplingHistorityService {


    /**
     * 查询取样记录
     * 列表
     *
     * @param laoczSamplingHistority 取样记录
     * @return 取样记录
     */
    @Override
    public List<LaoczSamplingHistority> selectLaoczSamplingHistorityList(LaoczSamplingHistority laoczSamplingHistority) {
        return baseMapper.selectLaoczSamplingHistorityList(laoczSamplingHistority);
    }

    /**
     * 查询取样
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @return
     */
    @Override
    public List<LaoczSamplingHistorityVO> selectLaoczSamplingHist(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId) {
        return baseMapper.selectLaoczSamplingHist(potteryAltarId, fromTime, endTime, liquorBatchId);
    }

}
