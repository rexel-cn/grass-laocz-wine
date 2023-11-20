package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 【请填写功能名称】对象 grass_point_user_star
 *
 * @author grass-service
 * @date 2022-10-21
 */
@Data
public class GrassPointUserStar {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 资产主键
     */
    @Excel(name = "资产主键")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String assetId;
    /**
     * 用户id
     */
    @Excel(name = "用户id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long userId;
    /**
     * 测点唯一标识
     */
    @Excel(name = "测点唯一标识")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointPrimaryKey;
    /**
     * 置顶时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "置顶时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date topTime;
    /**
     * 租户id
     */
    @Excel(name = "租户id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("assetId", getAssetId())
                .append("userId", getUserId())
                .append("pointPrimaryKey", getPointPrimaryKey())
                .append("topTime", getTopTime())
                .append("tenantId", getTenantId())
                .toString();
    }
}
