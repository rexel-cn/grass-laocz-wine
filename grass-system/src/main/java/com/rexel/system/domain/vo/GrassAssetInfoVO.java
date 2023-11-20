package com.rexel.system.domain.vo;

import com.rexel.system.domain.GrassAsset;
import com.rexel.system.domain.GrassPointInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName GrassAssetInfoVO
 * @Description
 * @Author 孟开通
 * @Date 2022/10/14 15:51
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassAssetInfoVO implements Serializable {
    private GrassAsset grassAsset;
    private List<GrassPointInfo> pointInfoList;
}
