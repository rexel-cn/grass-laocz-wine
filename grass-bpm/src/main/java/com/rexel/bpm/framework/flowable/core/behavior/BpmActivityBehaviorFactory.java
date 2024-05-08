package com.rexel.bpm.framework.flowable.core.behavior;

import com.rexel.bpm.framework.flowable.core.candidate.BpmTaskCandidateInvoker;
import lombok.Setter;
import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

/**
 * 自定义的 ActivityBehaviorFactory 实现类，目的如下：
 * 1. 自定义 {@link #createUserTaskActivityBehavior(UserTask)}：实现自定义的流程任务的 assignee 负责人的分配
 *
 * @author 芋道源码
 */
@Setter
public class BpmActivityBehaviorFactory extends DefaultActivityBehaviorFactory {

    private BpmTaskCandidateInvoker taskCandidateInvoker;

    @Override
    public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
        BpmUserTaskActivityBehavior bpmUserTaskActivityBehavior = new BpmUserTaskActivityBehavior(userTask);
        bpmUserTaskActivityBehavior.setTaskCandidateInvoker(taskCandidateInvoker);
        return bpmUserTaskActivityBehavior;
    }

    @Override
    public ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior(Activity activity,
                                                                             AbstractBpmnActivityBehavior behavior) {
        BpmParallelMultiInstanceBehavior bpmParallelMultiInstanceBehavior = new BpmParallelMultiInstanceBehavior(activity, behavior);
        bpmParallelMultiInstanceBehavior.setTaskCandidateInvoker(taskCandidateInvoker);
        return bpmParallelMultiInstanceBehavior;

    }

    @Override
    public SequentialMultiInstanceBehavior createSequentialMultiInstanceBehavior(Activity activity,
                                                                                 AbstractBpmnActivityBehavior behavior) {
        BpmSequentialMultiInstanceBehavior bpmSequentialMultiInstanceBehavior = new BpmSequentialMultiInstanceBehavior(activity, behavior);
        bpmSequentialMultiInstanceBehavior.setTaskCandidateInvoker(taskCandidateInvoker);
        return bpmSequentialMultiInstanceBehavior;

    }

}
