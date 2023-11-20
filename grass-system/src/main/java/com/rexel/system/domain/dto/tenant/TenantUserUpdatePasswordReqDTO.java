package com.rexel.system.domain.dto.tenant;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TenantUserUpdatePasswordReqDTO {
    /**
     * 租户id
     */
    private Long id;
    /**
     * 用户id
     */
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

}
