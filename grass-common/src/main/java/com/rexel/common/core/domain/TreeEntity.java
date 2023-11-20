package com.rexel.common.core.domain;

import com.rexel.common.utils.StringUtils;
import lombok.Data;

/**
 * Tree基类
 *
 * @author ids-admin
 */
@Data
public abstract class TreeEntity extends com.rexel.common.core.domain.BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 父部门ID
     */
    protected String parentId = "0";

    /**
     * 祖级列表
     */
    protected String ancestors = "0,";

    public abstract String getId();

    public String makeSelfAsNewParentIds() {
        if (StringUtils.isEmpty(getAncestors())) {
            return getId() + ",";
        }
        return getAncestors() + getId() + ",";
    }
}
