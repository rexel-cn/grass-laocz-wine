package com.rexel.system.domain.vo;

import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.GrassPointTagInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
// 回显用
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassPointTagInfoDTVO {

    /**
     * 标签信息
     */
    private GrassPointTagInfo grassPointTagInfo;


    private List<GrassPointInfo> pddtoList;


}
