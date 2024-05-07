package com.rexel.bpm.domain.task.instance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
public class BpmProcessInstanceCopyPageReqVO   {

    /**
     * 流程名称
     */
    private String processInstanceName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

}
