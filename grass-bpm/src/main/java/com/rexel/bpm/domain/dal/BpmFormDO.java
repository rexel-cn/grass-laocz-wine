package com.rexel.bpm.domain.dal;



import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmFormDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 表单名
     */
    private String name;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 表单的配置
     */
    private String conf;
    /**
     * 表单项的数组
     *
     * 目前直接将 https://github.com/JakHuang/form-generator 生成的 JSON 串，直接保存
     * 定义：https://github.com/JakHuang/form-generator/issues/46
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> fields;
    /**
     * 备注
     */
    private String remark;

}
