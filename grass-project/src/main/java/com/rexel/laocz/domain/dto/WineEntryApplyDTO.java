package com.rexel.laocz.domain.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName wineEntryApplyDTO
 * @Description 入酒申请
 * @Author 孟开通
 * @Date 2024/3/6 15:42
 **/
@Data
public class WineEntryApplyDTO {
    /**
     * 申请重量
     */
    @NotNull(message = "申请重量不能为空")
    private Double applyWeight;

    /**
     * 陶坛罐ID
     */
    @NotNull(message = "未选择陶坛罐")
    @Valid
    private List<WineOutApplyDTO> potteryAltars;

    /**
     * 酒品管理ID
     */
    @NotNull(message = "酒品未选择")
    private Long liquorManagementId;

    /**
     * 酒品批次号
     */
    @NotEmpty(message = "酒品批次号错误，请重新申请")
    private String liquorBatchId;

}
