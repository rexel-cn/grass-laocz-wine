package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczPump;
import com.rexel.laocz.domain.dto.PumpAddDto;
import com.rexel.laocz.domain.dto.PumpImportDto;
import com.rexel.laocz.domain.vo.LaoczPumpPointInfo;
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

    /**
     * 导入泵管理数据
     * @param pumpImportDtos 泵管理数据
     * @return 布尔
     */
    boolean importPump(List<PumpImportDto> pumpImportDtos);

    /**
     * 根据测点主键查询泵测点信息
     *
     * @param pointPrimaryKeys 测点主键
     * @return 泵测点信息
     */
    List<LaoczPumpPointInfo> selectLaoczPumpPointInfoByPointPrimaryKeys(List<Long> pointPrimaryKeys);
}
