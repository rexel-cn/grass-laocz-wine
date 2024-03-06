package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import com.rexel.laocz.mapper.LaoczLiquorRuleInfoMapper;
import com.rexel.laocz.service.ILaoczLiquorRuleInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 酒液批次存储报警规则信息Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorRuleInfoServiceImpl extends ServiceImpl<LaoczLiquorRuleInfoMapper, LaoczLiquorRuleInfo> implements ILaoczLiquorRuleInfoService {


    /**
     * 查询酒液批次存储报警规则信息列表
     *
     * @param laoczLiquorRuleInfo 酒液批次存储报警规则信息
     * @return 酒液批次存储报警规则信息
     */
    @Override
    public List<LaoczLiquorRuleInfo> selectLaoczLiquorRuleInfoList(LaoczLiquorRuleInfo laoczLiquorRuleInfo) {
        return baseMapper.selectLaoczLiquorRuleInfoList(laoczLiquorRuleInfo);
    }

}
