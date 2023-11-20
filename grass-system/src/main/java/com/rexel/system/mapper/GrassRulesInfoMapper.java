package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassRulesInfo;
import com.rexel.system.domain.vo.AlarmRulesDetailVo;
import com.rexel.system.domain.vo.RulesTypeIdVO;
import com.rexel.system.domain.vo.RulesVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报警规则Mapper接口
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Repository
public interface GrassRulesInfoMapper extends BaseMapper<GrassRulesInfo> {
    /**
     * 查询报警规则列表
     *
     * @param grassRulesInfo 报警规则
     * @return 报警规则集合
     */
    List<RulesVO> selectGrassRulesInfoList(GrassRulesInfo grassRulesInfo);

    /**
     * 获取通知模板
     *
     * @param list list
     * @return 结果
     */
    List<RulesVO> selectNoticeTemplateName(@Param("list") List<Long> list);

    /**
     * 根据设备id查询数量
     *
     * @param deviceId 设备id
     * @return 结果
     */
    @InterceptorIgnore(tenantLine = "on")
    Integer selectCountByDeviceId(String deviceId);

    /**
     * 查询规则详情
     *
     * @param rulesId rulesId
     * @return 结果
     */
    @InterceptorIgnore(tenantLine = "on")
    AlarmRulesDetailVo getAlarmRulesDetailVoByRulesId(String rulesId);

    /**
     * 查询规则
     *
     * @param id id
     * @return 结果
     */
    RulesTypeIdVO getRulesInfoById(Long id);

    /**
     * 删除租户报警规则
     *
     * @param tenantId tenantId
     * @return 结果
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
