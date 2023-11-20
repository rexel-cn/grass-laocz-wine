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
 * 通知配置模板对象 grass_notice_template
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassNoticeTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 通知模板主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 通知模板名称
     */
    @Excel(name = "通知模板名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String noticeTemplateName;
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
                .append("noticeTemplateName", getNoticeTemplateName())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
