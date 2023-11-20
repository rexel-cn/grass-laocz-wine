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
 * 通知短信配置对象 grass_notice_sms
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassNoticeSms extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 通知配置ID
     */
    @Excel(name = "通知配置ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long noticeConfigId;
    /**
     * 短信签名
     */
    @Excel(name = "短信签名")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String signName;
    /**
     * 短信模板code(参考阿里云短信)
     */
    @Excel(name = "短信模板code(参考阿里云短信)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String templateCode;
    /**
     * 模板审核结果
     */
    @Excel(name = "模板审核结果")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String examineResult;
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
                .append("noticeConfigId", getNoticeConfigId())
                .append("signName", getSignName())
                .append("templateCode", getTemplateCode())
                .append("examineResult", getExamineResult())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
