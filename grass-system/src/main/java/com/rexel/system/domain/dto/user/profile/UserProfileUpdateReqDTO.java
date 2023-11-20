package com.rexel.system.domain.dto.user.profile;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserProfileUpdateReqDTO {

    /**
     * 用户主键
     */
    private Long userId;

    //private String userName;
    //@Length(min = 11, max = 11, message = "手机号长度必须 11 位")
    //private String phoneNumber;

    /**
     * 用户昵称
     */
    private String nickName;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过 50 个字符")
    private String email;

}
