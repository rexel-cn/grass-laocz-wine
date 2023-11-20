package com.rexel.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 通知配置主对象 grass_notice_config
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassNoticeConfigVO {
    private static final long serialVersionUID = 1L;
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 推送类型(邮件1、站内信0、短信2、钉钉3)
     */
    private String pushType;
    /**
     * 推送类型(邮件、站内信、短信、钉钉)
     */
    private String pushTypeDesc;
    /**
     * 是否开通(0:未开通;1:开通)
     */
    private String isOpen;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("pushType", getPushType())
                .append("isOpen", getIsOpen())
                .toString();
    }
}
