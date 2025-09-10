package com.jiuxi.admin.core.controller.pc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @ClassName: LoggerLevelController
 * @Description: 动态修改日志级别
 * @Author: 杨攀
 * @Date: 2023/4/25 16:41
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/platform/loggerLevel")
public class LoggerLevelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerLevelController.class);

    private static final String TICKET = "topinfo";

    private static final List<String> LOGGER_LEVEL =  Arrays.asList("error","warn","info","debug");

    /**
     * 调整日志接口
     * @author 杨攀
     * @date 2023/4/25 16:25
     * @param ticket 票据
     * @param className 全类名，如：com.jiuxi.admin.core.controller.pc.LoadRunnerController
     * @param logLevel 日志级别： ERROR、WARN、INFO、DEBUG
     * @return com.jiuxi.common.bean.JsonResponse
     *
     * <pre>
     *     http://ip:端口/项目名/platform/loggerLevel/update?ticket=topinfo&className=com.jiuxi.admin.core.controller.pc.LoadRunnerController&logLevel=info
     * </pre>
     *
     */
    @RequestMapping("/update")
    public JsonResponse update(String ticket, String className, String logLevel){

        if(!TICKET.equals(ticket)){
            return JsonResponse.buildFailure("非法请求！ticket不合法。");
        }

        String level = logLevel.toLowerCase(Locale.ROOT);
        if(!LOGGER_LEVEL.contains(level)){
            return JsonResponse.buildFailure("非法日志级别！");
        }


        try {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            context.getLogger(className).setLevel(Level.valueOf(logLevel));
            return JsonResponse.buildSuccess("动态日志级别修改成功！");
        }catch (Exception e){
            LOGGER.error("动态修改日志级别失败！原因:{}", ExceptionUtils.getStackTrace(e));
        }
        return JsonResponse.buildFailure("动态修改日志级别失败！");
    }

}
