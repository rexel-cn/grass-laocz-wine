package com.rexel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 * @author ids-admin
 */
@Slf4j
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class GrassServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(GrassServiceApplication.class)
                .run(args);
        log.info("\n##############################################\n" +
                "##                                          ##\n" +
                "##            　grass-service启动成功         ##\n" +
                "##                                          ##\n" +
                "##############################################\n");

    }
}
