package com.jd.boot001.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 自定义日志拦截器
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    private final static String INVOKE_NO = "invokeNo";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放SessionId
        String token = UUID.randomUUID().toString().replaceAll("-", "");//.toUpperCase();
        MDC.put(INVOKE_NO, token);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 最后执行MDC删除
        MDC.remove(INVOKE_NO);
    }

}
