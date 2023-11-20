package com.rexel.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.CustomException;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.sign.Md5Utils;
import com.rexel.system.domain.GrassAssetName;
import com.rexel.system.domain.GrassNoticeTemplate;
import com.rexel.system.domain.GrassRuleNotice;
import com.rexel.system.domain.GrassRulesInfo;
import com.rexel.system.domain.dto.PulseRulesDTO;
import com.rexel.system.domain.dto.RulesExcelDTO;
import com.rexel.system.domain.vo.AlarmRulesDetailVo;
import com.rexel.system.domain.vo.RulesTypeIdVO;
import com.rexel.system.domain.vo.RulesVO;
import com.rexel.system.mapper.GrassRulesInfoMapper;
import com.rexel.system.service.IGrassAssetService;
import com.rexel.system.service.IGrassNoticeTemplateService;
import com.rexel.system.service.IGrassRuleNoticeService;
import com.rexel.system.service.IGrassRulesInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 报警规则Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Service
public class GrassRulesInfoServiceImpl extends ServiceImpl<GrassRulesInfoMapper, GrassRulesInfo> implements IGrassRulesInfoService {


    @Autowired
    private IGrassNoticeTemplateService iGrassNoticeTemplateService;
    @Autowired
    private IGrassRuleNoticeService iGrassRuleNoticeService;

    @Autowired
    private IGrassAssetService iGrassAssetService;

    /**
     * 查询报警规则列表
     *
     * @param grassRulesInfo 报警规则
     * @return 报警规则
     */
    @Override
    public List<RulesVO> selectGrassRulesInfoList(GrassRulesInfo grassRulesInfo) {
        List<RulesVO> rulesList = baseMapper.selectGrassRulesInfoList(grassRulesInfo);
        List<Long> collect = rulesList.stream().map(GrassRulesInfo::getId).collect(Collectors.toList());
        Map<Long, RulesVO> map = baseMapper.selectNoticeTemplateName(collect).stream()
                .collect(Collectors.toMap(RulesVO::getId, Function.identity()));
        rulesList.forEach(rulesVO -> {
            RulesVO vo = map.get(rulesVO.getId());
            rulesVO.setNoticeTemplateNameList(vo.getNoticeTemplateNameList());
            rulesVO.setNoticeTemplateIds(vo.getNoticeTemplateIds());
        });
        templateJoin(rulesList);
        return rulesList;
    }

    /**
     * 根据设备id查询数量
     *
     * @param deviceId 设备id
     * @return 结果
     */
    @Override
    public Integer selectCountByDeviceId(String deviceId) {
        return baseMapper.selectCountByDeviceId(deviceId);
    }

