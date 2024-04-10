package com.rexel.laocz.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczLiquorDict;
import com.rexel.laocz.mapper.LaoczLiquorDictMapper;
import com.rexel.laocz.service.ILaoczLiquorDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        List<LaoczLiquorDict> laoczLiquorDicts = baseMapper.selectLaoczLiquorDictList(laoczLiquorDict);
        return laoczLiquorDicts;
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
        if (laoczLiquorDicts != null && laoczLiquorDicts.size()>0){

            QueryWrapper<LaoczLiquorDict> wrapper = new QueryWrapper<>();
            wrapper.eq("dict_type",laoczLiquorDicts.get(0).getDictType());
            this.remove(wrapper);
            return this.saveBatch(laoczLiquorDicts);
        }
        return false;
    }




}
