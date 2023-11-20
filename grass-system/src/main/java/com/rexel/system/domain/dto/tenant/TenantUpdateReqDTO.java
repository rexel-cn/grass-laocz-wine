package com.rexel.system.domain.dto.tenant;

import com.rexel.system.domain.base.TenantBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TenantUpdateReqDTO extends TenantBase {

    /**
     * 租户主键
     */
    private Long id;

    /**
     * 手机号(现登录账号)
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phoneNumber;

//    private String mobile;

    /**
     * 用户Id
     */
    private Long userId;
//
//    /**
//     * 登录帐号
//     */
//    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "用户账号由 数字、字母 组成")
//    private String userName;

    /**
     * 用户名
     */
//    private String nickName;
    private String userName;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态
     */
    private String status;

    /**
     * 父子联动
     */
    private Boolean menuCheckStrictly;
    /**
     * 菜单id
     */
    private Set<Long> menuIds;

}
