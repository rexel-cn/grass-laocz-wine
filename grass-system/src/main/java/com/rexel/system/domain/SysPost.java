package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 岗位表 sys_post
 *
 * @author ids-admin
 */
@Data
@TableName("sys_post")
public class SysPost extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    @TableId
    private Long postId;
    /**
     * 岗位编码
     */
    @Excel(name = "岗位编码")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String postCode;
    /**
     * 岗位名称
     */
    @Excel(name = "岗位名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String postName;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long postSort;
    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String status;
    /**
     * 租户Id
     */
    @Excel(name = "租户Id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("postId", getPostId())
                .append("postCode", getPostCode())
                .append("postName", getPostName())
                .append("postSort", getPostSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("tenantId", getTenantId())
                .toString();
    }
}
