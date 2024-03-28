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
 * 陶坛管理对象 laocz_pottery_altar_management
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczPotteryAltarManagement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 陶坛管理主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long potteryAltarId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 陶坛管理编号
     */
    @Excel(name = "陶坛管理编号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String potteryAltarNumber;
    /**
     * 陶坛状态，1：使用，2封存
     */
    @Excel(name = "陶坛状态，1：使用，2封存")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long potteryAltarState;
    /**
     * 防火区主键ID，外键关联laocz_fire_zone_info
     */
    @Excel(name = "防火区主键ID，外键关联laocz_fire_zone_info")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long fireZoneId;
    /**
     * 满坛重量
     */
    @Excel(name = "满坛重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long potteryAltarFullAltarWeight;
    /**
     * 陶坛二维码地址
     */
    @Excel(name = "陶坛二维码地址")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String potteryAltarQrCodeAddress;

    /**
     * 区域名称
     */
    @TableField(exist = false)
    private String areaId;
    /**
     * 酒液批次Id
     */
    @TableField(exist = false)
    private String liquorBatchId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("potteryAltarId", getPotteryAltarId())
                .append("tenantId", getTenantId())
                .append("potteryAltarNumber", getPotteryAltarNumber())
                .append("potteryAltarState", getPotteryAltarState())
                .append("fireZoneId", getFireZoneId())
                .append("potteryAltarFullAltarWeight", getPotteryAltarFullAltarWeight())
                .append("potteryAltarQrCodeAddress", getPotteryAltarQrCodeAddress())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
