package com.rexel.bpm.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.bpm.domain.dal.BpmProcessDefinitionInfoDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;


@Mapper
public interface BpmProcessDefinitionInfoMapper extends BaseMapper<BpmProcessDefinitionInfoDO> {
    default List<BpmProcessDefinitionInfoDO> selectListByProcessDefinitionIds(Collection<String> processDefinitionIds) {
       return selectList(new LambdaQueryWrapper<BpmProcessDefinitionInfoDO>()
               .in(BpmProcessDefinitionInfoDO::getProcessDefinitionId, processDefinitionIds));
    }

    default BpmProcessDefinitionInfoDO selectByProcessDefinitionId(String processDefinitionId) {
        return selectOne(new LambdaQueryWrapper<BpmProcessDefinitionInfoDO>()
                .eq(BpmProcessDefinitionInfoDO::getProcessDefinitionId, processDefinitionId));
    }

}
