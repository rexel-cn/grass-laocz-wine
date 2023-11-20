package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 存储信息对象 grass_bucket_info
 *
 * @author grass-service
 * @date 2022-08-15
 */
@Data
public class GrassBucketInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 存储空间ID
     */
    @TableId(type = IdType.INPUT)
    private String bucketId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 存储空间名称
     */
    @Excel(name = "存储空间名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String bucketName;
    /**
     * InfluxDB中的ID
     */
    @Excel(name = "InfluxDB中的ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String bucketCode;
    @Excel(name = "存储时间")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long everySeconds;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("bucketId", getBucketId())
                .append("tenantId", getTenantId())
                .append("bucketName", getBucketName())
                .append("bucketCode", getBucketCode())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
