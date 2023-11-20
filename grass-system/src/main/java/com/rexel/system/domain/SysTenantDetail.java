package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户(公司)详情对象 sys_tenant_detail
 *
 * @author grass-service
 * @date 2023-03-01
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysTenantDetail {

    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 宣传图片url
     */
    @Excel(name = "宣传图片url")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String introduceUrl;

    /**
     * 文本标题
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String textTitle;
    /**
     * 公司介绍
     */
    @Excel(name = "公司介绍")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String companyProfile;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("introduceUrl", getIntroduceUrl())
                .append("companyProfile", getCompanyProfile())
                .toString();
    }
}
