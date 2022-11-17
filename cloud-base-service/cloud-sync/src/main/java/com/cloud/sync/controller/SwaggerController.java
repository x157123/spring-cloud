package com.cloud.sync.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(description = "文档接口", name = "文档接口")
public class SwaggerController {

    @GetMapping(value = "/swagger-resources")
    @Operation(summary = "获取服务名称", description = "获取服务名称")
    public JSONArray resources() {
        JSONArray jsonArray =  new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","分组接口");
        jsonObject.put("url","/v3/api-docs");
        jsonObject.put("swaggerVersion","3.0");
        jsonObject.put("location","/v3/api-docs");
        jsonArray.add(jsonObject);
        return jsonArray;
    }
}
