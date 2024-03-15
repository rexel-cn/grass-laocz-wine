package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.LaoczWineOperations;
import com.rexel.laocz.domain.dto.WineEntryApplyParamDTO;
import com.rexel.laocz.domain.vo.MatterDetailVO;
import com.rexel.laocz.domain.vo.MatterVO;
import com.rexel.laocz.domain.vo.WineDetailVO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import com.rexel.laocz.mapper.LaoczWineOperationsMapper;
import com.rexel.laocz.service.ILaoczWineDetailsService;
import com.rexel.laocz.service.ILaoczWineOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 酒操作业务Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Service
public class LaoczWineOperationsServiceImpl extends ServiceImpl<LaoczWineOperationsMapper, LaoczWineOperations> implements ILaoczWineOperationsService {
    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;
    @Autowired
    private LaoczWineDetailsMapper laoczWineDetailsMapper;

    /**
     * 查询酒操作业务列表
     *
     * @param laoczWineOperations 酒操作业务
     * @return 酒操作业务
     */
    @Override
    public List<LaoczWineOperations> selectLaoczWineOperationsList(LaoczWineOperations laoczWineOperations) {
        return baseMapper.selectLaoczWineOperationsList(laoczWineOperations);
    }

    /**
     * 获取我的事项
     *
     * @return 我的事项列表
     */
    @Override
    public List<MatterVO> getMatterVOList() {
        List<LaoczWineOperations> list = list();
        return list.stream().map(laoczWineOperations -> {
            MatterVO matterVO = new MatterVO();
            matterVO.setWineOperationsId(laoczWineOperations.getWineOperationsId());
            matterVO.setWorkOrderId(laoczWineOperations.getWorkOrderId());
            matterVO.setApplyTime(laoczWineOperations.getCreateTime());
            matterVO.setOperationType(OperationTypeEnum.getNameByValue(laoczWineOperations.getOperationType()));
            return matterVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取我的事项详情
     *
     * @param wineOperationsId 酒操作业务表 主键
     * @return 我的事项详情列表
     */
    @Override
    public List<MatterDetailVO> getMatterDetailVOList(Long wineOperationsId) {
        LaoczWineOperations operations = getById(wineOperationsId);
        String busyId = operations.getBusyId();
        return laoczWineDetailsMapper.selectMatterDetailVOList(busyId);
    }

    /**
     * 获取入酒详情
     *
     * @param wineDetailsId 酒业务操作详情id
     * @return 入酒详情
     */
    @Override
    public WineDetailVO getWineDetailVO(Long wineDetailsId) {
        return laoczWineDetailsMapper.selectWineDetailVOById(wineDetailsId);
    }

    /**
     * 设置称重罐
     *
     * @param weighingTank 称重罐
     */
    @Override
    public void setWeighingTank(WineEntryApplyParamDTO weighingTank) {
        iLaoczWineDetailsService.lambdaUpdate().eq(LaoczWineDetails::getWineDetailsId, weighingTank.getWineDetailsId())
                .set(LaoczWineDetails::getWeighingTank, weighingTank.getWeighingTank()).update();
    }


}
