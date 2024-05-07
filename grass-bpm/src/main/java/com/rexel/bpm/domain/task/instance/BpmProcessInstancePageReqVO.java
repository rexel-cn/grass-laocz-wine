package com.rexel.bpm.domain.task.instance;

import com.rexel.common.validation.InEnum;
import com.rexel.bpm.enums.BpmProcessInstanceStatusEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
public class BpmProcessInstancePageReqVO  {

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程定义的编号
     * example = "2048"
     */
    private String processDefinitionId;

    /**
     * 流程实例的状态
     * example = "1"
     */
    @InEnum(BpmProcessInstanceStatusEnum.class)
    private Integer status;

    /**
     * 流程分类
     * example = "1"
     */
    private String category;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

    /**
     * 发起用户编号
     */
    private Long startUserId; // 注意，只有在【流程实例】菜单，才使用该参数

}
