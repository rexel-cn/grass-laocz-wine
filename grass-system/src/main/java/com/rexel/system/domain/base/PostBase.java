package com.rexel.system.domain.base;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 岗位 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class PostBase {

    /**
     * 岗位名
     */
    @NotBlank(message = "岗位名称不能为空")
    @Size(max = 50, message = "岗位名称长度不能超过50个字符")
    private String postName;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Long postSort;

}
