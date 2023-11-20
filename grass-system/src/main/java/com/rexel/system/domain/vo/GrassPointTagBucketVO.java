package com.rexel.system.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据降维用测点标签信息对象
 *
 * @author grass-service
 * @date 2023-02-28
 */
@Data
public class GrassPointTagBucketVO {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 测点ID
     */
    private String pointId;
    /**
     * 标签键
     */
    private String tagKey;
    /**
     * 标签值
     */
    private String tagValue;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 空间ID
     */
    private String bucketId;
}
