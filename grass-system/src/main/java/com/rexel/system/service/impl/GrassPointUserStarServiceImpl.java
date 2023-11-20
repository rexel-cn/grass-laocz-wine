package com.rexel.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.domain.GrassPointUserStar;
import com.rexel.system.domain.dto.GrassPointUserStarDTO;
import com.rexel.system.mapper.GrassPointUserStarMapper;
import com.rexel.system.service.IGrassPointUserStarService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author grass-service
 * @date 2022-10-21
 */
@Service
public class GrassPointUserStarServiceImpl extends ServiceImpl<GrassPointUserStarMapper, GrassPointUserStar> implements IGrassPointUserStarService {


    /**
     * 查询【请填写功能名称】列表
     *
     * @param grassPointUserStar 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<GrassPointUserStar> selectGrassPointUserStarList(GrassPointUserStar grassPointUserStar) {
        return baseMapper.selectGrassPointUserStarList(grassPointUserStar);
    }

    @Override
    public Boolean top(GrassPointUserStarDTO grassPointUserStarDTO) {
        List<GrassPointUserStar> list = lambdaQuery().eq(GrassPointUserStar::getAssetId, grassPointUserStarDTO.getAssetId())
                .eq(GrassPointUserStar::getUserId, SecurityUtils.getUserId())
                .eq(GrassPointUserStar::getPointPrimaryKey, grassPointUserStarDTO.getPointPrimaryKey()).list();

        if (CollectionUtil.isNotEmpty(list)) {
            return lambdaUpdate().eq(GrassPointUserStar::getAssetId, grassPointUserStarDTO.getAssetId())
                    .eq(GrassPointUserStar::getUserId, SecurityUtils.getUserId())
                    .eq(GrassPointUserStar::getPointPrimaryKey, grassPointUserStarDTO.getPointPrimaryKey()).remove();
        }
        GrassPointUserStar grassPointUserStar = BeanUtil.copyProperties(grassPointUserStarDTO, GrassPointUserStar.class);
        grassPointUserStar.setUserId(SecurityUtils.getUserId());
        grassPointUserStar.setTopTime(new Date());
        return save(grassPointUserStar);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

}
