package com.cloud.common.core.exceptions;

import com.cloud.common.core.result.ResultBody;
import com.cloud.common.core.result.ResultEnum;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResultBody dataExceptionHandler(DataException e, HttpServletResponse response) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        ResultBody resultBody = ResultBody.error(ResultEnum.INTERNAL_SERVER_ERROR, e.getErrorMsg());
        setError(resultBody, response);
        return resultBody;
    }

    /**
     * 数据校验错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = DataValidationException.class)
    @ResponseBody
    public ResultBody validationExceptionHandler(DataValidationException e, HttpServletResponse response) {
        logger.error("数据校验错误！原因是:", e);
        ResultBody resultBody = ResultBody.error(ResultEnum.BODY_NOT_MATCH, e.getErrorMsg());
        setError(resultBody, response);
        return resultBody;
    }

    /**
     * 处理空指针的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultBody exceptionHandler(NullPointerException e, HttpServletResponse response) {
        logger.error("发生空指针异常！原因是:", e);
        ResultBody resultBody = ResultBody.error(ResultEnum.INTERNAL_SERVER_ERROR);
        setError(resultBody, response);
        return resultBody;
    }

    /**
     * 数据参数异常错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpMessageNotReadableException e, HttpServletResponse response) {
        logger.error("数据格式错误！原因是:", e);
        ResultBody resultBody = ResultBody.error(ResultEnum.BODY_NOT_MATCH);
        setError(resultBody, response);
        return resultBody;
    }

    /**
     * 处理其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBody exceptionHandler(Exception e, HttpServletResponse response) {
        logger.error("未知异常！原因是:", e);
        ResultBody resultBody = ResultBody.error(ResultEnum.INTERNAL_SERVER_ERROR);
        setError(resultBody, response);
        return resultBody;
    }

    /**
     * 封装数据信息
     * @param resultBody
     * @param response
     */
    private void setError(ResultBody resultBody, HttpServletResponse response) {
        try {
            //设置指定错误码
            response.setStatus(resultBody.getStatus());
        } catch (Exception e) {
            logger.error("异常:", e);
        }
    }
}
