package com.cloud.common.core.result;

import lombok.Data;

/**
 * @author liulei
 */
@Data
public class ResultBody {

    /**
     * 返回时间
     */
    private Long timestamp = System.currentTimeMillis();

    /**
     * 相应Code
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String error;

    /**
     * 响应结果
     */
    private Object data;


    public static ResultBody error(ResultEnum result) {
        ResultBody resultBody = new ResultBody();
        resultBody.setStatus(result.getStatus());
        resultBody.setError(result.getError());
        return resultBody;
    }

    public static ResultBody error(ResultEnum result, String msg) {
        ResultBody resultBody = new ResultBody();
        resultBody.setStatus(result.getStatus());
        resultBody.setError(msg);
        return resultBody;
    }

    public static ResultBody success(Object result) {
        ResultBody resultBody = new ResultBody();
        resultBody.setStatus(ResultEnum.SUCCESS.getStatus());
//        resultBody.setError(ResultEnum.SUCCESS.getError());
        resultBody.setData(result);
        return resultBody;
    }
}
