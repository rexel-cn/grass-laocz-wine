package com.rexel.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 列头元数据对象 sys_header_metadata
 *
 * @author grass-service
 * @date 2022-07-07
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysHeaderMetadata {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 表头名字
     */
    private String headerName;
    /**
     * 表头字段
     */
    private String prop;
    /**
     * 表头字段名称
     */
    private String propName;
    /**
     * 1:true,0:false
     */
    private Integer hasSort;
    /**
     * 当前排序
     */
    private Long currentSort;
    /**
     * 是否删除 0,未删除；1:删除
     */
    private Long isDelete;
    /**
     * 描述
     */
    private String propDescribtion;
    private Date createTime;
    private Date updateTime;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("headerName", getHeaderName())
                .append("prop", getProp())
                .append("propName", getPropName())
                .append("hasSort", getHasSort())
                .append("currentSort", getCurrentSort())
                .append("isDelete", getIsDelete())
                .append("propDescribtion", getPropDescribtion())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
