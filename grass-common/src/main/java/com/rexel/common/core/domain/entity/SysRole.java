package com.rexel.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色表 sys_role
 *
 * @author ids-admin
 */
@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;
    /**
     * $column.columnComment
     */
    @Excel(name = "租户Id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 角色名称
     */
    @Excel(name = "角色名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String roleName;
    /**
     * 角色权限字符串
     */
    @Excel(name = "角色权限字符串")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String roleKey;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long roleSort;
    /**
     * 角色类型：0管理员角色，1普通角色
     */
    @Excel(name = "角色类型：0管理员角色，1普通角色")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer roleType;
    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @Excel(name = "数据范围", readConverterExp = "1=：全部数据权限,2=：自定数据权限,3=：本部门数据权限,4=：本部门及以下数据权限")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String dataScope;
    /**
     * 菜单树选择项是否关联显示
     */
    @Excel(name = "菜单树选择项是否关联显示")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private boolean menuCheckStrictly;
    /**
     * 部门树选择项是否关联显示
     */
    @Excel(name = "部门树选择项是否关联显示")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private boolean deptCheckStrictly;
    /**
     * 角色状态（0正常 1停用）
     */
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer delFlag;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("tenantId", getTenantId())
                .append("roleName", getRoleName())
                .append("roleKey", getRoleKey())
                .append("roleSort", getRoleSort())
                .append("roleType", getRoleType())
                .append("dataScope", getDataScope())
                .append("menuCheckStrictly", isMenuCheckStrictly())
                .append("deptCheckStrictly", isDeptCheckStrictly())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }

}
