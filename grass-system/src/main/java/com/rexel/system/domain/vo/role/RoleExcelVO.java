package com.rexel.system.domain.vo.role;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 角色 Excel 导出响应 VO
 */
@Data
public class RoleExcelVO {

    @ExcelProperty("角色序号")
    private Long id;

    @ExcelProperty("角色名称")
    private String name;

    @ExcelProperty("角色标志")
    private String code;

    @ExcelProperty("角色排序")
    private Integer sort;

    @ExcelProperty("数据范围")
    private Integer dataScope;

    private String status;

}
