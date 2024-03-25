package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczSamplingHistority;
import com.rexel.laocz.domain.LaoczSamplingHistorityVO;
import com.rexel.laocz.domain.vo.LaoczSamplingVO;
import com.rexel.laocz.domain.vo.LaoczWineHistoryInfoVO;

import java.util.List;

/**
 * 取样记录
 * Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczSamplingHistorityService extends IService<LaoczSamplingHistority> {

    /**
     * 查询取样记录
     * 列表
     *
     * @param laoczSamplingHistority 取样记录
     * @return 取样记录
     * 集合
     */
    List<LaoczSamplingHistority> selectLaoczSamplingHistorityList(LaoczSamplingHistority laoczSamplingHistority);

    /**
     * 查询取样
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @return
     */
    List<LaoczSamplingHistorityVO> selectLaoczSamplingHist(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId);

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
    List<LaoczSamplingVO> selectLaoczSamplingList(String fromTime,
                                                  String endTime,
                                                  Long areaId,
                                                  Long fireZoneId,
                                                  String potteryAltarNumber,
                                                  String liquorBatchId);

    /**
     * 上传文件
     *
     * @param samplingHistorityId 取样历史数据主键
     * @param url                 链接
     * @return
     */
    Boolean updateLaoczSampling(Long samplingHistorityId, String url);

    /**
     * 取样管理详情
     *
     * @param samplingHistorityId 取样历史数据主键
     * @return
     */
    LaoczWineHistoryInfoVO getLaoczSamplingHistoryInfo(Long samplingHistorityId);
}
