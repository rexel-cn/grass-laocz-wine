package com.rexel.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.domain.GrassNoticeDingding;
import com.rexel.system.domain.dto.GrassNoticeDingdingDTO;
import com.rexel.system.mapper.GrassNoticeDingdingMapper;
import com.rexel.system.service.IGrassNoticeDingdingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通知钉钉配置Service业务层处理
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Service
public class GrassNoticeDingdingServiceImpl extends ServiceImpl<GrassNoticeDingdingMapper, GrassNoticeDingding> implements IGrassNoticeDingdingService {


    /**
     * 查询通知钉钉配置列表
     *
     * @param grassNoticeDingding 通知钉钉配置
     * @return 通知钉钉配置
     */
    @Override
    public List<GrassNoticeDingding> selectGrassNoticeDingdingList(GrassNoticeDingding grassNoticeDingding) {
        return baseMapper.selectGrassNoticeDingdingList(grassNoticeDingding);
    }

    @Override
    public List<GrassNoticeDingding> selectOneByTenantId() {
        List<GrassNoticeDingding> list = lambdaQuery()
                .eq(GrassNoticeDingding::getTenantId, SecurityUtils.getTenantId()).list();
        if (list.isEmpty()) {
            list.add(new GrassNoticeDingding());
            return list;
        }
        return list;
    }

    @Override
    public Boolean updateOrInsert(GrassNoticeDingdingDTO grassNoticeDingdingDTO) {
        List<Long> list = lambdaQuery()
                .eq(GrassNoticeDingding::getTenantId, SecurityUtils.getTenantId()).list().stream().map(GrassNoticeDingding::getId).collect(Collectors.toList());
        List<GrassNoticeDingding> dingDingList = grassNoticeDingdingDTO.getDingDingList();
        Map<Long, GrassNoticeDingding> newList = dingDingList.stream().collect(Collectors.toMap(GrassNoticeDingding::getId, Function.identity()));
        List<Long> deleteList = new ArrayList<>();
        for (Long id : list) {
            if (ObjectUtil.isNull(newList.get(id))) {
                deleteList.add(id);
            }
        }
        dingDingList.forEach(grassNoticeDingding -> grassNoticeDingding.setNoticeConfigId(grassNoticeDingdingDTO.getNoticeConfigId()));
        removeByIds(deleteList);
        return saveOrUpdateBatch(dingDingList);
    }

    @Override
    public List<GrassNoticeDingding> selectByTenantId(String tenantId) {
        return baseMapper.selectByTenantId(tenantId);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {

        return baseMapper.deleteByTenantId(tenantId);
    }

}
