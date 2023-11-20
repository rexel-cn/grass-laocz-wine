package com.rexel.earlywarning.common.constants;

/**
 * EarlyConstants
 *
 * @author chunhui.qu
 * @date 2022-3-8
 */
public class EarlyConstants {
    public static final String JUDGE_TYPE_ACC = "0";
    public static final String JUDGE_TYPE_SCC = "1";

    public static final String[] LIMITS = {"&", "|", "(", ")"};
    public static final String PREFIX = "^[a-zA-z].*";
    public static final String AND = " && ";
    public static final String UNDERLINE = "_";
    public static final String SPACE = " ";

    public static final String TIME = "time";
    public static final String EVENT = "event";
    public static final String DATA = "data";

    public static final String SUBMIT_JOB_ID = "JobID";
    public static final String SUBMIT_TRIGGERS = "Triggers";
    public static final String SUBMIT_TID = "Tid";
    public static final String SUBMIT_POINT = "Point";
    public static final String SUBMIT_VALUE = "Value";
    public static final String SUBMIT_JUDGE_FLAG = "JudgeFlag";
    public static final String SUBMIT_TRIGGER_CMD = "TriggerCmd";
    public static final String SUBMIT_TRIGGER_A_F = "TriggerAcquisitionFrequency";
    public static final String SUBMIT_EXECUTIONS = "Executions";
    public static final String SUBMIT_A_F = "AcquisitionFrequency";
    public static final String SUBMIT_INCREMENT = "Increment";
    public static final String SUBMIT_JUDGES = "Judges";
    public static final String SUBMIT_TASK_COMMAND = "TaskCommand";
    public static final String SUBMIT_SINGLE_C_TIME = "SingleContinuousTime";
    public static final String SUBMIT_TASK_EE_TIME = "TaskEffectiveTime";

    public static final String DELETE_JOB_ID = "jobId";

    public static final String J_STATUS_ID_START = "start";
    public static final String J_STATUS_NAME_START = "运行";
    public static final String J_STATUS_NAME_STOP = "停止";

    public static final String REDIS_PREFIX = "device:run:early:";

    public static final Long RULE_STATE_CLOSE = 0L;
    public static final Long RULE_STATE_OPEN = 1L;

    public static final String HEADER_TOPIC = "mqtt_receivedTopic";

    public static final String EVENT_NOTICE = "notice";
    public static final String EVENT_JOB_DETAIL = "jobDetail";
}
