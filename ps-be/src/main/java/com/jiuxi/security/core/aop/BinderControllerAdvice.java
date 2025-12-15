package com.jiuxi.security.core.aop;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * @Description: 修复 Spring Framework RCE 远程漏洞
 * https://spring.io/blog/2022/03/31/spring-framework-rce-early-announcement
 * @ClassName: BinderControllerAdvice
 * @Author: pdd
 * @Date: 2022-04-19 14:15
 * @Copyright: 2022 Hangzhou Jiuxi Inc. All rights reserved.
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class BinderControllerAdvice {

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        String[] denylist = new String[]{"class.*", "Class.*", "*.class.*", "*.Class.*"};
        dataBinder.setDisallowedFields(denylist);
    }

}
