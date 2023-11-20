package com.rexel.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.rexel.system.domain.GrassReduceInfo;

/**
 * 预聚合信息Mapper接口
 *
 * @author grass-service
 * @date 2023-04-27
 */
@Repository
public interface GrassReduceInfoMapper extends BaseMapper<GrassReduceInfo> {
    /**
     * 查询预聚合信息列表
     *
     * @param grassReduceInfo 预聚合信息
     * @return 预聚合信息集合
     */
    List<GrassReduceInfo> selectGrassReduceInfoList(GrassReduceInfo grassReduceInfo);

    /**
     * 批量新增预聚合信息
     *
     * @param grassReduceInfoList 预聚合信息列表
     * @return 结果
     */
    int batchGrassReduceInfo(List<GrassReduceInfo> grassReduceInfoList);

    /**
     * 删除预聚合信息
     */
    void deleteAllGrassReduceInfo();
}
