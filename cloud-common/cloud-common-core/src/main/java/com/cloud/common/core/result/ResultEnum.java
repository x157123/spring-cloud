package com.cloud.common.core.result;

import lombok.Data;

/**
 * @author liulei
 */

public enum ResultEnum {

    // 数据操作错误定义
    SUCCESS(200, "成功"),
    BODY_NOT_MATCH(400, "请求的数据格式不符"),
    SIGNATURE_NOT_MATCH(401, "请求的数字签名不匹配"),
    NOT_FOUND(404, "未找到该资源"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVER_BUSY(503, "服务器正忙，请稍后再试");

    /**
     * 错误码
     */
    private Integer status;

    /**
     * 错误描述
     */
    private String error;

    ResultEnum(Integer status, String error) {
        this.status = status;
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
