package com.jiuxi.mybatis.core.dynamic;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * @ClassName: DynamicDataSourceAspect
 * @Description: 拦截器
 * @Author: 杨攀
 * @Date: 2022/2/22 15:08
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
// 加上@Order后，让此切面优先于事务的切面执行
// @Order是很重要的，必须确保DynamicDataSourceAspect的执行优先于TranctionInterceptor。否则数据源会以默认的数据源执行
@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
public class DynamicDataSourceAspect {

    //@within在类上设置
    //@annotation在方法上进行设置
    @Pointcut(value = "@within(com.jiuxi.mybatis.core.dynamic.TargetDataSource) || @annotation(com.jiuxi.mybatis.core.dynamic.TargetDataSource)")
    public void pointcut() {
    }

    /*
    @Before("pointcut()")
    public void beforeMethod(JoinPoint joinPoint){

        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        //获取方法上的注解
        TargetDataSource annotationClass = method.getAnnotation(TargetDataSource.class);
        if(annotationClass == null){
            annotationClass = joinPoint.getTarget().getClass().getAnnotation(TargetDataSource.class);//获取类上面的注解
            if(annotationClass == null) return;
        }

        //获取注解上的数据源的值的信息
        String dataSourceKey =  annotationClass.value();

        if(StrUtil.isBlank(dataSourceKey)){
            throw new TopinfoRuntimeException(-116, "无效的数据源key");
        }

        // 设置数据源
        DynamicDataSourceContextHolder.setDataSourceType(dataSourceKey);
    }*/

    @Around("pointcut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        try {

            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            // 获取方法上的注解
            TargetDataSource annotationClass = method.getAnnotation(TargetDataSource.class);
            if (annotationClass == null) {
                // 获取类上面的注解
                annotationClass = joinPoint.getTarget().getClass().getAnnotation(TargetDataSource.class);
                if (annotationClass == null) {
                    return null;
                }
            }

            //获取注解上的数据源的值的信息
            String dataSourceKey = annotationClass.value();

            if (StrUtil.isBlank(dataSourceKey)) {
                throw new TopinfoRuntimeException(-116, "无效的数据源key");
            }

            // 设置数据源
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceKey);
            // 执行目标方法
            return joinPoint.proceed();
        } finally {
            // 清空 数据源，使用默认数据源
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }
}
