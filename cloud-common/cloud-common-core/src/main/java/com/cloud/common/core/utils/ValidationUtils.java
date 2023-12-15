package com.cloud.common.core.utils;

import com.cloud.common.core.exceptions.DataValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;

import java.util.HashSet;
import java.util.Set;

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
        if (!constraintViolations.isEmpty()) {
            throw new DataValidationException(String.format("参数校验失败:%s", constraintViolations.iterator().next().getMessage()));
        }
    }
}
