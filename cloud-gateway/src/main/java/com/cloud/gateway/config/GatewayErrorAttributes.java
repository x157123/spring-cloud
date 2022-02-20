package com.cloud.gateway.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liulei
 */
public class GatewayErrorAttributes extends DefaultErrorAttributes{
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        // 这里其实可以根据异常类型进行定制化逻辑
        Throwable error = super.getError(request);
        Map<String, Object> errorAttributes = new HashMap<>(8);
        errorAttributes.put("message", error.getMessage());
        errorAttributes.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorAttributes.put("method", request.methodName());
        errorAttributes.put("path", request.path());
        return errorAttributes;
    }
}
