package com.rexel.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PulseLinkInfoDTO
 * @Description 物联设备查询
 * @Author 孟开通
 * @Date 2022/10/11 09:28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PulseLinkInfoDTO implements Serializable {
    /**
     * 物联设备型号
     */
    private String linkDeviceType;
    /**
     * 物联设备名称
     */
    private String deviceName;
    /**
     * 设备状态
     */
    private String ljStatus;
    /**
     * 是否有主
     */
    private Boolean master;

    private Integer pageNum;

    private Integer pageSize;

    private String tenantId;

    public PulseLinkInfoDTO(Boolean master) {
        this.master = master;
    }

    public PulseLinkInfoDTO(Boolean master, String tenantId) {
        this.master = master;
        this.tenantId = tenantId;
    }

    public Integer getPageNum() {
        if (pageNum != null && pageSize != null) {
            return (pageNum - 1) * pageSize;
        }
        return null;

    }
}

