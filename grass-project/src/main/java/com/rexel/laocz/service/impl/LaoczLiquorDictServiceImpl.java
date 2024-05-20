package com.rexel.laocz.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.CustomException;
import com.rexel.laocz.domain.LaoczLiquorDict;
import com.rexel.laocz.enums.LaoczLiquorDictTypeEnum;
import com.rexel.laocz.mapper.LaoczLiquorDictMapper;
import com.rexel.laocz.service.ILaoczLiquorDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 酒品字典Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorDictServiceImpl extends ServiceImpl<LaoczLiquorDictMapper, LaoczLiquorDict> implements ILaoczLiquorDictService {
    /**
     * 查询酒品字典列表
     *
     * @param laoczLiquorDict 酒品字典
     * @return 酒品字典
     */
    @Override
    public List<LaoczLiquorDict> selectLaoczLiquorDictList(LaoczLiquorDict laoczLiquorDict) {
        return baseMapper.selectLaoczLiquorDictList(laoczLiquorDict);
    }

    /**
     * 酒液等级 香型名称 下拉
     * @return
     */
    @Override
    public List<String> dropDownLiquor(LaoczLiquorDict laoczLiquorDict) {

        List<LaoczLiquorDict> laoczLiquorDicts = this.selectLaoczLiquorDictList(laoczLiquorDict);

        ArrayList<String> liquorVos = new ArrayList<>();

        for (LaoczLiquorDict liquorDict : laoczLiquorDicts) {

            String wineDcitInfo = liquorDict.getWineDcitInfo();

            liquorVos.add(wineDcitInfo);

        }

        return liquorVos;
    }
    /**
     * 新增酒品字典
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addLiquorDict(List<LaoczLiquorDict> laoczLiquorDicts) {
        if (CollectionUtil.isEmpty(laoczLiquorDicts)) {
            return true;
        }
        //type不能为空
        if (laoczLiquorDicts.stream().anyMatch(laoczLiquorDict -> laoczLiquorDict.getDictType() == null)) {
            throw new CustomException("程序异常，请联系管理员");
        }
        Map<Long, List<LaoczLiquorDict>> collect = laoczLiquorDicts.stream().collect(Collectors.groupingBy(LaoczLiquorDict::getDictType));
        for (Map.Entry<Long, List<LaoczLiquorDict>> longListEntry : collect.entrySet()) {
            List<LaoczLiquorDict> value = longListEntry.getValue();
            //判断wineDcitInfo是否重复
            List<String> wineDcitInfos = value.stream().map(LaoczLiquorDict::getWineDcitInfo).collect(Collectors.toList());
            if (wineDcitInfos.size() != wineDcitInfos.stream().distinct().count()) {
                if (longListEntry.getKey().equals(LaoczLiquorDictTypeEnum.WINE_INFO.getCode())) {
                    throw new CustomException("酒品信息重复");
                }
                if (longListEntry.getKey().equals(LaoczLiquorDictTypeEnum.WINE_GRADE_INFO.getCode())) {
                    throw new CustomException("酒业等级信息重复");
                }
            }
            this.lambdaUpdate().eq(LaoczLiquorDict::getDictType, longListEntry.getKey()).remove();
        }
        return this.saveBatch(laoczLiquorDicts);
    }




}
