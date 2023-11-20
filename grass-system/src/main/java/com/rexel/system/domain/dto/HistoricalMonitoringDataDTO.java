package com.rexel.system.domain.dto;

import com.rexel.system.domain.GrassPointInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName HistoricalMonitoringDataDTO
 * @Description 历史检测数据
 * @Author 孟开通
 * @Date 2022/10/21 13:19
 **/
@Data
public class HistoricalMonitoringDataDTO {
    private Date from;
    private Date to;
    private List<GrassPointInfo> list;
}
