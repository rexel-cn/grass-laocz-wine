package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.vo.BatchInfoVo;
import com.rexel.laocz.domain.vo.BoardDataVO;
import com.rexel.laocz.domain.vo.LaoczBatchPotteryMappingVO;
import com.rexel.laocz.domain.vo.OverviewVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 陶坛与批次实时关系Mapper接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Repository
public interface LaoczBatchPotteryMappingMapper extends BaseMapper<LaoczBatchPotteryMapping> {
    /**
     * 查询陶坛与批次实时关系列表
     *
     * @param laoczBatchPotteryMapping 陶坛与批次实时关系
     * @return 陶坛与批次实时关系集合
     */
    List<LaoczBatchPotteryMapping> selectLaoczBatchPotteryMappingList(LaoczBatchPotteryMapping laoczBatchPotteryMapping);

    /**
     * 批量新增陶坛与批次实时关系
     *
     * @param laoczBatchPotteryMappingList 陶坛与批次实时关系列表
     * @return 结果
     */
    int batchLaoczBatchPotteryMapping(List<LaoczBatchPotteryMapping> laoczBatchPotteryMappingList);

    List<LaoczBatchPotteryMappingVO> selectTableDataInfoReportActual(@Param("fireZoneId") Long fireZoneId,
                                                                     @Param("liquorBatchId") String liquorBatchId,
                                                                     @Param("potteryAltarNumber") String potteryAltarNumber,
                                                                     @Param("liquorName") String liquorName,
                                                                     @Param("areaId") Long areaId);

    /**
     * 看板场区概览
     *
     * @param areaId     场区编号
     * @param fireZoneId 防火区编号
     * @return
     */
    List<BoardDataVO> selectBoardData(@Param("areaId") Long areaId, @Param("fireZoneId") Long fireZoneId);

    /**
     * 移动端场区概览
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 详情数据
     */
    OverviewVo selectOverviewVo(String potteryAltarNumber);

    List<BatchInfoVo> selectBatchInfo(String liquorBatchId);

    /**
     * 查询实际重量总和
     *
     * @param areaId        场区编号
     * @param fireZoneId    防火区编号
     * @param liquorBatchId 批次ID
     * @return 实际重量总和
     */
    Double selectActualWeightSum(@Param("areaId") Long areaId,
                                 @Param("fireZoneId") Long fireZoneId,
                                 @Param("liquorBatchId") String liquorBatchId);
}
