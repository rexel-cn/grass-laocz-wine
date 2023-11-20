package com.rexel.send.factory;

import com.rexel.common.constant.Constants;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.framework.mq.redis.core.RedisMQTemplate;
import com.rexel.framework.websocket.RedisWebSocketMessage;
import com.rexel.system.domain.GrassUserMessage;
import com.rexel.system.service.IGrassUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

/**
 * @ClassName Message
 * @Description 站内信
 * Author 孟开通
 * Date 2022/8/9 17:27
 **/
@Component
public class MessageSend extends PushAbstract {
    @Autowired
    private IGrassUserMessageService userMessageService;
    @Autowired
    private RedisMQTemplate redisMQTemplate;

    /**
     * 推送站内信
     *
     * @param userIdList userIdList
     * @param format     format
     * @param noticeType noticeType
     */
    @Override
    public void startSend(List<SysUser> userIdList, String format, Long noticeType) {
    }

    /**
     * 推送站内信
     *
     * @param userIdList  userIdList
     * @param format      format
     * @param policeLevel policeLevel
     * @param noticeType  noticeType
     */
    @Override
    public void startSend(List<SysUser> userIdList, String format, String policeLevel, Long noticeType) {
        userIdList.forEach(sysUser -> {
            // 根据开关状态进行发送
            if (isOpen(sysUser.getTenantId(), Constants.NOTICE_USER_MAIL)) {
                GrassUserMessage userMessage = new GrassUserMessage();
                userMessage.setTenantId(sysUser.getTenantId());
                userMessage.setUserId(sysUser.getUserId().toString());
                userMessage.setSendContent(MessageFormat.format(format, sysUser.getUserName()));
                userMessage.setPoliceLevel(policeLevel);
                userMessage.setNoticeType(noticeType);
                // 插入数据库
                userMessageService.save(userMessage);
                // 插入数据库并推送
                insertAndPushWebSocket(sysUser);
            }
        });
    }

    /**
     * 插入数据库并推送
     *
     * @param sysUser     sysUser
     */
    private void insertAndPushWebSocket(SysUser sysUser) {
        // 查询未读消息总数
        GrassUserMessage userMessageQueryPo = new GrassUserMessage();
        userMessageQueryPo.setUserId(sysUser.getUserId().toString());
        userMessageQueryPo.setTenantId(sysUser.getTenantId());
        userMessageQueryPo.setIsRead(0L);
        userMessageQueryPo.setNoticeType(0L);
        int count = userMessageService.selectCountByGrassUserMessage(userMessageQueryPo);
        // 推送站内信
        RedisWebSocketMessage testMessage = new RedisWebSocketMessage();
        testMessage.addHeader("userId", sysUser.getUserId().toString());
        testMessage.setBody(count);
        redisMQTemplate.send(testMessage);
    }
}