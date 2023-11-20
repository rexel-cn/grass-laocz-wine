package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.rexel.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.core.domain.BaseEntity;

/**
 * 预聚合信息对象 grass_reduce_info
 *
 * @author grass-service
 * @date 2023-04-27
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassReduceInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 聚合颗粒度
     */
    @Excel(name = "聚合颗粒度")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String interval;

    /**
     * 聚合颗粒度名称
     */
    private String intervalName;

    /**
     * 存储时长
     */
    @Excel(name = "存储时长")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long retentionDays;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("interval", getInterval())
                .append("retentionDays", getRetentionDays())
                .toString();
    }
}
