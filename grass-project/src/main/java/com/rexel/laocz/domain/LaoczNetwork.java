package com.rexel.laocz.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 老村长网络设备对象 laocz_network
 *
 * @author grass-service
 * @date 2024-06-11
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LaoczNetwork extends BaseEntity {

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
    @NotEmpty(message = "网络设备名不能为空")
    private String networkName;
    /**
     * ip
     */
    @Excel(name = "ip")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    @NotEmpty(message = "ip不能为空")
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", message = "无效的IP地址")
    private String ipAddr;
}

