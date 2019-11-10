package com.github.liuyehcf.framework.rpc.maple.test.consumer;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author hechenfeng
 * @date 2019/3/24
 */
@Component
@Aspect
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Around("bean(*_MAPLE_CONSUMER_BEAN)")
    public Object hsfConsumer(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = null;
        String methodName = null;
        Object[] args = null;
        Object result = null;
        String localIp = null;
        String remoteIp = null;

        try {
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = methodSignature.getMethod();

            className = methodSignature.getDeclaringTypeName();
            methodName = method.getName();
            args = proceedingJoinPoint.getArgs();

            result = proceedingJoinPoint.proceed(args);
        } catch (Throwable e) {
            LOGGER.error("className={}; methodName={}; errorMsg={}", className, methodName, e.getMessage(), e);
            throw e;
        } finally {
            String argsJson = null;
            String resultJson = null;
            try {
                argsJson = JSON.toJSONString(args);
                resultJson = JSON.toJSONString(result);
            } catch (Throwable e) {
                LOGGER.error("Serialize args or result error. errorMsg={}", e.getMessage(), e);
            }
            long useTime = System.currentTimeMillis() - startTime;

            LOGGER.info("className={}; methodName={}; localIp={}; remoteIp={}; params={}; result={}; useTime={}ms",
                    className, methodName, localIp, remoteIp, argsJson, resultJson, useTime);
        }
        return result;
    }
}
