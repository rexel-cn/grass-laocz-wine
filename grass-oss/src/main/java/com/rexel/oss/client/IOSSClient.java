package com.rexel.oss.client;

import com.rexel.oss.config.OssConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 *  kjt.gzst.gov.cn
 *
 * @version V1.0
 * @title: IOSSClient.java
 * @description: 常量 * @date: 2017年8月11日 下午9:55:00
 * @copyright: 2017 kjt.gzst.gov.cn Inc. All rights reserved.
 */
public interface IOSSClient {

    /**
     * 客户端初始化
     */
    void init();

    /**
     * 客户端初始化
     *
     * @param propertiesName propertiesName
     */
    void init(String propertiesName);

    /**
     * 客户端初始化
     *
     * @param config config
     */
    void init(OssConfig config);

    /**
     * 文件上传
     *
     * @param inputStream inputStream
     * @param path path
     * @return 返回可以访问的路径
     */
    String upload(InputStream inputStream, String path);

    /**
     * 文件上传
     *
     * @param multipartFile multipartFile
     * @param bucketName bucketName
     * @param path path
     * @return 返回可以访问的路径
     */
    String upload(MultipartFile multipartFile, String bucketName, String path);

    /**
     * 删除文件
     *
     * @param filename 文件的路径
     * @param bucketName bucketName
     */
    void delete(String filename, String bucketName);

    /**
     * 删除文件
     *
     * @param bucketName bucketName
     */
    void delete(String bucketName);
}
