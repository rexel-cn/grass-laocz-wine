package com.rexel.system.domain.vo;

import com.rexel.common.core.domain.vo.BaseNameValueVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName NoticeScopeDropDown
 * Description 通知模板 通知范围下拉框
 * Author 孟开通
 * Date 2022/8/4 17:12
 **/
@Data
public class NoticeScopeDropDown implements Serializable {
    /**
     *
     */
    private String noticeScope;
    /**
     * 前端显示使用
     */
    private String noticeScopeName;

    /**
     * 通知范围对象
     */
    private List<BaseNameValueVO> nameValueVOList;
}
