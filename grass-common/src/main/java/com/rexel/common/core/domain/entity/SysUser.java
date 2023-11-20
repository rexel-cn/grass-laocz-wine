package com.rexel.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import com.rexel.common.handler.JsonLongSetTypeHandler;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.Set;

/**
 * 用户对象 sys_user
 *
 * @author ids-admin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_user", autoResultMap = true)
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long userId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 部门ID
     */
    @Excel(name = "部门ID")
    private String deptId;
    @Excel(name = "岗位ID数组")
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> postIds;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String userName;
    /**
     * 用户类型:0超级管理员,1管理员,2普通租户
     */
    @Excel(name = "用户类型:0超级管理员,1管理员,2普通租户")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String userType;
    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String email;
    /**
     * 手机号码(现在的唯一登录账号)
     */
    @Excel(name = "手机号码")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String phoneNumber;
    /**
     * 用户性别（0男 1女 2未知）
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String sex;
    /**
     * 头像地址
     */
    @Excel(name = "头像地址")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String avatar;
    /**
     * 密码
     */
    @Excel(name = "密码")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String password;
    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer delFlag;
    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String loginIp;
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Date loginDate;

    @TableField(exist = false)
    private String bucketId;

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("tenantId", getTenantId())
                .append("deptId", getDeptId())
                .append("userName", getUserName())
//                .append("nickName", getNickName())
                .append("userType", getUserType())
                .append("email", getEmail())
                .append("phoneNumber", getPhoneNumber())
                .append("sex", getSex())
                .append("avatar", getAvatar())
                .append("password", getPassword())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("loginIp", getLoginIp())
                .append("loginDate", getLoginDate())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
