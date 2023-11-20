package com.rexel.system.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 角色 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class RoleBase {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;

    /**
     * 角色类型：0管理员角色，1普通角色
     */
    private Integer roleType;
    private String roleKey;

//    @NotBlank(message = "角色标志不能为空")
//    @Size(max = 100, message = "角色标志长度不能超过100个字符")
//    private String roleKey;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;
    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 父子联动
     */
    private Boolean menuCheckStrictly;
}
