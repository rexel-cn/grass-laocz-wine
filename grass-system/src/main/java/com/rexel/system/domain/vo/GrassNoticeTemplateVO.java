package com.rexel.system.domain.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rexel.system.domain.GrassNoticeTemplate;
import com.rexel.system.domain.dto.GrassNoticeScopeDTO;
import lombok.Data;

import java.util.List;


/**
 * ClassName GrassNoticeTemplateVO
 * Description
 * Author 孟开通
 * Date 2022/8/2 17:56
 **/
@Data
public class GrassNoticeTemplateVO extends GrassNoticeTemplate {
    /**
     * 通知范围
     */
    List<GrassNoticeScopeDTO> grassNoticeScopeList;
    /**
     * 通知方式
     */
    private List<String> modeArray;
    @JsonIgnore
    private List<String> modeArrayDesc;
    private String mode;

    @JsonIgnore
    private List<String> userNameList;
    private String userName;

    @JsonIgnore
    private List<String> roleNameList;
    private String roleName;

    public String getMode() {
        if (CollectionUtil.isNotEmpty(modeArrayDesc)) {
            return String.join(",", modeArrayDesc);
        }
        return mode;
    }

    public String getUserName() {
        if (CollectionUtil.isNotEmpty(userNameList)) {
            return String.join(",", userNameList);
        }
        return userName;
    }

    public String getRoleName() {
        if (CollectionUtil.isNotEmpty(roleNameList)) {
            return String.join(",", roleNameList);
        }
        return roleName;
    }
}
