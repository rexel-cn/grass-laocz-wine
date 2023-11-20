package com.rexel.nsq.rule.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.rexel.nsq.rule.bean.AliConfigPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: 董海
 * @Date: 2020/3/17
 * @Description: 阿里iot 配置文件工具类
 */
@Component
public class AliiotPropertiesUtil {
    private static final Logger log = LoggerFactory.getLogger(AliiotPropertiesUtil.class);
    private static AliiotPropertiesUtil propertiesUtil = new AliiotPropertiesUtil();

    private AliiotPropertiesUtil() {
        //Nothing
    }

    public AliiotPropertiesUtil getInstance() {
        return propertiesUtil;
    }

    /**
     * 获取DefaultAcsClient
     *
     * @param aliConfigPropertiesBean 配置文件
     * @return DefaultAcsClient
     */
    public DefaultAcsClient getIotClient(AliConfigPropertiesBean aliConfigPropertiesBean) {
        String accessKey = aliConfigPropertiesBean.getAccessKey();
        String accessSecret = aliConfigPropertiesBean.getAccessSecret();
        String regionId = aliConfigPropertiesBean.getRegionId();
        DefaultAcsClient client = null;
        try {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessSecret);
            client = new DefaultAcsClient(profile);
        } catch (Exception e) {
            log.error("读取阿里云资源配置文件异常:", e.getMessage());
        }
        return client;
    }
}
