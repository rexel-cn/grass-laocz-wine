package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczPump;
import com.rexel.laocz.domain.dto.PumpAddDto;
import com.rexel.laocz.domain.vo.LaoczPumpVo;
import com.rexel.laocz.domain.vo.PointInfo;

import java.util.List;

/**
 * 泵管理Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczPumpService extends IService<LaoczPump> {

    /**
     * 查询泵管理列表
     *
     * @param laoczPump 泵管理
     * @return 泵管理集合
     */
    List<LaoczPumpVo> selectLaoczPumpList(LaoczPump laoczPump);

    PumpAddDto getPumpDetail(Long pumpId);
    /**
     * 新增泵管理
     */
    boolean addPump(PumpAddDto pumpAddDto);

    boolean updateByIdWithPump(PumpAddDto pumpAddDto);

    boolean removeByIdWithPoint(Long pumpId);

    List<PointInfo> getPointInfo(Long pumpId);
}
