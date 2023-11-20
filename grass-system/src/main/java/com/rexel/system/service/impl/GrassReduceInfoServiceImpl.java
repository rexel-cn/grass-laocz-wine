package com.rexel.system.service.impl;

import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.config.PulseUrlConfig;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.pulse.PulseHttpRequestUtil;
import com.rexel.common.utils.pulse.PulseHttpResponseUtil;
import com.rexel.system.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rexel.system.mapper.GrassReduceInfoMapper;
import com.rexel.system.domain.GrassReduceInfo;
import com.rexel.system.service.IGrassReduceInfoService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 预聚合信息Service业务层处理
 *
 * @author grass-service
 * @date 2023-04-27
 */
@Service
public class GrassReduceInfoServiceImpl extends ServiceImpl<GrassReduceInfoMapper, GrassReduceInfo> implements IGrassReduceInfoService {
    @Autowired
    private PulseUrlConfig pulseUrlConfig;

    /**
     * 查询预聚合信息列表
     *
     * @param grassReduceInfo 预聚合信息
     * @return 预聚合信息
     */
    @Override
    public List<GrassReduceInfo> selectGrassReduceInfoList(GrassReduceInfo grassReduceInfo) {
        return baseMapper.selectGrassReduceInfoList(grassReduceInfo);
    }

    /**
     * 更新预聚合信息
     *
     * @param list list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGrassReduceInfo(List<GrassReduceInfo> list) {
        for (GrassReduceInfo info : list) {
            if (StringUtils.isEmpty(info.getInterval())) {
                throw new CustomException("聚合颗粒度不允许为空");
            }
            if ("0".equals(info.getInterval())) {
                throw new CustomException("不支持该聚合颗粒度");
            }
            if (info.getRetentionDays() == 0) {
                throw new CustomException("存储时长必须大于0");
            }
        }
        baseMapper.deleteAllGrassReduceInfo();
        if (list.size() > 0) {
            baseMapper.batchGrassReduceInfo(list);
        }
    }

    /**
     * 人工执行预聚合
     *
     * @param grassReduceExecuteVO grassReduceExecuteVO
     */
    @Override
    public AjaxResult doManualExecuteReduce(GrassReduceExecuteVO grassReduceExecuteVO) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenantId", SecurityUtils.getTenantId());
        paramJson.put("startTime", grassReduceExecuteVO.getStartTime());
        paramJson.put("stopTime", grassReduceExecuteVO.getEndTime());
        paramJson.put("interval", grassReduceExecuteVO.getInterval());

        String url = pulseUrlConfig.getReduceExecute();
        String resp = PulseHttpRequestUtil.sendPostJson(url, paramJson.toString());
        String result = PulseHttpResponseUtil.responseToData(resp);
        return AjaxResult.success(result);
    }

    /**
     * 人工执行预聚合
     *
     * @param grassReduceExecuteOneVO grassReduceExecuteOneVO
     */
    @Override
    public AjaxResult doManualExecuteOneReduce(GrassReduceExecuteOneVO grassReduceExecuteOneVO) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenantId", SecurityUtils.getTenantId());
        paramJson.put("deviceId", grassReduceExecuteOneVO.getDeviceId());
        paramJson.put("dataTime", grassReduceExecuteOneVO.getDataTime());
        paramJson.put("interval", grassReduceExecuteOneVO.getInterval());

        String url = pulseUrlConfig.getReduceExecuteOne();
        String resp = PulseHttpRequestUtil.sendPostJson(url, paramJson.toString());
        String result = PulseHttpResponseUtil.responseToData(resp);
        return AjaxResult.success(result);
    }

    /**
     * 删除预聚合数据
     *
     * @param grassReduceDeleteVO grassReduceDeleteVO
     * @return AjaxResult
     */
    @Override
    public AjaxResult doDeleteReduceData(GrassReduceDeleteVO grassReduceDeleteVO) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenantId", SecurityUtils.getTenantId());
        paramJson.put("startTime", grassReduceDeleteVO.getStartTime());
        paramJson.put("stopTime", grassReduceDeleteVO.getEndTime());
        paramJson.put("interval", grassReduceDeleteVO.getInterval());

        String url = pulseUrlConfig.getReduceDelete();
        String resp = PulseHttpRequestUtil.sendPostJson(url, paramJson.toString());
        String result = PulseHttpResponseUtil.responseToData(resp);
        return AjaxResult.success(result);
    }

    /**
     * 删除预聚合数据
     *
     * @param grassReduceDeleteOneVO grassReduceDeleteOneVO
     * @return AjaxResult
     */
    @Override
    public AjaxResult doDeleteReduceDataOne(GrassReduceDeleteOneVO grassReduceDeleteOneVO) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("resultId", grassReduceDeleteOneVO.getResultId());

        String url = pulseUrlConfig.getReduceDeleteOne();
        String resp = PulseHttpRequestUtil.sendPostJson(url, paramJson.toString());
        String result = PulseHttpResponseUtil.responseToData(resp);
        return AjaxResult.success(result);
    }

    /**
     * 删除预聚合数据
     *
     * @return AjaxResult
     */
    @Override
    public AjaxResult doDeleteReduceDataAll() {
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenantId", SecurityUtils.getTenantId());

        String url = pulseUrlConfig.getReduceDeleteAll();
        String resp = PulseHttpRequestUtil.sendPostJson(url, paramJson.toString());
        String result = PulseHttpResponseUtil.responseToData(resp);
        return AjaxResult.success(result);
    }

    /**
     * 查询预聚合结果状态
     *
     * @param grassReduceQueryPlanVO grassReduceQueryPlanVO
     * @return AjaxResult
     */
    @Override
    public AjaxResult queryPlanStatusList(GrassReduceQueryPlanVO grassReduceQueryPlanVO) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenantId", SecurityUtils.getTenantId());
        paramJson.put("deviceId", grassReduceQueryPlanVO.getDeviceId());
        paramJson.put("startTime", grassReduceQueryPlanVO.getStartTime());
        paramJson.put("stopTime", grassReduceQueryPlanVO.getEndTime());
        paramJson.put("interval", grassReduceQueryPlanVO.getInterval());
        paramJson.put("status", grassReduceQueryPlanVO.getStatus());
        if (grassReduceQueryPlanVO.getLimit() != null && grassReduceQueryPlanVO.getLimit() > 0) {
            paramJson.put("limit", grassReduceQueryPlanVO.getStatus());
        } else {
            paramJson.put("limit", 100);
        }

        String url = pulseUrlConfig.getReducePlanStatus();
        String resp = PulseHttpRequestUtil.sendPostJson(url, paramJson.toString());
        JSONArray result = PulseHttpResponseUtil.pretreatmentResultsArray(resp);
        return AjaxResult.success(result);
    }
}
