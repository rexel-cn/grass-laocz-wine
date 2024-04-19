package com.rexl.bpm.framework.constant;

/**
 * @ClassName ErrorCodeConstants
 * @Description
 * @Author 孟开通
 * @Date 2024/4/18 11:48
 **/
public class ErrorCodeConstants {
    public static final String MODEL_DEPLOY_FAIL_TASK_CANDIDATE_NOT_CONFIG="部署流程失败，" +
            "原因：用户任务({})未配置审批人，请点击【流程设计】按钮，选择该它的【任务（审批人）】进行配置";

    public static final String TASK_CREATE_FAIL_NO_CANDIDATE_USER="操作失败，原因：找不到任务的审批人！";
}
