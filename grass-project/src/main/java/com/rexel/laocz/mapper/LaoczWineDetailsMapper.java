package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.dto.WineHistoryDTO;
import com.rexel.laocz.domain.vo.MatterDetailVO;
import com.rexel.laocz.domain.vo.WineDetailPointVO;
import com.rexel.laocz.domain.vo.WineDetailVO;
import liquibase.pro.packaged.S;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 流转过程Mapper接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Repository
public interface LaoczWineDetailsMapper extends BaseMapper<LaoczWineDetails> {
    /**
     * 查询流转过程列表
     *
     * @param laoczWineDetails 流转过程
     * @return 流转过程集合
     */
    List<LaoczWineDetails> selectLaoczWineDetailsList(LaoczWineDetails laoczWineDetails);

    /**
     * 批量新增流转过程
     *
     * @param laoczWineDetailsList 流转过程列表
     * @return 结果
     */
    int batchLaoczWineDetails(List<LaoczWineDetails> laoczWineDetailsList);


    /**
     * 查询事项详情列表
     *
     * @param busyId 业务id
     * @return 事项详情列表
     */
    List<MatterDetailVO> selectMatterDetailVOList(@Param("busyId") String busyId, @Param("areaId") Long areaId, @Param("fireZoneId") Long fireZoneId);


    /**
     * 查询酒业务详情
     *
     * @param wineDetailsId 酒业务id
     * @return 酒业务详情
     */
    WineDetailVO selectWineDetailVOById(Long wineDetailsId);

    /**
     * 根据业务详情id查询对应的称重罐的相关测点
     *
     * @param wineDetailsId 业务详情id
     * @return 称重罐及泵的相关测点
     */
    List<WineDetailPointVO> selectWineDetailWeighingTankPointVOList(Long wineDetailsId);

    /**
     * 根据业务详情id查询对应的称重罐及泵的相关测点
     *
     * @param wineDetailsId 业务详情id
     * @return 称重罐及泵的相关测点
     */
    List<WineDetailPointVO> selectWineDetailPumpPointVOList(Long wineDetailsId);


    /**
     * 根据酒详情id查询酒详情相关的数据
     * @param wineDetailsId 酒详情id
     * @return  酒详情相关的数据
     */
    WineHistoryDTO selectWineHistoryDTOList(Long wineDetailsId);


    /**
     * 根据业务id查询酒详情相关的数据
     * @param busyId 业务id
     * @return 酒详情相关的数据
     */
    List<WineHistoryDTO> selectWineHistoryListByBusyId(String busyId);
}
