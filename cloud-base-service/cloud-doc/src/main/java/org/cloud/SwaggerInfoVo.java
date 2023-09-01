package org.cloud;


import lombok.Data;

import java.util.Map;

@Data
public class SwaggerInfoVo {

    private String version;
    private String title;
    private String description;
    private Map<String, Object> license;
}