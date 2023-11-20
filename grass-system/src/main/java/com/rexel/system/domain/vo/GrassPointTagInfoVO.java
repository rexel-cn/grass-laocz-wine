package com.rexel.system.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import com.rexel.system.domain.GrassPointTagInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

// 数据查询列表用
@Data
public class GrassPointTagInfoVO extends GrassPointTagInfo {


    @Excel(name = "关联数量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer relationNum;

    @Excel(name = "标签类型描述")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tageTypeDesc;

    /**
     * 备注
     */
    @Excel(name = "备注")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String remark;


}
