package com.rexel.common.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonUtil
 *
 * @author admin
 * @date 2021-12-29
 */
@Slf4j
public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 按照指定的键排序JSONArray
     *
     * @param jsonArray jsonArray
     * @param sortKey sortKey
     * @return 结果
     */
    public static JSONArray jsonArraySort(JSONArray jsonArray, String sortKey, boolean desc) {
        List<JSONObject> jsonValues = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonValues.add(jsonArray.getJSONObject(i));
        }

        jsonValues.sort((a, b) -> {
            String valA = a.getString(sortKey);
            String valB = b.getString(sortKey);
            if (desc) {
                return -valA.compareTo(valB);
            } else {
                return valA.compareTo(valB);
            }
        });

        JSONArray result = new JSONArray();
        result.addAll(jsonValues);
        return result;
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(text, typeReference);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            log.error("json parse err,json:{}", bytes, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {
            return null;
        }
        try {
            return objectMapper.readValue(text, clazz);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static String toJsonString(Object object) {
        return objectMapper.writeValueAsString(object);
    }
}