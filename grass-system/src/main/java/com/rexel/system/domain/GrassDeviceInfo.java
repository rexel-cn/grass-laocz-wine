package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
 * 设备信息对象 grass_device_info
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassDeviceInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 存储空间ID
     */
    private String bucketId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 设备ID
     */
    @TableId
    private String deviceId;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deviceName;
    /**
     * 连接ID
     */
    @Excel(name = "连接ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long linkId;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("bucketId", getBucketId())
                .append("tenantId", getTenantId())
                .append("deviceId", getDeviceId())
                .append("deviceName", getDeviceName())
                .append("linkId", getLinkId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