    /**
     * 创建
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(PulseRulesDTO pulseRulesAddUpdateDTO) {
        GrassRulesInfo grassRulesInfo = BeanUtil.copyProperties(pulseRulesAddUpdateDTO, GrassRulesInfo.class);
        //验证模板是否存在
        checkTemplate(pulseRulesAddUpdateDTO.getNoticeTemplateIds());
        //md5
        String md5Hash = md5Hash(pulseRulesAddUpdateDTO);
        grassRulesInfo.setRulesUnique(md5Hash);
        //规则保存
        save(grassRulesInfo);
        //规则通知保存
        List<GrassRuleNotice> ruleNoticeList = new ArrayList<>();
        for (Long noticeTemplateId : pulseRulesAddUpdateDTO.getNoticeTemplateIds()) {
            GrassRuleNotice grassRuleNotice = new GrassRuleNotice();
            grassRuleNotice.setNoticeTemplateId(noticeTemplateId);
            grassRuleNotice.setRulesId(grassRulesInfo.getId());
            ruleNoticeList.add(grassRuleNotice);
        }
        iGrassRuleNoticeService.saveBatch(ruleNoticeList);
        return true;
    }

    private void checkTemplate(List<Long> noticeTemplateIds) {
        if (CollectionUtil.isEmpty(noticeTemplateIds)) {
            throw new CustomException("通知模板必填项");
        }
        List<GrassNoticeTemplate> list = iGrassNoticeTemplateService.lambdaQuery().in(GrassNoticeTemplate::getId, noticeTemplateIds).list();
        if (CollectionUtil.isEmpty(list) || list.size() != noticeTemplateIds.size()) {
            throw new CustomException("通知模板不存在");
        }
    }

    /**
     * 修改
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(PulseRulesDTO pulseRulesAddUpdateDTO) {
        //验证模板是否存在
        checkTemplate(pulseRulesAddUpdateDTO.getNoticeTemplateIds());
        //修改规则
        updateById(pulseRulesAddUpdateDTO);
        //修改规则通知
        List<Long> list = pulseRulesAddUpdateDTO.getNoticeTemplateIds();
        if (CollectionUtil.isNotEmpty(list)) {
            List<GrassRuleNotice> ruleNoticeList = new ArrayList<>();
            for (Long noticeTemplateId : list) {
                GrassRuleNotice grassRuleNotice = new GrassRuleNotice();
                grassRuleNotice.setNoticeTemplateId(noticeTemplateId);
                grassRuleNotice.setRulesId(pulseRulesAddUpdateDTO.getId());
                ruleNoticeList.add(grassRuleNotice);
            }

            iGrassRuleNoticeService.lambdaUpdate().eq(GrassRuleNotice::getRulesId, pulseRulesAddUpdateDTO.getId()).remove();
            iGrassRuleNoticeService.saveBatch(ruleNoticeList);
        }
        return true;
    }

    /**
     * 删除
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(PulseRulesDTO pulseRulesAddUpdateDTO) {
        removeById(pulseRulesAddUpdateDTO.getId());
        iGrassRuleNoticeService.lambdaUpdate().eq(GrassRuleNotice::getRulesId, pulseRulesAddUpdateDTO.getId()).remove();
        return true;
    }

    /**
     * 修改状态
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @Override
    public Boolean changeStatus(PulseRulesDTO pulseRulesAddUpdateDTO) {
        return lambdaUpdate().eq(GrassRulesInfo::getId, pulseRulesAddUpdateDTO.getId())
                .set(GrassRulesInfo::getIsEnable, pulseRulesAddUpdateDTO.getIsEnable()).update();
    }

    /**
     * 查询当前租户的规则
     *
     * @return 结果
     */
    @Override
    public List<RulesExcelDTO> export() {
        return BeanUtil.copyToList(selectGrassRulesInfoList(null), RulesExcelDTO.class);
    }

    /**
     * 导入
     *
     * @param list list
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importRuleVo(List<RulesExcelDTO> list) {
        //根据租户删除
        lambdaUpdate().eq(GrassRulesInfo::getTenantId, SecurityUtils.getTenantId()).remove();
        iGrassRuleNoticeService.lambdaUpdate().eq(GrassRuleNotice::getTenantId, SecurityUtils.getTenantId()).remove();
        if (!list.isEmpty()) {
            List<RulesVO> rulesList = BeanUtil.copyToList(list, RulesVO.class);
            List<GrassAssetName> grassAssetNames = iGrassAssetService.selectIdByName(rulesList);
            if (CollectionUtil.isNotEmpty(grassAssetNames)) {
                Map<String, String> map = grassAssetNames.stream().collect(Collectors.toMap(grassAssetName -> {
                    String assetName = grassAssetName.getAssetName();
                    String assetTypeName = grassAssetName.getAssetTypeName();
                    return assetName + assetTypeName;
                }, GrassAssetName::getAssetId));

                for (RulesVO rulesVO : rulesList) {
                    String assetId = map.get(rulesVO.getAssetName() + rulesVO.getAssetTypeName());
                    rulesVO.setAssetId(assetId);
                }
            }

            //md5
            rulesList.forEach(rulesVO -> rulesVO.setRulesUnique(md5Hash(rulesVO)));

            List<GrassRulesInfo> grassRulesInfos = BeanUtil.copyToList(rulesList, GrassRulesInfo.class);

            //保存后获取id
            saveBatch(grassRulesInfos);

            Map<String, GrassRulesInfo> collect =
                    grassRulesInfos.stream().collect(Collectors.toMap(GrassRulesInfo::getRulesUnique, Function.identity()));
            //根据模板名字反查id
            convert(rulesList);
            //组装管理表数据
            List<GrassRuleNotice> ruleNoticeList = new ArrayList<>();
            for (RulesVO rulesVO : rulesList) {
                String rulesUnique = rulesVO.getRulesUnique();
                GrassRulesInfo grassRulesInfo = collect.get(rulesUnique);
                List<Long> noticeTemplateIds = rulesVO.getNoticeTemplateIds();
                if (CollectionUtil.isNotEmpty(noticeTemplateIds)) {
                    for (Long noticeTemplateId : noticeTemplateIds) {
                        GrassRuleNotice grassRuleNotice = new GrassRuleNotice(grassRulesInfo.getId(), noticeTemplateId, null, null);
                        ruleNoticeList.add(grassRuleNotice);
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(ruleNoticeList)) {
                iGrassRuleNoticeService.saveBatch(ruleNoticeList);
            }
        }
        return true;
    }

    /**
     * 根据规则id查询通知范围，通知方式
     *
     * @param rulesId rulesId
     * @return 结果
     */
    @Override
    public AlarmRulesDetailVo getAlarmRulesDetailVoByRulesId(String rulesId) {
        return baseMapper.getAlarmRulesDetailVoByRulesId(rulesId);
    }

