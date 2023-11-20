package com.rexel.common.utils.pulse;
/**
 * @Author 董海
 * @Date 2022/5/27 10:45
 * @version 1.0
 */

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.constant.Constants;
import com.rexel.common.constant.HttpStatus;
import com.rexel.common.constant.UserConstants;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.PulseRefreshToken;
import com.rexel.common.utils.http.HttpUtils;
import com.rexel.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PulseHttpRequestUtil
 * @Description pulse 请求util
 * @Author Hai.Dong
 * @Date 2022/5/27 10:45
 **/
@Slf4j
public class PulseHttpRequestUtil {

    private static final RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
    private static final PulseRefreshToken pulseRefreshToken = SpringUtils.getBean(PulseRefreshToken.class);

    /**
     * @param url     请求url
     * @param jsonStr 请求参数
     * @return String 返回数据
     */
    public static String sendPostJson(String url, String jsonStr) {
        String responseStr;
        try {
            responseStr = HttpUtils.sendPostJson(url, jsonStr, getRequestHeader());
            JSONObject jsonObject = JSON.parseObject(responseStr);
            if (jsonObject.getIntValue(Constants.CODE) == 2) {
                pulseRefreshToken.getToken();
                responseStr = sendPostJson(url, jsonStr);
            }
        } catch (Exception e) {
            log.error("pulse通讯异常{}", e);
            throw new ServiceException("平台内部通讯异常", HttpStatus.BAD_REQUEST);
        }
        return responseStr;
    }


    /**
     * 设置请求头
     *
     * @return
     */
    private static Map<String, Object> getRequestHeader() throws Exception {
        Map<String, Object> requestHeader = new HashMap<>(16);
        String cacheObject = redisCache.getCacheObject(UserConstants.PULSE_TOKEN);
        if (StrUtil.isEmpty(cacheObject)) {
            SpringUtils.getBean(PulseRefreshToken.class).getToken();
            cacheObject = redisCache.getCacheObject(UserConstants.PULSE_TOKEN);
        }
        requestHeader.put("Authorization", cacheObject);
        return requestHeader;
    }
}
