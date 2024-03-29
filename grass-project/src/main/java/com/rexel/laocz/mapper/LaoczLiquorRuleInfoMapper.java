package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import com.rexel.laocz.domain.vo.LiquorRuleInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒液批次存储报警规则信息Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczLiquorRuleInfoMapper extends BaseMapper<LaoczLiquorRuleInfo> {
    /**
     * 查询酒液批次存储报警规则信息列表
     *
     * @param laoczLiquorRuleInfo 酒液批次存储报警规则信息
     * @return 酒液批次存储报警规则信息集合
     */
    List<LaoczLiquorRuleInfo> selectLaoczLiquorRuleInfoList(LaoczLiquorRuleInfo laoczLiquorRuleInfo);

    /**
     * 批量新增酒液批次存储报警规则信息
     *
     * @param laoczLiquorRuleInfoList 酒液批次存储报警规则信息列表
     * @return 结果
     */
    int batchLaoczLiquorRuleInfo(List<LaoczLiquorRuleInfo> laoczLiquorRuleInfoList);
    /**
     * 酒液批次下拉
     * @return
     */
    List<String> dropDown();

    List<LiquorRuleInfoVo> selectLaoczLiquorRuleInfoListVo(LaoczLiquorRuleInfo laoczLiquorRuleInfo);
}
