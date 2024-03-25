package com.rexel.laocz.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaoczPumpVo {
    /**
     * 泵主键ID
     */
    private Long pumpId;
    /**
     * 泵编号
     */
    private Long pumpNumber;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 防火区名称
     */
    private String fireZoneName;
}
