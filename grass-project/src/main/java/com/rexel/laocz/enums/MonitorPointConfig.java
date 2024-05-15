package com.rexel.laocz.enums;

import lombok.Getter;

@Getter
public enum MonitorPointConfig {
    /**
     * DJ_SEX_TTK_B1_ES 陶坛库1号泵急停（1为急停状态）
     * DJ_SEX_TTK_B1_FAULT 陶坛库1号泵故障（1为故障状态）
     * DJ_SEX_TTK_B1_RE 陶坛库1号泵远程（1为远程状态）
     * DJ_SEX_TTK_B1_RUN 陶坛库1号泵正运行（0为停止状态）
     * FM_SRX_TTK_FM1_CL_FAULT 陶坛库阀门1关故障（1为故障状态）
     * FM_SRX_TTK_FM1_OP_FAULT 陶坛库阀门1开故障（1为故障状态）
     * SEX_TTK_1_1_ZL_OUT 陶坛库1楼1号秤（确保不超过上限）
     */
    ES("2", "es", CheckType.EQUALS, "1", "急停"),
    FAULT("2", "fault", CheckType.EQUALS, "1", "故障"),
    RE("2", "re", CheckType.EQUALS, "0", "不是远程状态"),
    RUN("2", "run", CheckType.EQUALS, "1", "运行状态"),
    CL_FAULT("1", "cl_fault", CheckType.EQUALS, "1", "关故障"),
    OP_FAULT("1", "op_fault", CheckType.EQUALS, "1", "开故障"),
    ZL_OUT("1", "zl_out", CheckType.GREATER_THAN, null, "秤超上限");

    /**
     * 1.称重罐，2泵
     */
    private final String type;
    private final String useMark;
    private final CheckType check;
    private final String value;
    private final String exception;

    MonitorPointConfig(String type, String useMark, CheckType check, String value, String exception) {
        this.type = type;
        this.useMark = useMark;
        this.check = check;
        this.value = value;
        this.exception = exception;
    }
}
