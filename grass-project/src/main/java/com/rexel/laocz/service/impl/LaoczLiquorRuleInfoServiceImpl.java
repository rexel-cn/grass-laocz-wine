package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import com.rexel.laocz.mapper.LaoczLiquorRuleInfoMapper;
import com.rexel.laocz.service.ILaoczLiquorRuleInfoService;
import com.rexel.laocz.vo.LiquorRuleInfoVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<LiquorRuleInfoVo> selectLaoczLiquorRuleInfoList(LaoczLiquorRuleInfo laoczLiquorRuleInfo) {

        List<LaoczLiquorRuleInfo> laoczLiquorRuleInfos = baseMapper.selectLaoczLiquorRuleInfoList(laoczLiquorRuleInfo);

        List<LiquorRuleInfoVo> list = laoczLiquorRuleInfos.stream().map((item) -> {
            LiquorRuleInfoVo liquorRuleInfoVo = new LiquorRuleInfoVo();

            BeanUtil.copyProperties(item, liquorRuleInfoVo);

            String liquorRuleNotifyUser = item.getLiquorRuleNotifyUser();

            String[] split = liquorRuleNotifyUser.split(",");

            int length = split.length;

            liquorRuleInfoVo.setCount(length);

            return liquorRuleInfoVo;
        }).collect(Collectors.toList());


        return list;
    }

}
