package com.rexel.laocz.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.rexel.bpm.framework.flowable.core.enums.BpmConstants;
import com.rexel.bpm.framework.flowable.core.util.FlowableUtils;
import com.rexel.common.core.page.PageDomain;
import com.rexel.common.core.page.TableSupport;
import com.rexel.common.utils.PageResult;
import com.rexel.common.utils.PageUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.laocz.bpm.service.LaoczBpmProcessInstanceService;
import com.rexel.laocz.domain.dto.ProcessInstancePageDTO;
import com.rexel.laocz.domain.vo.ProcessInstancePageVO;
import com.rexel.laocz.enums.OperationTypeEnum;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rexel.laocz.constant.BpmWineVariablesConstants.*;

/**
 * @ClassName LaoczBpmProcessInstanceServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2024/5/10 13:51
 **/
@Service
public class LaoczBpmProcessInstanceServiceImpl implements LaoczBpmProcessInstanceService {

    @Resource
    private HistoryService historyService;

    @Override
    public PageResult<ProcessInstancePageVO> getProcessInstancePage(ProcessInstancePageDTO processInstancePageDTO) {
        HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .includeProcessVariables()
                .startedBy(SecurityUtils.getUserId().toString())
                .processInstanceTenantId(FlowableUtils.getTenantId())
                .orderByProcessInstanceStartTime().desc();

        if (ObjectUtil.isNotNull(processInstancePageDTO.getBeginTime()) && ObjectUtil.isNotNull(processInstancePageDTO.getEndTime())) {
            processInstanceQuery.startedAfter(processInstancePageDTO.getBeginTime());
            processInstanceQuery.startedBefore(processInstancePageDTO.getEndTime());
        }

        if (StrUtil.isNotEmpty(processInstancePageDTO.getOperationType())) {
            processInstanceQuery.variableValueLike(OPERATION_TYPE, processInstancePageDTO.getOperationType());
        }
        if (StrUtil.isNotEmpty(processInstancePageDTO.getLiquorBatchId())) {
            processInstanceQuery.variableValueLike(LIQUOR_BATCH_ID, "%" + processInstancePageDTO.getLiquorBatchId() + "%");
        }


        // 查询数量
        long processInstanceCount = processInstanceQuery.count();
        if (processInstanceCount == 0) {
            return PageResult.empty();
        }
        List<ProcessInstancePageVO> processInstancePageVOS = new ArrayList<>();
        // 查询列表
        PageDomain pageDomain = TableSupport.getPageDomain();
        List<HistoricProcessInstance> listPage = processInstanceQuery.listPage(PageUtils.getStart(pageDomain), pageDomain.getPageSize());
        if (CollectionUtil.isEmpty(listPage)) {
            return PageResult.empty();
        }
        for (HistoricProcessInstance historicProcessInstance : listPage) {
            ProcessInstancePageVO processInstancePageVO = new ProcessInstancePageVO();
            processInstancePageVO.setProcessInstanceId(historicProcessInstance.getId());
            processInstancePageVO.setApplyTime(historicProcessInstance.getStartTime());

            Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
            if (processVariables.containsKey(OPERATION_TYPE)) {
                processInstancePageVO.setOperationType(OperationTypeEnum.getNameByValue(Long.valueOf(processVariables.get(OPERATION_TYPE).toString())));
            }
            if (processVariables.containsKey(LIQUOR_BATCH_ID)) {
                String liquorBatchId = processVariables.get(LIQUOR_BATCH_ID).toString();
                processInstancePageVO.setLiquorBatchId(liquorBatchId);
            }
            if (processVariables.containsKey(LIQUOR_NAME)) {
                processInstancePageVO.setLiquorName(processVariables.get(LIQUOR_NAME).toString());
            }
            processInstancePageVO.setStatus((Integer) processVariables.get(BpmConstants.PROCESS_INSTANCE_VARIABLE_STATUS));
            processInstancePageVOS.add(processInstancePageVO);
        }
        return new PageResult<>(processInstancePageVOS, processInstanceCount);
    }
}
