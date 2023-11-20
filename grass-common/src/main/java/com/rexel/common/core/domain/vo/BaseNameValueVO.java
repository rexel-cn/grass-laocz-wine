package com.rexel.common.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName NoticeScopeDropDown
 * Description
 * Author 孟开通
 * Date 2022/8/4 17:12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseNameValueVO implements Serializable {
    private String name;
    private Object value;

}
