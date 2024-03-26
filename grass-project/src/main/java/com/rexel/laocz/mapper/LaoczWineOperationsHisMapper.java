package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczWineOperationsHis;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒操作业务历史Mapper接口
 *
 * @author grass-service
 * @date 2024-03-26
 */
@Repository
public interface LaoczWineOperationsHisMapper extends BaseMapper<LaoczWineOperationsHis> {
    /**
     * 查询酒操作业务历史列表
     *
     * @param laoczWineOperationsHis 酒操作业务历史
     * @return 酒操作业务历史集合
     */
    List<LaoczWineOperationsHis> selectLaoczWineOperationsHisList(LaoczWineOperationsHis laoczWineOperationsHis);

    /**
     * 批量新增酒操作业务历史
     *
     * @param laoczWineOperationsHisList 酒操作业务历史列表
     * @return 结果
     */
    int batchLaoczWineOperationsHis(List<LaoczWineOperationsHis> laoczWineOperationsHisList);

}
