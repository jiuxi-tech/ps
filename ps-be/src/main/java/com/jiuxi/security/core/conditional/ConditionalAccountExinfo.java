package com.jiuxi.security.core.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Description: 自定义Condition注解实现
 * @ClassName: ConditionalAccountExinfo
 * @Author: pdd
 * @Date: 2020-09-14 11:34
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class ConditionalAccountExinfo implements Condition {

    /**
     * 判断是否配置了${topinfo.security.authentication.errCount}值，如果配置了，并且值为-1，返回false，加该注解的类不实例化，如果没有值，或者值不等于-1，实例化。
     *
     * @param context:
     * @param metadata:
     * @return boolean
     * @author pand
     * @date 2020-09-14 11:34
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        Integer property = environment.getProperty("topinfo.security.authentication.errCount", Integer.class);
        if (null != property && -1 == property) {
            return false;
        }
        return true;
    }
}
