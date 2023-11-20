package com.rexel.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.config.PulseUrlConfig;
import com.rexel.common.constant.Constants;
import com.rexel.common.constant.UserConstants;
import com.rexel.common.core.domain.PulseUser;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.exception.UtilException;
import com.rexel.common.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName PulseRefreshToken
 * Description
 * Author 孟开通
 * Date 2022/7/19 10:02
 **/
@Component
@Slf4j
public class PulseRefreshToken {
    @Autowired
    private PulseUrlConfig pulseUrlConfig;
    @Autowired
    private RedisCache redisCache;

    public void refreshTokenTime() throws Exception {
        if (!redisCache.hasKey(UserConstants.PULSE_TOKEN)) {
            getToken();
        }
        //2.刷新token有效期
        Map<String, Object> requestHeader = new HashMap<>(16);
        requestHeader.put("Authorization", redisCache.getCacheObject(UserConstants.PULSE_TOKEN));
        String tokenUrl = pulseUrlConfig.getRefreshToken();
        JSONObject jsonObject = JSON.parseObject(HttpUtils.sendPostJson(tokenUrl, null, requestHeader));
        if (jsonObject.getIntValue(Constants.CODE) != 0) {
            getToken();
            return;
        }
        //3.刷新redis  token 过期时间
        redisCache.setCacheObject(UserConstants.PULSE_TOKEN, redisCache.getCacheObject(UserConstants.PULSE_TOKEN), UserConstants.PULSE_TOKEN_TIME_OUT, TimeUnit.MINUTES);
        log.info("刷新Pulse_token");
    }

    /**
     * 请求 getToken
     *
     * @return boolean
     */
    public void getToken() throws Exception {
        redisCache.delete(UserConstants.PULSE_TOKEN);
        PulseUser user = new PulseUser();
        String tokenUrl = pulseUrlConfig.getTokenUrl();
        JSONObject jsonObject = JSON.parseObject(HttpUtils.sendPost(tokenUrl, JSON.toJSONString(user)));
        String string;
        //0  成功
        if ("200".equals(jsonObject.getString(Constants.CODE))
                && StrUtil.isNotEmpty((string = jsonObject.getJSONObject(Constants.DATA)
                .getString(Constants.TOKEN)))) {
            redisCache.setCacheObject(UserConstants.PULSE_TOKEN, string, UserConstants.PULSE_TOKEN_TIME_OUT, TimeUnit.MINUTES);
            return;
        }
        throw new UtilException("获取pulseToken失败");
    }


}
