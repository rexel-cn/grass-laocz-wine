package com.rexel.earlywarning.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrassSuggestInfoVO {
    /**
     * 自增ID
     */
    private Long suggestId;
    /**
     * 建议分类
     */
    private String suggestType;
    /**
     * 建议分类名称
     */
    private String suggestTypeName;
    /**
     * 建议标题
     */
    private String suggestTitle;
    /**
     * 建议内容
     */
    private String suggestContent;
}
