package com.rexel.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @Author limuyu
 * @Date 2023/4/23 10:07
 */
@Data
@Configuration
public class ConfigurationConfig {

    @Value("${configuration.ip}")
    private String ip;

    @Value("${configuration.port}")
    private Integer port;


    @Value("${configuration.registerUrl}")
    private String registerUrl;

    @Value("${configuration.deleteUrl}")
    private String deleteUrl;

    @Value("${configuration.syncUrl}")
    private String syncUrl;

    public String getRegisterUrl() {
        return ip + ":" + port + registerUrl;
    }

    public String getDeleteUrl() {
        return ip + ":" + port + deleteUrl;
    }

    public String getSyncUrl() {
        return ip + ":" + port + syncUrl;
    }
}
