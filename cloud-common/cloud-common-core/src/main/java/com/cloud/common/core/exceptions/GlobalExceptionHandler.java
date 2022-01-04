package com.cloud.common.core.exceptions;

import com.cloud.common.core.result.ResultBody;
import com.cloud.common.core.result.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liulei
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = DataException.class)
    @ResponseBody
    public ResultBody dataExceptionHandler(DataException e) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultBody.error(ResultEnum.INTERNAL_SERVER_ERROR, e.getErrorMsg());
    }

    /**
     * 数据校验错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = DataValidationException.class)
    @ResponseBody
    public ResultBody validationExceptionHandler(DataValidationException e) {
        logger.error("数据校验错误！原因是:", e);
        return ResultBody.error(ResultEnum.BODY_NOT_MATCH, e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultBody exceptionHandler(NullPointerException e) {
        logger.error("发生空指针异常！原因是:", e);
        return ResultBody.error(ResultEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 数据参数异常错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpMessageNotReadableException e) {
        logger.error("数据格式错误！原因是:", e);
        return ResultBody.error(ResultEnum.BODY_NOT_MATCH);
    }

    /**
     * 处理其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBody exceptionHandler(Exception e) {
        logger.error("未知异常！原因是:", e);
        return ResultBody.error(ResultEnum.INTERNAL_SERVER_ERROR);
    }
}
