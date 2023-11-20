package com.rexel.earlywarning.common.enums;

/**
 * MqttEnums
 *
 * @author chunhui.qu
 * @date 2022-3-2
 */
public class MqttEnums {
    /**
     * 事件类型
     */
    public enum USAGE {
        // 提交任务
        SUBMIT_JOB_SCC("/down/submitJob/scc"),
        // 提交任务
        SUBMIT_JOB_ACC("/down/submitJob/acc"),
        // 删除任务
        DELETE_JOB("/down/deleteJob"),
        // 任务详情
        JOB_DETAIL("/up/jobDetail"),
        // 规则通知
        JOB_NOTICE("/up/notice"),
        // 报警数据
        ALARM_DATA("/up/alarmData");

        private final String value;
        public String getValue() {
            return value;
        }
        USAGE(String value) {
            this.value = value;
        }
    }
}