    /**
     * 查询报警规则
     *
     * @param id id
     * @return 结果
     */
    @Override
    public RulesTypeIdVO getRulesInfoById(Long id) {
        RulesTypeIdVO rulesInfoById = baseMapper.getRulesInfoById(id);
        if (ObjectUtil.isEmpty(rulesInfoById)) {
            throw new CustomException("id不存在");
        }

        List<RulesTypeIdVO> list = new ArrayList<>();
        list.add(rulesInfoById);
        List<Long> collect = list.stream().map(GrassRulesInfo::getId).collect(Collectors.toList());
        Map<Long, RulesVO> map = baseMapper.selectNoticeTemplateName(collect).stream()
                .collect(Collectors.toMap(RulesVO::getId, Function.identity()));
        list.forEach(rulesVO -> {
            RulesVO vo = map.get(rulesVO.getId());
            rulesVO.setNoticeTemplateNameList(vo.getNoticeTemplateNameList());
            rulesVO.setNoticeTemplateIds(vo.getNoticeTemplateIds());
        });
        return rulesInfoById;
    }

    /**
     * 删除租户报警
     *
     * @param tenantId tenantId
     * @return 结果
     */
    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

    /**
     * 修改模板状态
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     */
    @Override
    public void updateTemplateStatus(PulseRulesDTO pulseRulesAddUpdateDTO) {
        Long rulesId = pulseRulesAddUpdateDTO.getId();
        if (rulesId == null) {
            throw new ServiceException("报警规则ID为空");
        }

        // 查询报警规则
        GrassRulesInfo info = baseMapper.selectById(rulesId);
        if (info == null) {
            throw new ServiceException("指定的报警规则ID不存在");
        }

        // 更新模板状态
        info.setIsTemplate(pulseRulesAddUpdateDTO.getIsTemplate());
        info.setUpdateBy(SecurityUtils.getUsername());
        info.setUpdateTime(new Date());
        baseMapper.updateById(info);
    }

    private void templateJoin(List<RulesVO> rulesList) {
        rulesList.forEach(rulesVO -> {
            List<String> noticeTemplateNameList = rulesVO.getNoticeTemplateNameList();
            if (CollectionUtil.isNotEmpty(noticeTemplateNameList)) {
                rulesVO.setNoticeTemplateName(String.join(",", noticeTemplateNameList));
            }
        });
    }

    private String md5Hash(GrassRulesInfo info) {
        String join = String.join(",", info.getDeviceId()
                , info.getPointId(), info.getPointJudge()
                , info.getPointValue().toString());
        return Md5Utils.hash(join);
    }

    private void convert(List<RulesVO> ruleVoList) {
        // 根据模板名字反查id，新增时用
        Set<String> templateNames = new HashSet<>();
        ruleVoList.forEach(ruleVo -> {
            String noticeTemplateName = ruleVo.getNoticeTemplateName();
            if (StringUtils.isNotEmpty(noticeTemplateName)) {
                String[] split = noticeTemplateName.split(",");
                templateNames.addAll(Arrays.asList(split));
            }
        });
        if (!templateNames.isEmpty()) {
            Map<String, Long> longMap = iGrassNoticeTemplateService
                    .lambdaQuery().in(GrassNoticeTemplate::getNoticeTemplateName, templateNames)
                    .list().stream().collect(Collectors.toMap(GrassNoticeTemplate::getNoticeTemplateName,
                            GrassNoticeTemplate::getId));

            ruleVoList.forEach(ruleVo -> {
                String noticeTemplateName = ruleVo.getNoticeTemplateName();
                if (StringUtils.isNotEmpty(noticeTemplateName)) {
                    String[] split = noticeTemplateName.split(",");
                    for (String s : split) {
                        List<Long> noticeTemplateIds = ruleVo.getNoticeTemplateIds();
                        if (noticeTemplateIds == null) {
                            noticeTemplateIds = new ArrayList<>();
                        }
                        Long aLong = longMap.get(s);
                        noticeTemplateIds.add(aLong);
                        ruleVo.setNoticeTemplateIds(noticeTemplateIds);
                    }
                }
            });
        }
    }
}
