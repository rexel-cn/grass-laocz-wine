package com.rexel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName PulseLinkStatusEnum
 * @Description
 * @Author 孟开通
 * @Date 2023/4/21 13:22
 **/
@AllArgsConstructor
@NoArgsConstructor
public enum PulseLinkStatusEnum {
    /**
     * 连接状态(1:在线,0:离线)
     */
    ONLINE("1", "在线"),
    OFFLINE("0", "离线");
    @Getter
    private String code;
    @Getter
    private String desc;


}
