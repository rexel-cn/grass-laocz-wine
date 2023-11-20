package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.constant.UserConstants;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.domain.GrassNoticeConfig;
import com.rexel.system.domain.dto.GrassNoticeConfigDTO;
import com.rexel.system.domain.vo.GrassNoticeConfigVO;
import com.rexel.system.mapper.GrassNoticeConfigMapper;
import com.rexel.system.service.IGrassNoticeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知配置主Service业务层处理
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Service
public class GrassNoticeConfigServiceImpl extends ServiceImpl<GrassNoticeConfigMapper, GrassNoticeConfig> implements IGrassNoticeConfigService {


    @Autowired
    private GrassNoticeConfigMapper grassNoticeConfigMapper;

    /**
     * 查询通知配置主列表
     *
     * @param grassNoticeConfig 通知配置主
     * @return 通知配置主
     */
    @Override
    public List<GrassNoticeConfigVO> selectGrassNoticeConfigList(GrassNoticeConfig grassNoticeConfig) {
        List<GrassNoticeConfigVO> grassNoticeConfigVOS = grassNoticeConfigMapper.selectList();
        List<GrassNoticeConfigVO> collect = grassNoticeConfigVOS.stream().filter(grassNoticeConfigVO -> grassNoticeConfigVO.getId() == null).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            List<GrassNoticeConfig> list = new ArrayList<>(UserConstants.SEND_TYPE_COUNT);
            collect.forEach(grassNoticeConfigVO -> {
                GrassNoticeConfig noticeConfig = new GrassNoticeConfig();
                noticeConfig.setPushType(grassNoticeConfigVO.getPushType());
                noticeConfig.setTenantId(SecurityUtils.getTenantId());
                list.add(noticeConfig);
            });
            saveBatch(list);
            grassNoticeConfigVOS = grassNoticeConfigMapper.selectList();
        }
        return grassNoticeConfigVOS;
    }

    /**
     * 修改是否开通状态
     *
     * @param grassNoticeConfigDTO
     * @return
     */
    @Override
    public boolean updateOpenStatusById(GrassNoticeConfigDTO grassNoticeConfigDTO) {
        return lambdaUpdate()
                .eq(GrassNoticeConfig::getId, grassNoticeConfigDTO.getId())
                .set(true, GrassNoticeConfig::getIsOpen, grassNoticeConfigDTO.getIsOpen())
                .update(new GrassNoticeConfig());
    }

    @Override
    public GrassNoticeConfig selectOneByPushType(GrassNoticeConfig grassNoticeConfig) {
        return grassNoticeConfigMapper.selectOneByPushType(grassNoticeConfig);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

}
