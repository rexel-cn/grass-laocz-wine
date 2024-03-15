package com.rexel.common.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CaffeineUtil {
    //咖啡因缓存
    public static Cache<Object, Object> cache = Caffeine.newBuilder().build();

    /**
     * 添加String类型缓存
     *
     * @param key
     * @param value
     */
    public static void setStringCache(String key, String value) {
        cache.put(key, value);
    }

    /**
     * 是否包含key
     *
     * @param key
     * @return 包含：true 不包含：false
     */
    public static boolean getIndexCache(String key) {
        return cache.asMap().containsKey(key);
    }

    /**
     * 返回类型
     *
     * @param key
     * @return
     */
    public static String getStingCache(String key) {
        return cache.getIfPresent(key) + "";
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public static void RemoveCache(String key) {
        cache.invalidate(key);
    }

    /**
     * 删除所有
     */
    public static void RemoveAllCache() {
        cache.invalidateAll();
    }


}
