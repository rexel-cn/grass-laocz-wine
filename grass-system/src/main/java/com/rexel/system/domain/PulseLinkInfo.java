package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 连接信息对象 pulse_link_info
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Data
public class PulseLinkInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 连接ID
     */
    private Long linkId;
    /**
     * 连接时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "连接时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date linkTime;
    /**
     * 机器码
     */
    @Excel(name = "机器码")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String machineCode;
    /**
     * 物联设备型号
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String linkDeviceType;
    /**
     * 连接租户
     */
    private String linkTenant;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("linkId", getLinkId())
                .append("linkTime", getLinkTime())
                .append("machineCode", getMachineCode())
                .toString();
    }
}
