package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author ids-admin
 */
@Data
@TableName("sys_user_role")
public class SysUserRole {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 租户Id
     */
    private String tenantId;
}
