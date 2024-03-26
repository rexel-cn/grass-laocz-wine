package com.rexel.laocz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 树状下拉框父
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-26 1:15 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreePullDownVO {
    private Long value;
    private String label;
    private List<TreePullDownChildrenVO> children;
}
