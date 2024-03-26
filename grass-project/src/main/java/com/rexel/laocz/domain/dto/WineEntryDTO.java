package com.rexel.laocz.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName WineEntryDTO
 * @Description 入酒操作 开始、急停、继续
 * @Author 孟开通
 * @Date 2024/3/13 11:47
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WineEntryDTO extends WinBaseDTO {
    /**
     * 操作类型 1:开始 2:急停 3:继续
     */
    private String status;
}
