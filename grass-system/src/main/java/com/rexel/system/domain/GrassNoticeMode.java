package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 通知模板对应方式对象 grass_notice_mode
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassNoticeMode {

    private static final long serialVersionUID = 1L;

    /**
     * 通知模板id
     */
    @Excel(name = "通知模板id")
    private Long noticeTemplateId;
    /**
     * 通知方式(参考字典notification_type)
     */
    @Excel(name = "通知方式(参考字典notification_type)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long noticeMode;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("noticeTemplateId", getNoticeTemplateId())
                .append("noticeMode", getNoticeMode())
                .toString();
    }
}
