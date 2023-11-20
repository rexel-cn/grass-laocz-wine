package com.rexel.system.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExcelReqDTO {
    /**
     * 登录帐号
     */
    private String userName;
    /**
     * 用户名称
     */
    private String nickName;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 状态
     */
    private Integer status;
}
