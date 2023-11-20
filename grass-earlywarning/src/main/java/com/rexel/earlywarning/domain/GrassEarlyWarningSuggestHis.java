package com.rexel.earlywarning.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.core.domain.BaseEntity;

/**
 * 处置建议历史对象 grass_early_warning_suggest_his
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GrassEarlyWarningSuggestHis extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 报警历史ID
     */
    private Long hisId;
    /**
     * 处置建议ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long suggestId;
    /**
     * 建议分类
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String suggestType;
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
