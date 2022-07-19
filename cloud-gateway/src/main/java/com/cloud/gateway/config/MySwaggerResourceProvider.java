package com.cloud.gateway.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ksyzz
 * @since <pre>2019/04/09</pre>
 */
@Component
@ConfigurationProperties(prefix = "api.server")
public class MySwaggerResourceProvider {

    /**
     * swagger2默认的url后缀
     */
    private static final String SWAGGER2URL = "/v3/api-docs";

    /**
     * 网关路由
     */
    private final RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    private List<String> exclude;

    @Autowired
    public MySwaggerResourceProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    public Set<String> getServices() {
        Set<String> routeHosts = new HashSet<>();
        // 由于我的网关采用的是负载均衡的方式，因此我需要拿到所有应用的serviceId
        // 获取所有可用的host：serviceId
        routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
                .filter(route -> exclude != null && !exclude.contains(route.getUri().getHost().toLowerCase()))
                .subscribe(route -> routeHosts.add(route.getUri().getHost().toLowerCase()));
        // 记录已经添加过的server，存在同一个应用注册了多个服务在注册中心上
        return routeHosts;
    }


    public JSONObject getRoot() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("configUrl", "/v3/api-docs/swagger-config");
        jsonObject.put("oauth2RedirectUrl", "http://localhost:9000/webjars/swagger-ui/oauth2-redirect.html");
        jsonObject.put("validatorUrl", "");
        Set<String> routeHosts = getServices();
        JSONArray jsonArray = new JSONArray();
        for (String routeHost : routeHosts) {
            JSONObject json = new JSONObject();
            json.put("url", "/" + routeHost + SWAGGER2URL);
            json.put("name", routeHost);
            jsonArray.add(json);
        }
        jsonObject.put("urls", jsonArray);
        return jsonObject;
    }

    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }
}
