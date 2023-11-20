package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassRulesInfo;
import com.rexel.system.domain.dto.PulseRulesDTO;
import com.rexel.system.domain.dto.RulesExcelDTO;
import com.rexel.system.domain.vo.AlarmRulesDetailVo;
import com.rexel.system.domain.vo.RulesTypeIdVO;
import com.rexel.system.domain.vo.RulesVO;

import java.util.List;

/**
 * 报警规则Service接口
 *
 * @author grass-service
 * @date 2022-08-16
 */
public interface IGrassRulesInfoService extends IService<GrassRulesInfo> {

    /**
     * 查询报警规则列表
     *
     * @param grassRulesInfo 报警规则
     * @return 报警规则集合
     */
    List<RulesVO> selectGrassRulesInfoList(GrassRulesInfo grassRulesInfo);

    /**
     * 根据设备id查询数量
     *
     * @param deviceId 设备id
     * @return 结果
     */
    Integer selectCountByDeviceId(String deviceId);

    /**
     * 创建
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    Boolean create(PulseRulesDTO pulseRulesAddUpdateDTO);

    /**
     * 修改
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    Boolean update(PulseRulesDTO pulseRulesAddUpdateDTO);

    /**
     * 删除
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    Boolean delete(PulseRulesDTO pulseRulesAddUpdateDTO);

    /**
     * 修改状态
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    Boolean changeStatus(PulseRulesDTO pulseRulesAddUpdateDTO);

    /**
     * 查询当前租户的规则
     *
     * @return 结果
     */
    List<RulesExcelDTO> export();

    /**
     * 导入
     *
     * @param list list
     * @return 结果
     */
    Boolean importRuleVo(List<RulesExcelDTO> list);

    /**
     * 根据规则id查询通知范围，通知方式
     *
     * @param rulesId rulesId
     * @return 结果
     */
    AlarmRulesDetailVo getAlarmRulesDetailVoByRulesId(String rulesId);

    /**
     * 查询报警规则
     *
     * @param id id
     * @return 结果
     */
    RulesTypeIdVO getRulesInfoById(Long id);

    /**
     * 删除租户报警
     *
     * @param tenantId tenantId
     * @return 结果
     */
    Boolean deleteByTenantId(String tenantId);

    /**
     * 修改模板状态
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     */
    void updateTemplateStatus(PulseRulesDTO pulseRulesAddUpdateDTO);
}
