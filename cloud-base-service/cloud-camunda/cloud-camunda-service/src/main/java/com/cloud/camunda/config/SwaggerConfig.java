package com.cloud.camunda.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author Lenovo
 * @date 2020/8/3
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean(name="docket")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("接口文档")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SeClover数据平台开放接口")
                .description("Rest API接口")
                .termsOfServiceUrl("https://blog.csdn.net/youbitch1")
                .version("1.0")
                .build();
    }


}
