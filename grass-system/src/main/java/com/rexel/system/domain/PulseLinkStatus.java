package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 设备状态对象 pulse_link_status
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PulseLinkStatus {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 机器码
     */
    @Excel(name = "机器码")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String machineCode;
    /**
     * 连接时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "连接时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date linkTime;
    /**
     * 连接状态(1:在线,0:离线)
     */
    @Excel(name = "连接状态(1:在线,0:离线)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String linkStatus;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("machineCode", getMachineCode())
                .append("linkTime", getLinkTime())
                .append("linkStatus", getLinkStatus())
                .toString();
    }
}
