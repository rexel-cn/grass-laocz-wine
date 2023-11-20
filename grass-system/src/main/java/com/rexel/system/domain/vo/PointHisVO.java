package com.rexel.system.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PointHisVO
 * @Description
 * @Author 孟开通
 * @Date 2022/10/24 13:34
 **/
@Data
public class PointHisVO implements Serializable {
    /**
     * 数据列表
     */
    private List<Map<String, String>> pointHisData;
    /**
     * 列头
     */
    private LinkedHashMap<String, String> headerMetadata;

}
