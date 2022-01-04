package com.cloud.common.core.exceptions;

import lombok.Data;

/**
 * @author liulei
 */
@Data
public class DataValidationException extends RuntimeException {
    /**
     * 错误信息
     */
    protected String errorMsg;


    public DataValidationException(String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
    }
}
