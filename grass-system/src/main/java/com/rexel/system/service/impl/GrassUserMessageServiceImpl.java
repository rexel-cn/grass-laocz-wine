package com.rexel.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.constant.Constants;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.framework.mq.redis.core.RedisMQTemplate;
import com.rexel.framework.websocket.RedisWebSocketMessage;
import com.rexel.system.domain.GrassUserMessage;
import com.rexel.system.mapper.GrassUserMessageMapper;
import com.rexel.system.service.IGrassUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户站内信Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-09
 */
@Service
public class GrassUserMessageServiceImpl extends ServiceImpl<GrassUserMessageMapper, GrassUserMessage> implements IGrassUserMessageService {
    @Autowired
    private RedisMQTemplate redisMqTemplate;

    /**
     * 查询用户站内信列表
     *
     * @param grassUserMessage 用户站内信
     * @return 用户站内信
     */
    @Override
    public List<GrassUserMessage> selectGrassUserMessageList(GrassUserMessage grassUserMessage) {
        return baseMapper.selectGrassUserMessageList(grassUserMessage);
    }

    /**
     * 查询消息数量
     *
     * @param userMessage userMessage
     * @return 结果
     */
    @Override
    public AjaxResult selectUserMessageCount(GrassUserMessage userMessage) {
        // 查询所有未读消息
        userMessage.setIsRead(0L);
        userMessage.setUserId(SecurityUtils.getUserId().toString());
        List<GrassUserMessage> msgList = baseMapper.selectGrassUserMessageList(userMessage);

        // 按照通知类型统计分组
        Map<Long, List<GrassUserMessage>> map = new HashMap<>(4);
        msgList.forEach(data -> {
            Long noticeType = data.getNoticeType();
            if (!map.containsKey(noticeType)) {
                map.put(noticeType, new ArrayList<>());
            }
            map.get(noticeType).add(data);
        });

        // 生成应答结果
        JSONObject jsonObject = new JSONObject();
        map.forEach((type, list) -> jsonObject.put(type.toString(), list.size()));

        // 确保每一个类型都返回，供前端页面显示用
        if (!jsonObject.containsKey(Constants.ALARM_DEVICE.toString())) {
            jsonObject.put(Constants.ALARM_DEVICE.toString(), 0);
        }
        if (!jsonObject.containsKey(Constants.ALARM_EARLY_WARN.toString())) {
            jsonObject.put(Constants.ALARM_EARLY_WARN.toString(), 0);
        }
        return AjaxResult.success(jsonObject);
    }

    /**
     * 查询消息数量
     *
     * @param grassUserMessage grassUserMessage
     * @return 结果
     */
    @Override
    public Integer selectCountByGrassUserMessage(GrassUserMessage grassUserMessage) {
        return baseMapper.selectCountByGrassUserMessage(grassUserMessage);
    }

    /**
     * @param userMessage userMessage
     * @return 结果
     */
    @Override
    public Boolean confirmRead(GrassUserMessage userMessage) {
        lambdaUpdate().eq(GrassUserMessage::getId, userMessage.getId())
                .set(GrassUserMessage::getIsRead, 1L).update();
        GrassUserMessage userMessageQueryPo = new GrassUserMessage();
        userMessageQueryPo.setUserId(SecurityUtils.getUserId().toString());
        userMessageQueryPo.setTenantId(SecurityUtils.getTenantId());
        userMessageQueryPo.setIsRead(0L);
        //websocket 通知到页面
        pushWebsocketPage(userMessage);
        return true;
    }

    /**
     * @param userMessage userMessage
     * @return 结果
     */
    @Override
    public Boolean allConfirmRead(GrassUserMessage userMessage) {
        lambdaUpdate()
                .eq(GrassUserMessage::getUserId, SecurityUtils.getUserId())
                .eq(GrassUserMessage::getNoticeType, userMessage.getNoticeType())
                .set(GrassUserMessage::getIsRead, 1L).update();
        //websocket 通知到页面
        pushWebsocketPage(userMessage);
        return true;
    }

    /**
     * 删除指定租户数据
     *
     * @param tenantId tenantId
     * @return 结果
     */
    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

    /**
     * 计算未读条数 通知到页面
     *
     * @param userMessageQueryPo userMessageQueryPo
     */
    private void pushWebsocketPage(GrassUserMessage userMessageQueryPo) {
        userMessageQueryPo.setUserId(SecurityUtils.getUserId().toString());
        userMessageQueryPo.setTenantId(SecurityUtils.getTenantId());
        userMessageQueryPo.setIsRead(0L);
        userMessageQueryPo.setNoticeType(0L);
        Integer count = this.selectCountByGrassUserMessage(userMessageQueryPo);

        // 推送站内信
        RedisWebSocketMessage testMessage = new RedisWebSocketMessage();
        testMessage.addHeader("userId", SecurityUtils.getUserId().toString());
        testMessage.setBody(count);
        redisMqTemplate.send(testMessage);
    }
}
