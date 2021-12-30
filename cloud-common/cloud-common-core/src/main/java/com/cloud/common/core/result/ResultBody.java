package com.cloud.common.core.result;

import lombok.Data;

/**
 * @author liulei
 */
@Data
public class ResultBody {

    /**
     * 相应Code
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应结果
     */
    private Object result;


    public static ResultBody error(ResultEnum result) {
        ResultBody resultBody = new ResultBody();
        resultBody.setCode(result.getResultCode());
        resultBody.setMsg(result.getResultMsg());
        return resultBody;
    }

    public static ResultBody error(ResultEnum result, String msg) {
        ResultBody resultBody = new ResultBody();
        resultBody.setCode(result.getResultCode());
        resultBody.setMsg(msg);
        return resultBody;
    }

    public static ResultBody success(Object result) {
        ResultBody resultBody = new ResultBody();
        resultBody.setCode(ResultEnum.SUCCESS.getResultCode());
        resultBody.setMsg(ResultEnum.SUCCESS.getResultMsg());
        resultBody.setResult(result);
        return resultBody;
    }
}
