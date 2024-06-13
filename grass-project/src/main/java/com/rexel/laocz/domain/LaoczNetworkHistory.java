package com.rexel.laocz.domain;

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
 * 老村长网络设备报警对象 laocz_network_history
 *
 * @author grass-service
 * @date 2024-06-13
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczNetworkHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
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
     * 网络设备名
     */
    @Excel(name = "网络设备名")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String networkName;
    /**
     * ip
     */
    @Excel(name = "ip")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String ipAddr;
    /**
     * 报警内容
     */
    @Excel(name = "报警内容")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String content;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("networkName", getNetworkName())
                .append("ipAddr", getIpAddr())
                .append("content", getContent())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
