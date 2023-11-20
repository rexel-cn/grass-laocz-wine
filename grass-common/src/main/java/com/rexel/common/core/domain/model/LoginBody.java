package com.rexel.common.core.domain.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户登录对象
 *
 * @author ids-admin
 */
@Data
public class LoginBody implements Serializable {
    /**
     * 公司英文名
     */
//    @NotBlank(message = "公司英文名称不能为空")
//    private String engName;
    /**
     * 用户名
     */
//    @NotBlank(message = "用户名称不能为空")
//    private String username;
    /**
     * 用户密码
     */
    @NotBlank(message = "电话号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phoneNumber;

    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空")
    private String password;
    /**
     * 验证码
     */
    private String code;
    /**
     * 唯一标识
     */
    private String uuid = "";

}
