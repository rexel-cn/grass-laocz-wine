package com.rexel.system.domain.vo;

import com.rexel.common.core.domain.entity.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName RuleNoticeVO
 * @Description
 * @Author 孟开通
 * @Date 2022/8/17 16:36
 **/
@Data
public class RuleNoticeVO implements Serializable {
    private String noticeTemplateId;
    private List<String> noticeModeInfoList;
    private List<SysUser> userList;
}
