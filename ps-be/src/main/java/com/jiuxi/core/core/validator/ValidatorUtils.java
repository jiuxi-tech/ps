package com.jiuxi.core.core.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @description:  hibernate-validator校验工具类
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 * @author 杨攀
 * @date 2020/7/20 14:53
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws IllegalArgumentException  校验不通过，则报异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws IllegalArgumentException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
        	ConstraintViolation<Object> constraint = (ConstraintViolation<Object>)constraintViolations.iterator().next();
            throw new IllegalArgumentException(constraint.getMessage());
        }
    }
}
