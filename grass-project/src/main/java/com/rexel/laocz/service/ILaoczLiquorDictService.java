package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczLiquorDict;
import com.rexel.laocz.domain.vo.LiquorVo;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 酒品字典Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczLiquorDictService extends IService<LaoczLiquorDict> {

    /**
     * 查询酒品字典列表
     *
     * @param laoczLiquorDict 酒品字典
     * @return 酒品字典集合
     */
    List<LaoczLiquorDict> selectLaoczLiquorDictList(LaoczLiquorDict laoczLiquorDict);

    /**
     * 酒液等级 香型名称 下拉
     * @return
     */
    List<String> dropDownLiquor(LaoczLiquorDict laoczLiquorDict);
    /**
     * 新增酒品字典
     */
    boolean addLiquorDict(List<LaoczLiquorDict> laoczLiquorDicts);

}
