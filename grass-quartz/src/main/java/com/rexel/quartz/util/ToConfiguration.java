package com.rexel.quartz.util;

import com.alibaba.fastjson2.JSON;
import com.rexel.common.config.ConfigurationConfig;
import com.rexel.system.domain.ConfigurePhone;
import com.rexel.system.service.ISysTenantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @version 1.0
 * @Author limuyu
 * @Date 2023/4/21 13:23
 */

@Component
@Slf4j
public class ToConfiguration {

    @Autowired
    private ISysTenantService sysTenantService;
    @Autowired
    private ConfigurationConfig configurationConfig;
    /**
     * 定时向组态传输租户手机号
     *
     * @return boolean
     */
    public void phoneToConfiguration() {
        List<ConfigurePhone> phoneNumberList = sysTenantService.selectPhoneFromTenant();


        phoneToConfigure(phoneNumberList);
    }


    /**
     * 将电话号list传给组态接口
     */

    private void phoneToConfigure(List<ConfigurePhone> phoneNumberList) {

        //获取配置

        String url = configurationConfig.getSyncUrl();


        //创建httpClient连接
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //调用接口传递参数
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(phoneNumberList),
                ContentType.create("application/json", "UTF-8")));
        try {
            CloseableHttpResponse execute = httpClient.execute(httpPost);
            HttpEntity entity = execute.getEntity();
            String str = EntityUtils.toString(entity);
            log.info("账号同步给组态接口调用成功{}",str);
        } catch (IOException e) {
            log.info("账号同步给组态接口调用失败");
            e.printStackTrace();
        }
    }
}
