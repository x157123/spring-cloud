package com.cloud.gateway.controller;


import com.alibaba.fastjson.JSONObject;
import com.cloud.gateway.config.MySwaggerResourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

;

/**
 * swagger聚合接口，三个接口都是swagger-ui.html需要访问的接口
 *
 * @author liulei
 */
@RestController
@RequestMapping("/v3/api-docs")
public class SwaggerResourceController {

    private MySwaggerResourceProvider swaggerResourceProvider;

    @Autowired
    public SwaggerResourceController(MySwaggerResourceProvider swaggerResourceProvider) {
        this.swaggerResourceProvider = swaggerResourceProvider;
    }


    @GetMapping("/swagger-config")
    public JSONObject getList(){
        return swaggerResourceProvider.getRoot();
    }

}
