package com.jiuxi.mybatis.core.dynamic;

import java.lang.annotation.*;

/**
 * @ClassName: TargetDataSource
 * @Description: 多数据源的 注解
 * @Author: 杨攀
 * @Date: 2022/2/22 15:00
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TargetDataSource {

    String value();

}
