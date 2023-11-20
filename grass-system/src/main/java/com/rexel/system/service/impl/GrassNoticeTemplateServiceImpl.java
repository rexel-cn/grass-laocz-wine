package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.domain.GrassNoticeMode;
import com.rexel.system.domain.GrassNoticeScope;
import com.rexel.system.domain.GrassNoticeTemplate;
import com.rexel.system.domain.dto.GrassNoticeScopeDTO;
import com.rexel.system.domain.dto.GrassNoticeTemplateDTO;
import com.rexel.system.domain.vo.GrassNoticeTemplateVO;
import com.rexel.system.domain.vo.NoticeScopeDropDown;
import com.rexel.system.mapper.GrassNoticeTemplateMapper;
import com.rexel.system.service.IGrassNoticeModeService;
import com.rexel.system.service.IGrassNoticeScopeService;
import com.rexel.system.service.IGrassNoticeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通知配置模板Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Service
public class GrassNoticeTemplateServiceImpl extends ServiceImpl<GrassNoticeTemplateMapper, GrassNoticeTemplate> implements IGrassNoticeTemplateService {
    @Autowired
    private IGrassNoticeModeService iGrassNoticeModeService;
    @Autowired
    private IGrassNoticeScopeService iGrassNoticeScopeService;


    /**
     * 查询通知配置模板列表
     *
     * @param grassNoticeTemplate 通知配置模板
     * @return 通知配置模板
     */
    @Override
    public List<GrassNoticeTemplateVO> selectGrassNoticeTemplateList(GrassNoticeTemplate grassNoticeTemplate) {
        return baseMapper.selectGrassNoticeTemplateList(grassNoticeTemplate);
    }

    @Override
    public GrassNoticeTemplateVO selectGrassNoticeTemplateById(GrassNoticeTemplate grassNoticeTemplate) {
        List<GrassNoticeTemplateVO> grassNoticeTemplateVOS = baseMapper.selectGrassNoticeTemplateList(grassNoticeTemplate);
        if (grassNoticeTemplateVOS.isEmpty()) {
            return new GrassNoticeTemplateVO();
        }
        GrassNoticeTemplateVO grassNoticeTemplateVO = grassNoticeTemplateVOS.get(0);
        List<GrassNoticeScopeDTO> grassNoticeScopeList = grassNoticeTemplateVO.getGrassNoticeScopeList();
        Map<Long, GrassNoticeScopeDTO> collect = grassNoticeScopeList.stream()
                .collect(Collectors.toMap(GrassNoticeScope::getNoticeScope, Function.identity()));
        List<NoticeScopeDropDown> noticeScopeDropDowns = iGrassNoticeScopeService.noticeScopeList();
        for (NoticeScopeDropDown noticeScopeDropDown : noticeScopeDropDowns) {
            if (!collect.containsKey(Long.parseLong(noticeScopeDropDown.getNoticeScope()))) {
                GrassNoticeScopeDTO grassNoticeScopeDTO = new GrassNoticeScopeDTO();
                grassNoticeScopeDTO.setNoticeScope(Long.parseLong(noticeScopeDropDown.getNoticeScope()));
                grassNoticeScopeDTO.setNoticeObjectArray(new ArrayList<>());
                grassNoticeScopeList.add(grassNoticeScopeDTO);
            }
        }
        return grassNoticeTemplateVO;
    }

    /**
     * 创建通知模板
     *
     * @param grassNoticeScopeDTOS 添加对象
     * @return
     */
    @Override
    @Transactional
    public Boolean createGrassNoticeTemplate(GrassNoticeTemplateDTO grassNoticeScopeDTOS) {
        save(grassNoticeScopeDTOS);
        List<GrassNoticeMode> grassNoticeModes = convertGrassNoticeModeList(grassNoticeScopeDTOS);
        List<GrassNoticeScope> grassNoticeScopes = convertGrassNoticeScopeList(grassNoticeScopeDTOS);
        grassNoticeScopeDTOS.setTenantId(SecurityUtils.getTenantId());
        iGrassNoticeModeService.saveBatch(grassNoticeModes);
        iGrassNoticeScopeService.saveBatch(grassNoticeScopes);
        return true;
    }

