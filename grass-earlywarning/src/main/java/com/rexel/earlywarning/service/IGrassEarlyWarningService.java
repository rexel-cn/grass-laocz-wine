package com.rexel.earlywarning.service;

import java.util.List;
import com.rexel.earlywarning.domain.GrassEarlyWarning;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmData;

/**
 * 预警规则Service接口
 *
 * @author admin
 * @date 2022-01-14
 */
public interface IGrassEarlyWarningService {
    /**
     * 查询预警规则
     *
     * @param rulesId 预警规则ID
     * @return 预警规则
     */
    GrassEarlyWarning selectGrassEarlyWarningById(Long rulesId);

    /**
     * 查询预警规则列表
     *
     * @param grassEarlyWarning 预警规则
     * @return 预警规则集合
     */
    List<GrassEarlyWarning> selectGrassEarlyWarningList(GrassEarlyWarning grassEarlyWarning);

    /**
     * 新增预警规则
     *
     * @param grassEarlyWarning 预警规则
     */
    void insertGrassEarlyWarning(GrassEarlyWarning grassEarlyWarning);

    /**
     * 修改预警规则
     *
     * @param grassEarlyWarning 预警规则
     */
    void updateGrassEarlyWarning(GrassEarlyWarning grassEarlyWarning);

    /**
     * 修改预警规则
     *
     * @param grassEarlyWarning 预警规则
     */
    void updateGrassEarlyWarningStatus(GrassEarlyWarning grassEarlyWarning);

    /**
     * 修改预警规则模板状态
     *
     * @param grassEarlyWarning 预警规则
     */
    void updateGrassEarlyWarningTemplate(GrassEarlyWarning grassEarlyWarning);

    /**
     * 删除预警规则信息
     *
     * @param rulesId 预警规则ID
     */
    void deleteGrassEarlyWarningById(Long rulesId);

    /**
     * 删除预警规则信息
     *
     * @param tenantId tenantId
     */
    void deleteGrassEarlyWarningByTenantId(String tenantId);

    /**
     * 提交提交所有任务
     */
    void submitAllJob();

    /**
     * 写入预警通知数据
     *
     * @param alarmData alarmData
     */
    void writeAlarmData(GrassEarlyWarningAlarmData alarmData);

    /**
     * 检查设备是否正在被使用
     *
     * @param deviceId deviceId
     */
    void checkDeviceCanDelete(String deviceId);

    /**
     * 刷新MQTT订阅
     */
    void refreshMqttListener();
}
