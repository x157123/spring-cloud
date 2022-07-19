package com.user;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.core.utils.HttpUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取用户token
 * @author liulei
 */
public class Token {

    public static void main(String[] args) {

        try {

            Map<String, String> headerMap = new HashMap<>(1);
            headerMap.put("Authorization", "Basic dXNlcmNlbnRlcjoxMTg2MDQ1ZDU1OTlkZTZlZjJjYTI4MjM0N2E1NWNhMg==");

            Map<String, String> paramsMap = new HashMap<>(4);
            paramsMap.put("tenantId", "000000");
            paramsMap.put("grant_type", "password");
            paramsMap.put("scope", "all");

            // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            List<String> auth = new ArrayList<>();
            /* 读入TXT文件 */
            String pathname = "C:\\Users\\liulei\\Desktop\\users.txt";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] str = line.split(",");
                if (str.length >= 2) {
                    paramsMap.put("username", str[0]);
                    paramsMap.put("password", str[1]);
                    String jsonStr = HttpUtil.sendPost("http://192.168.10.116:1000/api/doraemon-oauth/oauth/token", paramsMap, headerMap);
                    JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                    if (jsonObject.getInteger("code").intValue() == 200) {
                        auth.add(jsonObject.getString("access_token"));
                    }
                }
            }




            /* 写入Txt文件 */
            File writeName = new File("X:\\user-data.txt");
//            writeName.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writeName));
            for (String au : auth) {
                out.write("bearer " +au + "\r\n");
            }
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
