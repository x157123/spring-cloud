package org.cloud.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cloud.user.vo.ServeTableVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("/test")
@Tag(description = "同步启动服务", name = "同步启动服务")
public class TestController {


    @GetMapping("/msg")
    @Operation(summary = "测试", description = "测试")
    public ServeTableVo msg(){
        System.out.println("xxxxxxxxxxxxxx");
        return new ServeTableVo();
    }
}
