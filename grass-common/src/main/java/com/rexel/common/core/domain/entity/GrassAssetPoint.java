package com.rexel.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 资产测点关联对象 grass_asset_point
 *
 * @author grass-service
 * @date 2022-07-19
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassAssetPoint {
    private static final long serialVersionUID = 1L;

    /**
     * 资产id
     */
    private String assetId;
    /**
     * 点位主键id
     */
    private Long pointPrimaryKey;

    /**
     * 测点主键id
     */
    @TableField(exist = false)
    private Long id;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("assetId" , getAssetId())
                .append("pointPrimaryKey" , getPointPrimaryKey())
                .toString();
    }
}
