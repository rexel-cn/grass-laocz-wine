package com.rexel.nsq.rule.bean;

import com.rexel.common.utils.Base64Utils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: 董海
 * @Date: 2020/3/19
 * @Description: 阿里云连接配置类
 */
@Component
@Data
@ConfigurationProperties("ali")
public class AliConfigPropertiesBean {

    private String accessKey;
    private String accessSecret;
    private String uid;
    private String regionId;
    private String iotDomain;

    private String iotVersion;
    private String smsDomain;
    private String smsVersion;

    public String getSmsDomain() {

        return Base64Utils.decode(smsDomain);
    }

    public String getSmsVersion() {
        return Base64Utils.decode(smsVersion);
    }

    public String getRegionId() {
        return Base64Utils.decode(regionId);
    }

    public String getIotDomain() {
        return Base64Utils.decode(iotDomain);
    }

    public void setIotDomain(String iotDomain) {
        this.iotDomain = iotDomain;
    }

    public String getIotVersion() {
        return Base64Utils.decode(iotVersion);
    }

    public String getAccessKey() {
        return Base64Utils.decode(accessKey);
    }

    public String getAccessSecret() {
        return Base64Utils.decode(accessSecret);
    }

    public String getUid() {
        return Base64Utils.decode(uid);
    }
}
