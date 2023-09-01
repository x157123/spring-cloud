package org.cloud;

import lombok.Data;

import java.util.List;

@Data
public class PathInfoVo {

    //请求路劲
    private String pathUrl;
    //请求方式
    private String httpMethod;
    private String operationId;
    //概要
    private String summary;
    //描述
    private String description;

    private List<ParameterVo> reqList;

    private List<ResponseVo> respList;
}