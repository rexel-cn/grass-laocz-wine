package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.LaoczLiquorAlarmHistory;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.vo.LaoczLiquorAlarmHistoryVO;
import com.rexel.laocz.mapper.LaoczLiquorAlarmHistoryMapper;
import com.rexel.laocz.service.ILaoczLiquorAlarmHistoryService;
import com.rexel.laocz.service.ILaoczPotteryAltarManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 老村长酒存储时间报警历史Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorAlarmHistoryServiceImpl extends ServiceImpl<LaoczLiquorAlarmHistoryMapper, LaoczLiquorAlarmHistory> implements ILaoczLiquorAlarmHistoryService {

    @Autowired
    private ILaoczPotteryAltarManagementService iLaoczPotteryAltarManagementService;
    /**
     * 查询老村长酒存储时间报警历史列表
     *
     * @param laoczLiquorAlarmHistory 老村长酒存储时间报警历史
     * @return 老村长酒存储时间报警历史
     */
    @Override
    public List<LaoczLiquorAlarmHistory> selectLaoczLiquorAlarmHistoryList(LaoczLiquorAlarmHistory laoczLiquorAlarmHistory) {
        return baseMapper.selectLaoczLiquorAlarmHistoryList(laoczLiquorAlarmHistory);
    }

    /**
     * 查询报警历史信息
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @param liquorRuleName 规则名称
     * @return
     */
    @Override
    public List<LaoczLiquorAlarmHistory> selectLaoczLiquorAlarmHistory(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId, String liquorRuleName) {
        return baseMapper.selectLaoczLiquorAlarmHistory(potteryAltarId, fromTime, endTime, liquorBatchId, liquorRuleName);
    }

    /**
     * 报警查询
     *
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @param liquorRuleName 规则名称
     * @return
     */
    @Override
    public List<LaoczLiquorAlarmHistoryVO> selectLaoczLiquorAlarmHistoryList(String fromTime, String endTime, String liquorBatchId, String liquorRuleName) {
        try {
            return baseMapper.selectLaoczLiquorAlarmHistoryVOList(fromTime, endTime, liquorBatchId, liquorRuleName);
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new ServiceException("查询失败");
        }
    }
    /**
     * 获取报警详情
     *
     * @return
     */
    @Override
    public LaoczLiquorAlarmHistoryVO getByIdWithAlarmInfo(Long liquorAlarmHistoryId) {
         return   baseMapper.selectAlarmInfo(liquorAlarmHistoryId);
    }

}
