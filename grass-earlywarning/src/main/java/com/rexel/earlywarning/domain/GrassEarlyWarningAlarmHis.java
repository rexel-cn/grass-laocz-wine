package com.rexel.earlywarning.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 预警规则报警历史对象 grass_early_warning_alarm_his
 *
 * @author grass-service
 * @date 2023-09-15
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GrassEarlyWarningAlarmHis {
    private static final long serialVersionUID = 1L;

    /**
     * 报警历史ID
     */
    @TableId(type = IdType.AUTO)
    private Long hisId;
    /**
     * 预警规则ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long rulesId;
    /**
     * 预警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date alarmTime;
    /**
     * 预警规则名称
     */
    private String rulesName;

    /**
     * 建议标题
     */
    private String suggestTitle;

    /**
     * 建议内容
     */
    private String suggestContent;

    /**
     * 预警等级
     */
    private String rulesLevel;

    /**
     * 预警等级名称
     */
    private String rulesLevelName;
}
