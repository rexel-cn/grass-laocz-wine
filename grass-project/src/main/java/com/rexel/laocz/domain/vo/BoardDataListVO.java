package com.rexel.laocz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 看板
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-22 3:59 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDataListVO {
    private Integer tableTotal;
    private List<BoardDataVO> boardDataVOList;
}
