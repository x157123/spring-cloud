package com.test;

import com.cloud.common.util.http.OkHttpUtils;

public class Location {
    public static void main(String[] args) {

        for (int i = 0; i < 100000; i++) {
            OkHttpUtils.builder().url("http://127.0.0.1:18085/location/putLocation")
                    // 有参数的话添加参数，可多个
                    .addParam("userId", i + "")
                    .addParam("lat", "0.0")
                    .addParam("lon", "0.0")
                    .get()
                    .sync();
        }
    }
}
