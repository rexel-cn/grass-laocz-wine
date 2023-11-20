package com.rexel.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MqttProperties
 * @Description mqtt配置类
 * @Author 孟开通
 * @Date 2023/8/31 11:01
 **/
@Configuration
@ConfigurationProperties(prefix = "mqtt")
@Data
public class MqttProperties {

    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 订阅主题，可以是多个主题
     */
    private String[] topics;
    /**
     * 服务器地址以及端口
     */
    private String[] serverUrls;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passWord;

    /**
     * 心跳时间,默认为5分钟
     */
    private Integer keepAliveInterval;

    /**
     * 是否不保持session,默认为session保持
     */
    private Boolean cleanSession;

    /**
     * 是否自动重联，默认为开启自动重联
     */
    private Boolean automaticReconnect;

    /**
     * 连接超时,默认为30秒
     */
    private int completionTimeout;

    /**
     * 通信质量，详见MQTT协议
     */
    private Integer qos;
}
