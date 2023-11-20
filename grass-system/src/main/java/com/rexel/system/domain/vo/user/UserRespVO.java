package com.rexel.system.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.system.domain.base.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRespVO extends UserBase {

    private Long userId;

    private String loginIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String deptName;
}
