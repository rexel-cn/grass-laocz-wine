package com.rexel.laocz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.DictUtils;
import com.rexel.laocz.constant.WineDictConstants;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.LaoczPump;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.LaoczWineOperations;
import com.rexel.laocz.domain.dto.WineEntryApplyParamDTO;
import com.rexel.laocz.domain.vo.MatterDetailVO;
import com.rexel.laocz.domain.vo.MatterVO;
import com.rexel.laocz.domain.vo.WineDetailVO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.WineBusyStatusEnum;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import com.rexel.laocz.mapper.LaoczWineOperationsMapper;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import com.rexel.laocz.service.ILaoczPumpService;
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
    @Autowired
    private ILaoczPumpService iLaoczPumpService;
    @Autowired
    private ILaoczFireZoneInfoService iLaoczFireZoneInfoService;

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
            matterVO.setOperationTypeNumber(laoczWineOperations.getOperationType());
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
        WineDetailVO wineDetailVO = laoczWineDetailsMapper.selectWineDetailVOById(wineDetailsId);
        buildSamplingPurpose(wineDetailVO);
        return wineDetailVO;
    }

    /**
     * 取样用途
     *
     * @param wineDetailVO
     */
    private void buildSamplingPurpose(WineDetailVO wineDetailVO) {
        if (wineDetailVO == null) {
            return;
        }
        String samplingPurpose = wineDetailVO.getSamplingPurpose();
        if (StrUtil.isEmpty(samplingPurpose)) {
            return;
        }
        wineDetailVO.setSamplingPurpose(DictUtils.getDictLabel(WineDictConstants.SAMPLING_PURPOSE, samplingPurpose));
    }

    /**
     * 设置称重罐
     *
     * @param weighingTank 称重罐
     */
    @Override
    public Boolean setWeighingTank(WineEntryApplyParamDTO weighingTank) {
        LaoczWineDetails details = iLaoczWineDetailsService.lambdaQuery().eq(LaoczWineDetails::getWineDetailsId, weighingTank.getWineDetailsId()).one();
        if (!WineBusyStatusEnum.NOT_STARTED.getValue().equals(details.getBusyStatus())) {
            throw new CustomException("酒操作已开始，无法设置称重罐");
        }
        LaoczPump one = iLaoczPumpService.lambdaQuery().eq(LaoczPump::getFireZoneId, weighingTank.getFireZoneId()).one();
        if (one == null) {
            LaoczFireZoneInfo byId = iLaoczFireZoneInfoService.getById(weighingTank.getFireZoneId());
            throw new CustomException("请联系管理员在防火区:{}，设置泵信息", byId.getFireZoneName());
        }
        return iLaoczWineDetailsService.lambdaUpdate().eq(LaoczWineDetails::getWineDetailsId, weighingTank.getWineDetailsId())
                .set(LaoczWineDetails::getWeighingTank, weighingTank.getWeighingTank())
                .set(LaoczWineDetails::getPumpId, one.getPumpId())
                .update();
    }


}
