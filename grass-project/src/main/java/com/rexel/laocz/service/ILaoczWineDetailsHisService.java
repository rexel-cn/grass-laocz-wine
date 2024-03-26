package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWineDetailsHis;

import java.util.List;

/**
 * 酒操作业务详情历史Service接口
 *
 * @author grass-service
 * @date 2024-03-26
 */
public interface ILaoczWineDetailsHisService extends IService<LaoczWineDetailsHis> {

    /**
     * 查询酒操作业务详情历史列表
     *
     * @param laoczWineDetailsHis 酒操作业务详情历史
     * @return 酒操作业务详情历史集合
     */
    List<LaoczWineDetailsHis> selectLaoczWineDetailsHisList(LaoczWineDetailsHis laoczWineDetailsHis);

}
