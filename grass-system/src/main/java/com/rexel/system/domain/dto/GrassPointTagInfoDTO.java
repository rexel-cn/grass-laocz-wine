package com.rexel.system.domain.dto;

import com.rexel.system.domain.GrassPointTagInfo;
import lombok.Data;

import java.util.List;
@Data
public class GrassPointTagInfoDTO {
    /**
     * 标签信息
     */
    private GrassPointTagInfo grassPointTagInfo;

    /**
     * 关联测点集合
     */
    private List<GrassPDDTO> pddtoList;

    // 测点信息表的id
    private List<Long> pointIds;



}
