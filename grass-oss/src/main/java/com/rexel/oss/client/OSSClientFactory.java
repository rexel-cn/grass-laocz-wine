package com.rexel.oss.client;

import com.rexel.oss.constant.Constants;

/**
 * 文件上传Factory
 */
public final class OSSClientFactory {

    public static IOSSClient build(String clientType) {
        IOSSClient ossClient;
        if (Constants.CLIENT_LOCAL.equals(clientType)) {
            ossClient = new LocalClient();
        } else if (Constants.CLIENTA_ALIYUN.equals(clientType)) {
            ossClient = new AliyunOSSClient();
        } else if (Constants.CLIENTA_MINIO.equals(clientType)) {
            ossClient = new MInoOSSClient();
        } else {
            return null;
        }
        return ossClient;
    }

}
