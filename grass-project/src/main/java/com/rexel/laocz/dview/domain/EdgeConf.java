package com.rexel.laocz.dview.domain;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName DviewServiceImpl
 * @Description 访问DVIEW服务的配置
 * @Author 孟开通
 * @Date 2023/2/16 11:51
 **/
@Data
@Configuration
public class EdgeConf {
    /**
     * IP地址
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;
}

