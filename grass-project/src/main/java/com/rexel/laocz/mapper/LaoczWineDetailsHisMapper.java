package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczWineDetailsHis;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒操作业务详情历史Mapper接口
 *
 * @author grass-service
 * @date 2024-03-26
 */
@Repository
public interface LaoczWineDetailsHisMapper extends BaseMapper<LaoczWineDetailsHis> {
    /**
     * 查询酒操作业务详情历史列表
     *
     * @param laoczWineDetailsHis 酒操作业务详情历史
     * @return 酒操作业务详情历史集合
     */
    List<LaoczWineDetailsHis> selectLaoczWineDetailsHisList(LaoczWineDetailsHis laoczWineDetailsHis);

    /**
     * 批量新增酒操作业务详情历史
     *
     * @param laoczWineDetailsHisList 酒操作业务详情历史列表
     * @return 结果
     */
    int batchLaoczWineDetailsHis(List<LaoczWineDetailsHis> laoczWineDetailsHisList);

}
