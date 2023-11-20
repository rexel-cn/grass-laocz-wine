package com.rexel.earlywarning.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.*;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.core.domain.BaseEntity;

/**
 * 处置建议对象 grass_suggest_info
 *
 * @author grass-service
 * @date 2023-09-15
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GrassEarlyWarningSuggest extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    private Long suggestId;
    /**
     * 建议分类
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String suggestType;
    /**
     * 建议分类名称
     */
    @TableField(exist = false)
    private String suggestTypeName;
    /**
     * 建议标题
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String suggestTitle;
    /**
     * 建议内容
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String suggestContent;
    /**
     * 租户ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
}
