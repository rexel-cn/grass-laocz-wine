package com.rexel.system.domain.dto.user;

import com.rexel.system.domain.base.UserBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserUpdateReqDTO extends UserBase {

    /**
     * 用户id
     */
    @NotNull(message = "用户编号不能为空")
    private Long userId;

}
