package com.rexel.common.enums;

/**
 * 发送内容模板
 *
 * @author donghai
 * @date 2020/12/15
 */
public enum SendContentTemplateEnum {
    /**
     * 设备告警内容模板
     */
    DEVICE_RULES_ALARM_CONTENT_TEMPLATE(1, "尊敬的{0}您好！{1}测点：于时间{2}触发告警，瞬时值为{3}，请及时处理。"),
    /**
     * 设备告警内容模板可选
     */
    DEVICE_RULES_ALARM_CONTENT_TEMPLATE_CHOOSE(6, "{5}内不再进行重复告警,"),
    /**
     * 设备预警通知模板
     */
    EARLY_WARNING_CONTENT_TEMPLATE(5, "尊敬的{0},设备触发预警,请及时处理.预警规则:{1},处置建议:{2}");

    private final Integer code;
    private final String info;

    SendContentTemplateEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
