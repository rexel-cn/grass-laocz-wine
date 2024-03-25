package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczSamplingHistority;
import com.rexel.laocz.domain.LaoczSamplingHistorityVO;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.mapper.LaoczSamplingHistorityMapper;
import com.rexel.laocz.service.ILaoczSamplingHistorityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 取样管理
     *
     * @param fromTime           开始时间
     * @param endTime            结束时间
     * @param areaId             场区编号
     * @param fireZoneId         防火区编号
     * @param potteryAltarNumber 陶坛编号
     * @param liquorBatchId      酒品批次
     * @return
     */
    @Override
    public List<LaoczSamplingVO> selectLaoczSamplingList(String fromTime, String endTime, Long areaId, Long fireZoneId, String potteryAltarNumber, String liquorBatchId) {
        return baseMapper.selectLaoczSamplingList(fromTime, endTime, areaId, fireZoneId, potteryAltarNumber, liquorBatchId).stream()
                .peek(aaa -> aaa.setState(aaa.getSamplingFile().isEmpty() ? 0 : 1))
                .collect(Collectors.toList());
    }

    /**
     * 上传文件
     *
     * @param samplingHistorityId 取样历史数据
     * @param url                 链接
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateLaoczSampling(Long samplingHistorityId, String url) {
        LaoczSamplingHistority laoczSamplingHistority = new LaoczSamplingHistority();
        laoczSamplingHistority.setSamplingFile(url);
        return this.lambdaUpdate().eq(LaoczSamplingHistority::getSamplingHistorityId, samplingHistorityId).update(laoczSamplingHistority);
    }

    /**
     * 取样管理详情
     *
     * @param samplingHistorityId 取样历史数据主键
     * @return
     */
    @Override
    public LaoczWineHistoryInfoVO getLaoczSamplingHistoryInfo(Long samplingHistorityId) {
        try {
            PotteryAltarInfomationDInfoVO potteryAltarInfomationDInfoVO = baseMapper.getLaoczSamplingHistoryInfo(samplingHistorityId);
            LaoczWineHistoryInfoVO laoczWineHistoryInfoVO = new LaoczWineHistoryInfoVO();
            laoczWineHistoryInfoVO.setWorkOrderId(potteryAltarInfomationDInfoVO.getWorkOrderId());
            laoczWineHistoryInfoVO.setCurrentWineIndustryVO(BeanUtil.copyProperties(potteryAltarInfomationDInfoVO, CurrentWineIndustryInfoVO.class));
            laoczWineHistoryInfoVO.setPotteryAltarInformationInfoVO(BeanUtil.copyProperties(potteryAltarInfomationDInfoVO, PotteryAltarInformationInfoVO.class));
            return laoczWineHistoryInfoVO;
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new SecurityException("查询失败");
        }
    }
}
