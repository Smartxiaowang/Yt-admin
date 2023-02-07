package com.zcloud.newsbt.until;

import org.apache.http.HttpHeaders;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @ClassName LogOperationAspect
 * @Description TODO
 * @Author Dear lin
 * @Date 17:14 2023/2/6/006
 * @Version 1.0
 **/
@Aspect
@Component
public class LogOperationAspect {

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        try {
            //执行方法
            Object result = point.proceed();

            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveLog(point, time, 1);

            return result;
        }catch(Exception e) {
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveLog(point, time, 0);
            throw e;
        }
    }
    private void saveLog(ProceedingJoinPoint joinPoint, long time, Integer status) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        LogOperation annotation = method.getAnnotation(LogOperation.class);

        SysLogOperationEntity log = new SysLogOperationEntity();
        if(annotation != null){
            //注解上的描述
            log.setOperation(annotation.value());
        }

        //登录用户信息
        /*UserDetail user = SecurityUser.getUser();
        if(user != null){
            log.setCreatorName(user.getUsername());
        }*/

        log.setStatus(status);
        log.setRequestTime((int)time);

        //请求相关信息
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.setIp(IpUtils.getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setRequestUri(request.getRequestURI());
        log.setRequestMethod(request.getMethod());

        //请求参数
        Object[] args = joinPoint.getArgs();
        try{
            String params = JsonUtils.toJsonString(args[0]);
            log.setRequestParams(params);
        }catch (Exception e){

        }

        //保存到DB
    }
}
