package com.rexel.laocz.domain.vo;


import com.rexel.laocz.domain.LaoczFireZoneInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FireZoneInfoVo extends LaoczFireZoneInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 场区名称
     */
    private String areaName;

}
