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
 * 工艺组态地址信息对象 grass_web_conf
 *
 * @author grass-service
 * @date 2022-07-18
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassWebConf extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 组态名称
     */
    @Excel(name = "组态名称")
    private String webName;
    /**
     * 组态地址
     */
    @Excel(name = "组态地址")
    private String webAddress;
    /**
     * 组态分类
     */
    @Excel(name = "组态分类")
    private String webType;
    /**
     * 组态图片地址
     */
    @Excel(name = "组态图片地址")
    private String webPictureUrl;
    /**
     * 组态描述
     */
    @Excel(name = "组态描述")
    private String webDescribe;
    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long webSort;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;

    /**
     * 组态分类名
     */
    @TableField(exist = false)
    private String webTypeName;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("webName", getWebName())
                .append("webAddress", getWebAddress())
                .append("webType", getWebType())
                .append("webPictureUrl", getWebPictureUrl())
                .append("webDescribe", getWebDescribe())
                .append("webSort", getWebSort())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
