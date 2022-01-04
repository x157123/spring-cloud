package com.cloud.common.core.utils;

import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.cloud.common.core.exceptions.DataValidationException;
import org.hibernate.validator.HibernateValidator;

/**
 * @author liulei
 */
public class ValidationUtils {

    /**
     * 使用hibernate的注解来进行验证
     *
     */
    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 数据校验
     * @param obj
     * @param <T>
     */
    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = new HashSet<>();
        try {
            constraintViolations = validator.validate(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            throw new DataValidationException(String.format("参数校验失败:%s", constraintViolations.iterator().next().getMessage()));
        }
    }
}
