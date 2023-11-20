package com.rexel.common.utils.pulse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.exception.UtilException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * ClassName PulseHttpResponseUtil
 * Description
 * Author 孟开通
 * Date 2022/5/13 20:21
 **/
@Slf4j
public class PulseHttpResponseUtil {

    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String DATA = "data";

    public static final String DATA_LIST = "dataList";


    /**
     * 预处理格式化pulse 结果
     *
     * @param returnValue
     * @return
     */
    public static JSONObject pretreatmentResultsObject(String returnValue) {
        String responseToData = responseToData(JSON.parseObject(returnValue));
        return JSON.parseObject(responseToData);
    }

    public static JSONArray pretreatmentResultsArray(String returnValue) {
        String responseToData = responseToData(JSON.parseObject(returnValue));
        return JSON.parseArray(responseToData);
    }

    public static <T> List<T> pretreatmentResultsList(String returnValue, Class<T> clazz) {
        String responseToData = responseToData(JSON.parseObject(returnValue));
        return JSON.parseArray(responseToData, clazz);
    }

    public static <T> List<T> pretreatmentResultsListToPage(String returnValue, Class<T> clazz) {
        String responseToData = responseToDataPage(JSON.parseObject(returnValue));
        return JSON.parseArray(responseToData, clazz);
    }

    /**
     * true为成功
     *
     * @param returnValue
     * @return
     */
    public static Boolean responseToBoolean(String returnValue) {
        JSONObject jsonObject = JSON.parseObject(returnValue);
        codeCheck(jsonObject);
        return true;
    }

    public static String responseToData(String returnValue) throws RuntimeException {
        JSONObject jsonObject = JSON.parseObject(returnValue);
        codeCheck(jsonObject);
        return jsonObject.getString(DATA);
    }

    private static String responseToData(JSONObject jsonObject) throws RuntimeException {
        codeCheck(jsonObject);
        return jsonObject.getString(DATA);
    }

    private static String responseToDataPage(JSONObject jsonObject) throws RuntimeException {
        codeCheck(jsonObject);
        return jsonObject.getJSONObject(DATA).getString(DATA_LIST);
    }

    private static void codeCheck(JSONObject jsonObject) {
        //获取code编码
        int code = jsonObject.getIntValue(CODE);
        //不为200报错
        if (code != 200) {
            log.error("pulse获得返回数据信息错误 code=" + code + "msg=" + jsonObject.getString(MSG));
            throw new UtilException("系统内部接口错误");
        }
    }

}
