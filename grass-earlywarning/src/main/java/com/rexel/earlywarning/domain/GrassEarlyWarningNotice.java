package com.rexel.earlywarning.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.core.domain.BaseEntity;

/**
 * 预警规则通知范围对象 grass_early_warning_notice
 *
 * @author grass-service
 * @date 2023-09-15
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GrassEarlyWarningNotice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 关联规则id
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long rulesId;
    /**
     * 通知模板id
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long noticeTemplateId;
    /**
     * 通知模板名称
     */
    @TableField(exist = false)
    private String noticeTemplateName;
    /**
     * 租户ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
}
