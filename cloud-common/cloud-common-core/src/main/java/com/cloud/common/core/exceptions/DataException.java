package com.cloud.common.core.exceptions;

import lombok.Data;

/**
 * @author liulei
 */
@Data
public class DataException extends RuntimeException {
    /**
     * 错误信息
     */
    protected String errorMsg;

    public DataException(String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
    }
}
