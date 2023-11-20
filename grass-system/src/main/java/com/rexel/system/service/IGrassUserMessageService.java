package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.system.domain.GrassUserMessage;

import java.util.List;

/**
 * 用户站内信Service接口
 *
 * @author grass-service
 * @date 2022-08-09
 */
public interface IGrassUserMessageService extends IService<GrassUserMessage> {

    /**
     * 查询用户站内信列表
     *
     * @param grassUserMessage 用户站内信
     * @return 用户站内信集合
     */
    List<GrassUserMessage> selectGrassUserMessageList(GrassUserMessage grassUserMessage);

    /**
     * 查询未读消息数量
     *
     * @param userMessage userMessage
     * @return 结果
     */
    AjaxResult selectUserMessageCount(GrassUserMessage userMessage);

    Integer selectCountByGrassUserMessage(GrassUserMessage grassUserMessage);

    Boolean confirmRead(GrassUserMessage userMessage);

    Boolean allConfirmRead(GrassUserMessage userMessage);

    Boolean deleteByTenantId(String tenantId);
}
