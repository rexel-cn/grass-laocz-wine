package com.rexel.common.utils;/**
 * @Author 董海
 * @Date 2022/12/7 17:02
 * @version 1.0
 */

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName LinkedHashMapUtil
 * @Description TODO
 * @Author Hai.Dong
 * @Date 2022/12/7 17:02
 **/
public class LinkedHashMapUtil {


    /**
     * 获取第一个元素
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static  <K, V> Map.Entry<K, V> getHead(LinkedHashMap<K, V> map) {
        return map.entrySet().iterator().next();
    }


    /**
     * 获取尾部元素
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map.Entry<K, V> getTail(LinkedHashMap<K, V> map) {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        Map.Entry<K, V> tail = null;
        while (iterator.hasNext()) {
            tail = iterator.next();
        }
        return tail;
    }
}
