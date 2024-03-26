package com.rexel.laocz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 树状下拉框子
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-26 1:16 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreePullDownChildrenVO {
    private Long value;
    private String label;
}
