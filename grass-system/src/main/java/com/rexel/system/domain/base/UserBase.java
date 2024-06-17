package com.rexel.system.domain.base;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 用户 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
@Accessors(chain = true)
public class UserBase {

    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    private String userName;

    private String remark;

    private String deptId;

    private Set<Long> postIds;

    private Set<Long> roleIds;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过 50 个字符")
    private String email;

    //@NotBlank(message = "电话号码不能为空")
    //@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phoneNumber;

    private Integer sex;

    private String avatar;

    private String userType;

    private String status;

}
