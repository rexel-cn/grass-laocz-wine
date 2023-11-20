package com.rexel.framework.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @ClassName WebSocketConfig
 * @Description
 * @Author 董海
 * @Date 2020/6/29 14:22
 **/
@Configuration
public class WebSocketConfig {
    /*
     * 本地测试时，需要放开下面注释 。因为使用的是springboot 自身的tomcat。。。声明将@ServerEndPoint的注解交由Spring管理
     * 因为线上环境发布使用的是 外部的 tomcat
     * */


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


}