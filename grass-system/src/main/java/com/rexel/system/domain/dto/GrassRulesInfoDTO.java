package com.rexel.system.domain.dto;

import com.rexel.system.domain.GrassRulesInfo;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: HuWei.Fu
 * @Date: 2022/11/17/17:10
 * @Description:
 */
@Data
public class GrassRulesInfoDTO extends GrassRulesInfo {

    // 资产设备名
    private String assetName;

    // 归属分类
    private String assetTypeName;

    // 点位名称
    private String pointName;

}
