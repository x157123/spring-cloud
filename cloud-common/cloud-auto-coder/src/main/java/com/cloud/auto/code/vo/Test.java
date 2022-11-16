package com.cloud.auto.code.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.common.core.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        String str = HttpUtil.sendGet("http://59.225.208.77:9080/zd/ww/xxlx", null, null);
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONArray jsonArray = jsonObject.getJSONArray("datas");
        Map<String, Integer> map = new HashMap<>();
        for (int i = 1; i < jsonArray.size()+1; i++) {
            JSONObject obj = jsonArray.getJSONObject(i-1);
            String key = obj.getString("dm");
            String name = obj.getString("mc");
            String key1 = key.substring(0, 1);
            String key2 = key.substring(1, key.length());
            Integer parentId = 0;
            if (key2.equals("00")) {
                map.put(key1, i);
            }else{
                parentId = map.get(key1);
            }
            System.out.println("INSERT INTO event_test_v5.sg_sync_dict (id, dict_key, \"name\", parent_id, seq, \"type\") VALUES('"+i+"','"+ key +"','" +name+"', '"+ parentId +"', "+i+", 'ww_xx');");
        }
        System.out.println(str);
    }
}
