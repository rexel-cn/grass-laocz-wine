package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczWineOperations;
import com.rexel.laocz.domain.dto.WineOperationDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒操作业务Mapper接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Repository
public interface LaoczWineOperationsMapper extends BaseMapper<LaoczWineOperations> {
    /**
     * 查询酒操作业务列表
     *
     * @param laoczWineOperations 酒操作业务
     * @return 酒操作业务集合
     */
    List<LaoczWineOperations> selectLaoczWineOperationsList(WineOperationDTO laoczWineOperations);

    /**
     * 批量新增酒操作业务
     *
     * @param laoczWineOperationsList 酒操作业务列表
     * @return 结果
     */
    int batchLaoczWineOperations(List<LaoczWineOperations> laoczWineOperationsList);

}
