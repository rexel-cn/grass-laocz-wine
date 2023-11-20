package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 通知模板对应范围对象 grass_notice_scope
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Data
public class GrassNoticeScope implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知模板id
     */
    @Excel(name = "通知模板id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long noticeTemplateId;
    /**
     * 通知范围(参考字典notice_scope)
     */
    @Excel(name = "通知范围(参考字典notice_scope)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long noticeScope;
    /**
     * 通知对象id(包括个人跟角色)
     */
    @Excel(name = "通知对象id(包括个人跟角色)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long noticeObject;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("noticeTemplateId", getNoticeTemplateId())
                .append("noticeScope", getNoticeScope())
                .append("noticeObject", getNoticeObject())
                .toString();
    }
}
