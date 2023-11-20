package com.rexel.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName GrassPointHisDTO
 * @Description
 * @Author 孟开通
 * @Date 2022/10/24 10:56
 **/
@Data
public class GrassPointHisDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date from;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date to;
    private List<Long> ids;
}
