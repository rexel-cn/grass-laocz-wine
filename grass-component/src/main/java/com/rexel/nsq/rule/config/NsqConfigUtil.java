package com.rexel.nsq.rule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ${PROJECT_NAME}
 * @Description
 * @Author 孟开通
 * @Date 2022/4/22 17:49
 **/
@Configuration
public class NsqConfigUtil {
    @Value("${nsq.status}")
    public Boolean status;
    @Value("${nsq.namesrvAddr}")
    public String namesrvAddr;

    @Value("${nsq.port}")
    public int port;

    @Value("${nsq.MaxInFlight}")
    public int maxInFlight;
}