package com.cloud.gateway.controller;


import com.alibaba.fastjson.JSONArray;
import com.cloud.gateway.config.MySwaggerResourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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


    @RequestMapping("/swagger-config")
    public JSONArray getList(){
        return swaggerResourceProvider.getRoot();
}

}
