package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczSamplingHistority;
import com.rexel.laocz.domain.LaoczSamplingHistorityVO;
import com.rexel.laocz.domain.vo.LaoczSamplingVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 取样记录
 * Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczSamplingHistorityMapper extends BaseMapper<LaoczSamplingHistority> {
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
     * 批量新增取样记录
     *
     * @param laoczSamplingHistorityList 取样记录
     *                                   列表
     * @return 结果
     */
    int batchLaoczSamplingHistority(List<LaoczSamplingHistority> laoczSamplingHistorityList);

    /**
     * 查询取样
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @return
     */
    List<LaoczSamplingHistorityVO> selectLaoczSamplingHist(@Param("potteryAltarId") Long potteryAltarId,
                                                           @Param("fromTime") String fromTime,
                                                           @Param("endTime") String endTime,
                                                           @Param("liquorBatchId") String liquorBatchId);

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
    List<LaoczSamplingVO> selectLaoczSamplingList(@Param("fromTime") String fromTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("areaId") Long areaId,
                                                  @Param("fireZoneId") Long fireZoneId,
                                                  @Param("potteryAltarNumber") String potteryAltarNumber,
                                                  @Param("liquorBatchId") String liquorBatchId);
}
