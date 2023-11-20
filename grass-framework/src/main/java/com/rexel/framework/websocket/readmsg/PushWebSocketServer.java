package com.rexel.framework.websocket.readmsg;

import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.spring.SpringUtils;
import com.rexel.message.domain.GrassUserMessageFramework;
import com.rexel.message.service.IGrassUserMessageFrameworkApi;
import com.rexel.user.ISysUserServiceFrameworkApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName WebSocketServer
 * @Description
 * @Author 董海
 * @Date 2020/6/28 16:25
 **/
@ServerEndpoint(value = "/rexel-api/pushServer/{token}")
@Component
@Slf4j
public class PushWebSocketServer {
    /**
     * 必须按照此方式注入service
     */
    public static IGrassUserMessageFrameworkApi userMessageService = SpringUtils.getBean(IGrassUserMessageFrameworkApi.class);
    public static ISysUserServiceFrameworkApi userService = SpringUtils.getBean(ISysUserServiceFrameworkApi.class);
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    public static ConcurrentHashMap<String, List<PushWebSocketServer>> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";


    /**
     * 发送object 自定义消息
     *
     * @param message 消息
     * @param userId  用户id
     */
    public static void sendAsyInfo(String message, @PathParam("userId") String userId) {
        log.info("[socket]--发送消息到用户:{},报文信息:{}", userId, message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).forEach(item -> item.sendStringMessage(message));
        } else {
            log.error("[socket]--用户:{},不在线！", userId);
        }
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String userId) {
        // websocket会话
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).add(this);
        } else {
            webSocketMap.put(userId, new ArrayList<>(Collections.singletonList(this)));
        }

        SysUser user = userService.selectUserById(Long.valueOf(userId));
        if (user == null) {
            log.error("[socket]--用户id:{},查无此id！", userId);
            throw new RuntimeException("用户id:" + userId + ",查无此id！");
        }
        GrassUserMessageFramework userMessageQueryPo = new GrassUserMessageFramework();
        userMessageQueryPo.setUserId(userId);
        userMessageQueryPo.setTenantId(user.getTenantId());
        userMessageQueryPo.setIsRead(0L);
        userMessageQueryPo.setNoticeType(0L);
        int count = userMessageService.selectCountByGrassUserMessage(userMessageQueryPo);
        sendAsyInfo(count + "", userId);

        log.info("[socket]--当前连接用户id:{},当前在线人数为:{}", userId, webSocketMap.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).remove(this);
            if (webSocketMap.get(userId).size() == 0) {
                webSocketMap.remove(userId);
            }
        }
        log.info("[socket]--用户退出id:{},当前在线人数为:{}", userId, webSocketMap.size());
    }

    /**
     * @param error XX
     */
    @OnError
    public void onError(Throwable error) {
        log.error("[socket]--用户连接错误:" + error.getMessage());
        log.error("[socket]--用户连接错误:{},原因:{}", userId, error.getMessage());
    }

    /**
     * 实现服务器主动推送
     *
     * @param message 推送消息
     */
    @OnMessage
    public void sendStringMessage(String message) {
        try {
            if (session.isOpen()) {
                this.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("[socket]--用户发送消息错误:{},原因:{}", userId, e.getMessage());
        }
    }
}