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
 * 酒品字典对象 laocz_liquor_dict
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczLiquorDict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 酒品字典id
     */
    @TableId(type = IdType.AUTO)
    private Long liquorDictId;
    /**
     * 1:酒品信息,2:酒业等级信息
     */
    @Excel(name = "1:酒品信息,2:酒业等级信息")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long dictType;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 信息描述
     */
    @Excel(name = "信息描述")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String wineDcitInfo;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("liquorDictId", getLiquorDictId())
                .append("dictType", getDictType())
                .append("tenantId", getTenantId())
                .append("wineDcitInfo", getWineDcitInfo())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
