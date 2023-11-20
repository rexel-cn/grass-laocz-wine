package com.rexel.system.domain.dto.tenant;

import com.rexel.system.domain.base.TenantBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TenantCreateReqDTO extends TenantBase {

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 电话
     */
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phoneNumber;


    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 启用，停用
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
