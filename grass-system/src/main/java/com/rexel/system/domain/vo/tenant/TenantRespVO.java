package com.rexel.system.domain.vo.tenant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.system.domain.base.TenantBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TenantRespVO extends TenantBase {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private List<Integer> checkedKeys;

    private Long userId;

    private String nickName;

    private String userName;

    private String phoneNumber;

    private String status;

    private String email;

    private Boolean menuCheckStrictly;
}
