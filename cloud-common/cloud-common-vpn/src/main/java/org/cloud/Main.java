package org.cloud;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.cloud.common.util.http.OkHttpUtils;
import com.cloud.common.util.text.Base64Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static String gcodeUrl = "https://api.gcorelabs.com/cdn/public-net-list";


    private static String url = "vmess://ew0KICAidiI6ICIyIiwNCiAgInBzIjogIndtZXNzLXdzIiwNCiAgImFkZCI6ICJjb2RlLjE1NzEyMzQ1Lnh5eiIsDQogICJwb3J0IjogIjQ0MyIsDQogICJpZCI6ICI0NTViNzRlMS03ODEwLTQ5NmMtYTQ4Yi1kNDlmYjJjMWNlNzIiLA0KICAiYWlkIjogIjAiLA0KICAic2N5IjogImF1dG8iLA0KICAibmV0IjogIndzIiwNCiAgInR5cGUiOiAibm9uZSIsDQogICJob3N0IjogIiIsDQogICJwYXRoIjogIi9kb2MiLA0KICAidGxzIjogInRscyIsDQogICJzbmkiOiAiIiwNCiAgImFscG4iOiAiIg0KfQ==";

    public static void main(String[] args) {

        String str = OkHttpUtils.builder().url(gcodeUrl).get().sync();

        String newUrl = url.substring("vmess://".length());

        String json = Base64Utils.decode(newUrl);

        JSONObject newVmess = JSONObject.parseObject(json);

        System.out.println(json);

        Pattern p = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d");

        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONArray jsonArray = jsonObject.getJSONArray("addresses");
        for (Object o : jsonArray) {
            String ip  = o.toString();
            Matcher m = p.matcher(ip);
            while(m.find()) {
                newVmess.put("add",m.group());
                System.out.println("vmess://" + Base64Utils.encode(newVmess.toString()));
            }
        }

    }
}