package com.rexel.laocz.domain.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.rexel.laocz.enums.PotteryAltarStateEnum;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName WinePotteryAltarBaseDTO
 * @Description
 * @Author 孟开通
 * @Date 2024/3/29 10:00
 **/
@Data
public class WinePotteryAltarBaseDTO {
    /**
     * 防火区主键ID
     */
    private Long fireZoneId;
    /**
     * 陶坛管理编号 like
     */
    private String potteryAltarNumber;
    /**
     * 陶坛管理编号 eq
     */
    private String eqPotteryAltarNumber;
    /**
     * 陶坛管理主键ID
     */
    private List<Long> potteryAltarIds;
    /**
     * 陶坛状态
     */
    private Long potteryAltarState = PotteryAltarStateEnum.USE.getCode();

    public void setPotteryAltarIds(List<Long> potteryAltarIds) {
        if (CollectionUtil.isNotEmpty(potteryAltarIds)) {
            //如果Long为null就移除
            potteryAltarIds.removeIf(Objects::isNull);
        }
        this.potteryAltarIds = potteryAltarIds;
    }
}
