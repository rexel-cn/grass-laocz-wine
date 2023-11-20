package com.rexel.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName PulseUrlConfig
 * @Description TODO
 * @Author Hai.Dong
 * @Date 2022/5/11 14:42
 **/
@Data
@Configuration
public class PulseIpConfig {


    @Value("${pulse.ip}")
    private String ip;

    @Value("${pulse.port}")
    private Integer port;
}
