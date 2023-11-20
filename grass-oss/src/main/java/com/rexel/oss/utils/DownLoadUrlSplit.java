package com.rexel.oss.utils;

/**
 * @ClassName DownLoadUrlSplit
 * @Description 根据下载地址截取
 * @Author 孟开通
 * @Date 2022/11/15 17:15
 **/
public class DownLoadUrlSplit {

    public static String getBucketName(String url) {
        String[] split = url.split("/");
        return split[3];
    }

    public static String getFileName(String url) {
        String[] split = url.split("/");
        return url.substring(url.indexOf(split[4]));
    }
}
