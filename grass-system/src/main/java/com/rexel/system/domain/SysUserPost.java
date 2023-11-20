package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author ids-admin
 */
@Data
@TableName("sys_user_post")
public class SysUserPost {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;
    /**
     * 租户Id
     */
    private String tenantId;
}
