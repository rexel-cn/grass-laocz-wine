package com.rexel.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.rexel.common.constant.Constants;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.utils.spring.SpringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName LoginTokenUtils
 * @Description
 * @Author 孟开通
 * @Date 2023/1/13 15:06
 **/
public class LoginTokenUtils {

    private static final RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    public static List<LoginUser> getLoginUserList() {
        Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
        List<LoginUser> list = new ArrayList<>();
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            list.add(user);
        }
        return list;
    }

    public static void offline(String key) {
        redisCache.delete(Constants.LOGIN_TOKEN_KEY + key);
    }


    /**
     * 根据用户id 获取token
     *
     * @param userId 用户id
     * @return token集合
     */
    public static List<String> getTokenByUserId(Long userId) {
        List<String> tokenList = new ArrayList<>();
        List<LoginUser> loginUserList = LoginTokenUtils.getLoginUserList();
        Map<Long, List<LoginUser>> userMap = loginUserList.stream().collect(Collectors.groupingBy(LoginUser::getUserId));
        if (userMap.containsKey(userId)) {
            List<LoginUser> list = userMap.get(userId);
            if (CollectionUtil.isNotEmpty(list)) {
                list.forEach(loginUser -> tokenList.add(loginUser.getToken()));
            }
        }
        return tokenList;
    }
}
