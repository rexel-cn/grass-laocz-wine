package com.rexel.oss.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * MinIo 配置文件
 */

@ConfigurationProperties(prefix = "oss")
public class MinioConfig {

    /**
     * 服务地址（服务接口地址）
     */
     String url= "";

    /**
     * 用户名
     */
     String accessKey= "";

    /**
     * 密码
     */
     String secretKey= "";

    /**
     * 存储桶名称
     */
     String bucketName= "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

/*
    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();
    }
*/

}

