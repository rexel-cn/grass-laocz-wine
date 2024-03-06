package com.rexel.laocz.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 酒品管理对象 laocz_liquor_management
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczLiquorManagement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 酒品管理ID
     */
    private Long liquorManagementId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 酒品名称
     */
    @Excel(name = "酒品名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorName;
    /**
     * 酒类等级——酒品字典维护
     */
    @Excel(name = "酒类等级——酒品字典维护")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorLevel;
    /**
     * 酒业轮次
     */
    @Excel(name = "酒业轮次")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorRound;
    /**
     * 酒香名称——酒品字典维护
     */
    @Excel(name = "酒香名称——酒品字典维护")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorFlavorName;
    /**
     * 酒液来源
     */
    @Excel(name = "酒液来源")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorSource;
    /**
     * 酒液年份
     */
    @Excel(name = "酒液年份")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorYear;
    /**
     * 酒液酿造时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "酒液酿造时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date liquorBrewingTime;
    /**
     * 酒精度数
     */
    @Excel(name = "酒精度数")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorContent;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("liquorManagementId", getLiquorManagementId())
                .append("tenantId", getTenantId())
                .append("liquorName", getLiquorName())
                .append("liquorLevel", getLiquorLevel())
                .append("liquorRound", getLiquorRound())
                .append("liquorFlavorName", getLiquorFlavorName())
                .append("liquorSource", getLiquorSource())
                .append("liquorYear", getLiquorYear())
                .append("liquorBrewingTime", getLiquorBrewingTime())
                .append("liquorContent", getLiquorContent())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
