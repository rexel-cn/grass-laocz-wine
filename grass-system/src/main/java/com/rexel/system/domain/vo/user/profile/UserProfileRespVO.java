package com.rexel.system.domain.vo.user.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.core.domain.entity.SysDept;
import com.rexel.common.core.domain.entity.SysRole;
import com.rexel.system.domain.SysPost;
import com.rexel.system.domain.base.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRespVO extends UserBase {

    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String roleName;

    private String deptName;
    private String postName;

    /**
     * 所属角色
     */
    private List<SysRole> roles;
    /**
     * 所在部门
     */
    private SysDept dept;
    /**
     * 所属岗位数组
     */
    private List<SysPost> posts;

}
