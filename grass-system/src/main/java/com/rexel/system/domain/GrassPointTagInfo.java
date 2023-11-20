package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 测点标签信息对象 grass_point_tag_info
 *
 * @author grass-service
 * @date 2022-10-17
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassPointTagInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 标签键
     */
    @Excel(name = "标签键")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tagKey;
    /**
     * 标签值
     */
    @Excel(name = "标签值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tagValue;
    /**
     * 标签类型
     */
    @Excel(name = "标签类型")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tagType;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tagKey", getTagKey())
                .append("tagValue", getTagValue())
                .append("tagType", getTagType())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
