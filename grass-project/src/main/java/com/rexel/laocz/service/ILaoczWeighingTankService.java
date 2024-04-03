package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWeighingTank;
import com.rexel.laocz.domain.dto.WeighingTankAddDto;
import com.rexel.laocz.domain.dto.WeighingTankDto;
import com.rexel.laocz.domain.vo.PointInfo;
import com.rexel.laocz.domain.vo.WeighingTankAddVo;
import com.rexel.laocz.domain.vo.WeighingTankVo;
import com.rexel.system.domain.dto.PulsePointQueryDTO;
import com.rexel.system.domain.vo.PointQueryVO;

import java.util.List;

/**
 * 称重罐管理Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczWeighingTankService extends IService<LaoczWeighingTank> {

    /**
     * 查询称重罐管理列表
     *
     * @param laoczWeighingTank 称重罐管理
     * @return 称重罐管理集合
     */
    List<LaoczWeighingTank> selectLaoczWeighingTankList(LaoczWeighingTank laoczWeighingTank);
    /**
     * 查询称重罐管理列表详细信息
     */
    List<WeighingTankVo> selectLaoczWeighingTankListDetail(LaoczWeighingTank laoczWeighingTank);
    /**
     * 获取称重罐管理详细信息
     */
    WeighingTankAddDto getByIdWithTank(Long weighingTankId);
    /**
     * 修改称重罐管理
     */
    boolean updateByIdWithWeighingTank(WeighingTankAddDto weighingTankAddDto);
    /**
     * 新增称重罐管理
     */
    boolean addWeighingTank(WeighingTankAddDto weighingTankAddDto);
    /**
     * 导入称重罐管理列表
     */
    boolean importWeighingTank(List<WeighingTankDto> WeighingTankDtos);

    List<WeighingTankAddVo> getAddVo(String dictType);

    List<PointInfo> getPointInfo(Long weighingTankId);

    boolean removeByIdWithPoint(Long weighingTankId);

    /**
     * 分页查询过滤已被选择测点
     * @param
     * @return
     */
    List<PointQueryVO> getListPageNoChoice(String deviceId, String pointId,String pointName,String pointPrimaryKey);
}
