package com.rexel.system.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExcelRespVO implements Serializable {

    @Excel(name = "用户编号")
    private String userId;
    @Excel(name = "登录帐号")
    private String userName;
    @Excel(name = "用户昵称")
    private String nickName;
    @Excel(name = "手机号")
    private String phoneNumber;
    @Excel(name = "所属部门")
    private String deptName;
    @Excel(name = "状态", readConverterExp = "0=启用中,1=停用中")
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Excel(isExport = false)
    private String deptId;

}
