package com.rexel.system.domain.base;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 租户 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class TenantBase {

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 租户名
     */
    @NotNull(message = "公司中文名称不能为空")
    private String tenantName;

    /**
     * 租户英文名
     */
    @NotBlank(message = "公司英文名称不能为空")
    //@Size(max = 25, min = 3)
    //@Pattern(regexp = "^\\s*|[0-9A-Za-z]*$", message = "企业名称(英文)为数字和字母组合")
    private String engName;
    /**
     * 存储时间
     */
    @NotNull(message = "存储时间不能为空")
    private String everySeconds;
    /**
     * 、
     * 坐标
     */
    private String coordinate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 图标
     */
    private String logo;
}
