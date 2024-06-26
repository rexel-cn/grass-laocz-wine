package com.rexel.laocz.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.rexel.common.core.domain.entity.SysDictData;
import com.rexel.common.core.domain.vo.BaseNameValueVO;
import com.rexel.common.utils.DictUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName EquipmentUtils
 * @Description 资产设备：泵，称重罐工具类
 * @Author 孟开通
 * @Date 2024/6/25 13:46
 **/
public class EquipmentUtils {
    /**
     * 根据字典类型获取使用标识
     *
     * @param dictType 字典类型
     * @return 使用标识 name=
     */
    public static List<BaseNameValueVO> getDictCache(String dictType) {
        List<SysDictData> dictCache = DictUtils.getDictCache(dictType);
        return convertDictDataToBaseNameValueVO(dictCache);
    }

    /**
     * 获取泵使用标识
     *
     * @param dictCache 字典数据
     * @return 使用标识
     */
    private static List<BaseNameValueVO> convertDictDataToBaseNameValueVO(List<SysDictData> dictCache) {
        if (CollectionUtil.isEmpty(dictCache)) {
            return null;
        }
        return dictCache.stream().map(sysDictData -> {
            BaseNameValueVO baseNameValueVO = new BaseNameValueVO();
            baseNameValueVO.setName(sysDictData.getDictLabel());
            baseNameValueVO.setValue(sysDictData.getDictValue());
            return baseNameValueVO;
        }).collect(Collectors.toList());
    }
}
