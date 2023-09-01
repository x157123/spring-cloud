package org.cloud;

import lombok.Data;

import java.util.List;

@Data
public class ParameterVo {
    private String in;
    private String name;
    private String type;
    private String description;
    private boolean required;
    private String format;
    private List<ParameterVo> children;
}