package com.cloud.gateway.controller;


import com.alibaba.fastjson.JSONObject;
import com.cloud.gateway.config.MySwaggerResourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * swagger聚合接口，三个接口都是swagger-ui.html需要访问的接口
 *
 * @author liulei
 */
@RestController
@RequestMapping
public class SwaggerResourceController {

    private final MySwaggerResourceProvider swaggerResourceProvider;

    @Autowired
    public SwaggerResourceController(MySwaggerResourceProvider swaggerResourceProvider) {
        this.swaggerResourceProvider = swaggerResourceProvider;
    }


    @RequestMapping("/v3/api-docs/swagger-config")
    public JSONObject getList() {
        return swaggerResourceProvider.getRoot();
    }


    @RequestMapping("/docs/{serve}")
    public JSONObject getDoc(@PathVariable String serve){
        System.out.println(serve);
        return new JSONObject();
    }

    public String client(String url){
        RestTemplate client = new RestTemplate();
        //  执行HTTP请求
        ResponseEntity<String> response = client.getForEntity(url, String.class);
        return response.getBody();
    }


}
