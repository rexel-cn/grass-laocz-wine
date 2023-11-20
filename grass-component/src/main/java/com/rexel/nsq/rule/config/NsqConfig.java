package com.rexel.nsq.rule.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName MqConfig
 * @Description Nsq配置类
 * @Author 孟开通
 * @Date 2022/4/22 17:38
 **/
@Data
@Configuration
@PropertySource("classpath:/nsq.properties")
public class NsqConfig {

    @Value("${nsq.topic}")
    private String topic;

    @Value("${nsq.topic.channel}")
    private String channel;

}