package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczLiquorManagement;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒品管理Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczLiquorManagementMapper extends BaseMapper<LaoczLiquorManagement> {
    /**
     * 查询酒品管理列表
     *
     * @param laoczLiquorManagement 酒品管理
     * @return 酒品管理集合
     */
    List<LaoczLiquorManagement> selectLaoczLiquorManagementList(LaoczLiquorManagement laoczLiquorManagement);

    /**
     * 批量新增酒品管理
     *
     * @param laoczLiquorManagementList 酒品管理列表
     * @return 结果
     */
    int batchLaoczLiquorManagement(List<LaoczLiquorManagement> laoczLiquorManagementList);

}
