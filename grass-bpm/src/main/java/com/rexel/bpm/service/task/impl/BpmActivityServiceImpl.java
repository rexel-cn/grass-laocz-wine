package com.rexel.bpm.service.task.impl;

import com.rexel.bpm.convert.task.BpmActivityConvert;
import com.rexel.bpm.service.task.BpmActivityService;
import com.rexel.bpm.domain.task.activity.BpmActivityRespVO;
import com.rexel.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * BPM 活动实例 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
@Validated
public class BpmActivityServiceImpl implements BpmActivityService {

    @Resource
    private HistoryService historyService;

    @Override
    public List<BpmActivityRespVO> getActivityListByProcessInstanceId(String processInstanceId) {
        List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();

        return BpmActivityConvert.INSTANCE.convertList(activityList);
    }
    public BpmActivityRespVO convert(HistoricActivityInstance bean) {
        if ( bean == null ) {
            return null;
        }

        BpmActivityRespVO bpmActivityRespVO = new BpmActivityRespVO();

        bpmActivityRespVO.setKey( bean.getActivityId() );
        bpmActivityRespVO.setType( bean.getActivityType() );
        if(bean.getStartTime()!=null){
            bpmActivityRespVO.setStartTime(  bean.getStartTime().getTime());
        }

        if(bean.getEndTime()!=null){
            bpmActivityRespVO.setEndTime( bean.getEndTime() .getTime() );
        }

        bpmActivityRespVO.setTaskId( bean.getTaskId() );

        return bpmActivityRespVO;
    }

    @Override
    public List<HistoricActivityInstance> getHistoricActivityListByExecutionId(String executionId) {
        return historyService.createHistoricActivityInstanceQuery().executionId(executionId).list();
    }

}
