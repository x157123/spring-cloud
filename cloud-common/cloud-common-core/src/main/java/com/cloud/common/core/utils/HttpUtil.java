package com.cloud.common.core.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liulei
 */
public class HttpUtil {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>(3);
        Map<String, String> headerMap = new HashMap<>(1);
        map.put("client_id", "94c9fb37ae3e40f2a70b4afdbf4a5671");
        map.put("client_secret", "46d52e3f5391489585445fed58ed50db");
        map.put("grant_type", "client_credentials");
        String url = "http://10.11.231.145:18083/tglserver/token";

        String auth = "";

        try {
            String jsonString = sendPost(url, map, null);
            JSONObject object = JSONObject.parseObject(jsonString);
            auth = object.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.clear();
        headerMap.clear();
        map.put("pageSize", "10");
        map.put("page", "10");
        map.put("client_id", "94c9fb37ae3e40f2a70b4afdbf4a5671");
        headerMap.put("Authorization", "" + "bearer " + auth);
        url = "http://10.11.231.145:18083/Api/CommonApi/GetMonitorBycondition";
        sendGet(url, map, headerMap);
    }


    public static String sendGet(String url, Map<String, String> paramsMap, Map<String, String> headerMap) {
        //判断是否有参数
        StringBuffer stringBuffer = new StringBuffer();
        if (paramsMap != null) {
            //将参数转换为字符串
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                stringBuffer.append(key).append("=").append(value).append("&");
            }
            if (url.contains("?")) {
                url += "&" + stringBuffer.substring(0, stringBuffer.length() - 1);
            } else {
                url += "?" + stringBuffer.substring(0, stringBuffer.length() - 1);
            }
        }
        // 创建httpGet远程连接实例
        HttpGet httpGet = new HttpGet(url);
        if (headerMap != null) {
            headerMap.forEach((k, v) -> {
                httpGet.setHeader(k, v);
            });
        }
        // 设置配置请求参数 连接主机服务超时时间35000 请求超时时间35000  数据读取超时时间60000
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(35000)
                .setConnectionRequestTimeout(35000)
                .setSocketTimeout(60000)
                .build();
        // 为httpGet实例设置配置
        httpGet.setConfig(requestConfig);
        return getResponse(httpGet);
    }

    public static String sendPost(String url, Map<String, String> paramsMap, Map<String, String> headerMap) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        if (paramsMap != null) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            paramsMap.forEach((k, v) -> {
                params.add(new BasicNameValuePair(k, v));
            });
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        }
        if (headerMap != null) {
            headerMap.forEach((k, v) -> {
                httpPost.setHeader(k, v);
            });
        }
        return getResponse(httpPost);
    }

    /**
     * 获取
     *
     * @param httpUriRequest
     * @return
     */
    private static String getResponse(HttpUriRequest httpUriRequest) {
        // 默认配置创建一个httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // 执行get请求得到返回对象
            CloseableHttpResponse response = httpClient.execute(httpUriRequest);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
