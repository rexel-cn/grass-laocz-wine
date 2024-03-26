package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWineOperationsHis;

import java.util.List;

/**
 * 酒操作业务历史Service接口
 *
 * @author grass-service
 * @date 2024-03-26
 */
public interface ILaoczWineOperationsHisService extends IService<LaoczWineOperationsHis> {

    /**
     * 查询酒操作业务历史列表
     *
     * @param laoczWineOperationsHis 酒操作业务历史
     * @return 酒操作业务历史集合
     */
    List<LaoczWineOperationsHis> selectLaoczWineOperationsHisList(LaoczWineOperationsHis laoczWineOperationsHis);

}
