package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczLiquorDict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒品字典Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczLiquorDictMapper extends BaseMapper<LaoczLiquorDict> {
    /**
     * 查询酒品字典列表
     *
     * @param laoczLiquorDict 酒品字典
     * @return 酒品字典集合
     */
    List<LaoczLiquorDict> selectLaoczLiquorDictList(LaoczLiquorDict laoczLiquorDict);

    /**
     * 批量新增酒品字典
     *
     * @param laoczLiquorDictList 酒品字典列表
     * @return 结果
     */
    int batchLaoczLiquorDict(List<LaoczLiquorDict> laoczLiquorDictList);

}