    /**
     * 修改通知模板
     *
     * @param grassNoticeScopeDTOS 修改对象
     * @return
     */
    @Override
    @Transactional
    public Boolean updateGrassNoticeTemplate(GrassNoticeTemplateDTO grassNoticeScopeDTOS) {
        Long id = grassNoticeScopeDTOS.getId();
        List<GrassNoticeScope> grassNoticeScopes = convertGrassNoticeScopeList(grassNoticeScopeDTOS);
        List<GrassNoticeMode> grassNoticeModes = convertGrassNoticeModeList(grassNoticeScopeDTOS);
        updateById(grassNoticeScopeDTOS);
        iGrassNoticeScopeService.lambdaUpdate().eq(GrassNoticeScope::getNoticeTemplateId, id).remove();
        iGrassNoticeScopeService.saveBatch(grassNoticeScopes);
        iGrassNoticeModeService.lambdaUpdate().eq(GrassNoticeMode::getNoticeTemplateId, id).remove();
        iGrassNoticeModeService.saveBatch(grassNoticeModes);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteGrassNoticeTemplate(GrassNoticeTemplateDTO grassNoticeScopeDTOS) {
        Long id = grassNoticeScopeDTOS.getId();
        removeById(id);
        iGrassNoticeScopeService.lambdaUpdate().eq(GrassNoticeScope::getNoticeTemplateId, id).remove();
        iGrassNoticeModeService.lambdaUpdate().eq(GrassNoticeMode::getNoticeTemplateId, id).remove();
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteByTenantId(String tenantId) {
        iGrassNoticeScopeService.deleteByTenantId(tenantId);
        iGrassNoticeModeService.deleteByTenantId(tenantId);
        return baseMapper.deleteByTenantId(tenantId);
    }

    /**
     * 通知模板对应范围
     *
     * @param grassNoticeScopeDTOS
     * @return
     */
    private List<GrassNoticeScope> convertGrassNoticeScopeList(GrassNoticeTemplateDTO grassNoticeScopeDTOS) {
        List<GrassNoticeScopeDTO> grassNoticeScopeList = grassNoticeScopeDTOS.getGrassNoticeScopeList();
        List<GrassNoticeScope> list = new ArrayList<>();
        for (GrassNoticeScopeDTO grassNoticeScopeDTO : grassNoticeScopeList) {
            Long noticeScope = grassNoticeScopeDTO.getNoticeScope();
            List<Long> noticeObjectList = grassNoticeScopeDTO.getNoticeObjectArray();
            for (Long s : noticeObjectList) {
                GrassNoticeScope grassNoticeScope = new GrassNoticeScope();
                grassNoticeScope.setNoticeTemplateId(grassNoticeScopeDTOS.getId());
                grassNoticeScope.setNoticeScope(noticeScope);
                grassNoticeScope.setNoticeObject(s);
                list.add(grassNoticeScope);
            }
        }
        return list;
    }
    /**
     * 通知模板对应方式
     *
     * @param grassNoticeScopeDTOS
     * @return
     */
    private List<GrassNoticeMode> convertGrassNoticeModeList(GrassNoticeTemplateDTO grassNoticeScopeDTOS) {
        List<Long> modeArray = grassNoticeScopeDTOS.getModeArray();
        List<GrassNoticeMode> list = new ArrayList<>(modeArray.size());
        for (Long s : modeArray) {
            GrassNoticeMode grassNoticeMode = new GrassNoticeMode();
            grassNoticeMode.setNoticeTemplateId(grassNoticeScopeDTOS.getId());
            grassNoticeMode.setNoticeMode(s);
            list.add(grassNoticeMode);
        }
        return list;
    }
}
