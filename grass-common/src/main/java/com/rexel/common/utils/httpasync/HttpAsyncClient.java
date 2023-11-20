package com.rexel.common.utils.httpasync;/**
 * @Author 董海
 * @Date 2022/12/19 13:56
 * @version 1.0
 */

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.core.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName HttpAsyncClient
 * @Description Http 异步请求工具类
 * @Author Hai.Dong
 * @Date 2022/12/19 13:56
 **/
@Slf4j
@Component
public class HttpAsyncClient {


    @Autowired
    private RedisCache redisCache;


    /**
     * get请求
     * <p>
     * 请求示例：  replace 为请求参数
     * LinkedHashMap<String, Class> stringClassLinkedHashMap = HttpAsyncClient.batchRequestGet(
     * Arrays.asList("http://47.116.1.70:9809/api/rexel-api/gs/produce/factory/product"),
     * replace,
     * FactoryEnergyConsumptionVO.class);
     *
     * @param urlList 请求地址
     * @return LinkedHashMap<String, String>  。 v： String。 自己根据需求可以进行任意转换，转换为list 或者对象都可以
     */
//    @Deprecated
//    public LinkedHashMap<String, String> batchRequestGet(List<String> urlList, String param) {
//        String replace = null;
//        if (StringUtils.isNotEmpty(param)) {
//            replace = param.replace(":", "=");
//            replace = replace.replace("{", "");
//            replace = replace.replace("}", "");
//            replace = replace.replace(",", "&");
//            replace = replace.replace(",", "&");
//            replace = replace.replace("\"", "");
//        }
//
//        List<HttpRequestBase> requestsList = new ArrayList<>(urlList.size());
//        String finalReplace = replace;
//        urlList.forEach(a -> requestsList.add(new HttpGet(a + "?" + finalReplace)));
//        return commmonRequest(requestsList);
//    }

    /**
     * @param urlList 请求地址
     * @param o       请求参数
     * @return 自己根据需求可以进行任意转换，转换为list 或者对象都可以
     * @throws URISyntaxException
     */
    public LinkedHashMap<String, String> batchRequestGet(List<String> urlList, Object o) throws URISyntaxException {
        List<HttpRequestBase> requestsList = new ArrayList<>(urlList.size());
        if (CollectionUtil.isEmpty(urlList)) {
            return new LinkedHashMap<>();
        }
        if (ObjectUtil.isNotEmpty(o)) {
            Map<String, Object> stringObjectMap = BeanUtil.beanToMap(o);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                //时间格式额外处理
                // 直接toString 2023-01-01 00:00:00.0   2023-12-31 23:59:59.999   参数不会传递成功
                //需要转换为 2023-01-01 00:00:00  2023-12-31 23:59:59
                if (entry.getValue() instanceof Date) {
                    entry.setValue(DateUtil.formatDateTime((Date) entry.getValue()));
                }
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            for (String s : urlList) {
                URIBuilder uriBuilder = new URIBuilder(s);
                uriBuilder.setParameters(nameValuePairs);
                requestsList.add(new HttpGet(uriBuilder.build()));
            }
        } else {
            for (String s : urlList) {
                requestsList.add(new HttpGet(s));
            }
        }
        return commmonRequest(requestsList);
    }
    /**
     * @param urlList 请求地址
     * @return
     */
    public <T> LinkedHashMap<String, String> commmonRequest(List<HttpRequestBase> urlList) {
        final RequestConfig requestConfitg = RequestConfig.custom()
                .setSocketTimeout(30000)
                // 超时时间 ms
                .setConnectTimeout(10000)
                .build();
        final CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfitg).build();

        httpClient.start();

        final CountDownLatch latch = new CountDownLatch(urlList.size());
        LinkedHashMap<String, String> responseJson = new LinkedHashMap<>();

        for (final HttpRequestBase request : urlList) {
            httpClient.execute(request, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(final HttpResponse response) {
                    latch.countDown();
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        try {
                            StringBuilder result = new StringBuilder();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                result.append(line);
                            }
                            JSONObject jsonObject = JSON.parseObject(result.toString());
                            String data = jsonObject.get("data").toString();
                            if (!data.equals("{}")) {
                                responseJson.put(request.getURI().toString(), data);
                                //存放到redis
                                redisCache.setCacheObject(request.getURI().toString(), data);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 如果请求失败或者异常，返回上一次的结果
                        responseJson.put(request.getURI().toString(), redisCache.getCacheObject(request.getURI().toString()));
                    }
                }

                @Override
                public void failed(final Exception ex) {
                    latch.countDown();
                    log.error(request.getURI() + "请求失败，失败原因:" + ex.getCause().toString());
                    // 如果请求异常，返回上一次的结果
                    responseJson.put(request.getURI().toString(), redisCache.getCacheObject(request.getURI().toString()));
                }

                @Override
                public void cancelled() {
                    latch.countDown();
                }
            });
        }
        try {
            //保证请求全部执行完成
            latch.await();
            return responseJson;
        } catch (InterruptedException ex) {
            log.error("HttpAsyncClient请求异常，异常原因:" + ex.getCause().toString());
        } finally {
            try {
                httpClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return responseJson;
    }


    /**
     * post 请求
     *
     * @param urlList        请求地址
     * @param queryParameter 请求参数
     * @param clazz          返回类型
     * @return
     */
    public <T> LinkedHashMap<String, String> batchRequestPost(List<String> urlList, String queryParameter, Class<T> clazz) {

        List<HttpRequestBase> requestsList = new ArrayList<>(urlList.size());
        urlList.forEach(a -> {
            HttpPost httpPost = new HttpPost(a);
            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(queryParameter == null ? new JSONObject().toJSONString() : queryParameter, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(requestEntity);
            requestsList.add(new HttpPost(a));
        });

        return commmonRequest(requestsList);
    }
}
