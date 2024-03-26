package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczWeighingTank;
import com.rexel.laocz.domain.vo.PointInfo;
import com.rexel.laocz.domain.vo.WeighingTankVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 称重罐管理Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczWeighingTankMapper extends BaseMapper<LaoczWeighingTank> {
    /**
     * 查询称重罐管理列表
     *
     * @param laoczWeighingTank 称重罐管理
     * @return 称重罐管理集合
     */
    List<LaoczWeighingTank> selectLaoczWeighingTankList(LaoczWeighingTank laoczWeighingTank);

    /**
     * 批量新增称重罐管理
     *
     * @param laoczWeighingTankList 称重罐管理列表
     * @return 结果
     */
    int batchLaoczWeighingTank(List<LaoczWeighingTank> laoczWeighingTankList);

    List<PointInfo> getPointInfo();

    List<WeighingTankVo> selectLaoczWeighingTankListDetail(LaoczWeighingTank laoczWeighingTank);
}
