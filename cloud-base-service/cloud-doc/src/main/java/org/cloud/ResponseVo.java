package org.cloud;

import lombok.Data;

import java.util.List;

@Data
public class ResponseVo {
    private String name;
    private String description;
    private String type;
    private boolean required;
    private String format;
    private List<ResponseVo> children;
}