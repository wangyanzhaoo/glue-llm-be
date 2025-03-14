package com.rune.log.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rune.annotation.OperationLogging;
import com.rune.log.domain.entity.OperationLog;
import com.rune.log.service.OperationLogService;
import com.rune.utils.IpUtil;
import com.rune.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author avalon
 * @date 22/8/5 11:37
 * @description 操作日志切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final HttpServletRequest request;

    private final OperationLogService logService;

    private final ObjectMapper objectMapper;

    /**
     * 切面开始
     *
     * @param joinPoint /
     * @return /
     * @throws Throwable /
     */
    @Around("execution(@(@com.rune.annotation.OperationLogging *) * *(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            saveLog(joinPoint, startTime, result, null);
            return result;
        } catch (Throwable e) {
            saveLog(joinPoint, startTime, null, e.getMessage());
            throw e;
        }
    }

    /**
     * 保存日志
     *
     * @param joinPoint /
     * @param startTime 开始时间
     * @param result    /
     * @param errorMsg  错误信息
     */
    private void saveLog(ProceedingJoinPoint joinPoint, Long startTime, Object result, String errorMsg) throws JsonProcessingException {

        // 结束时间
        long executionTime = System.currentTimeMillis() - startTime;

        // 获取目标方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取操作日志注解 备注： AnnotationUtils.findAnnotation 方法无法获取继承的属性
        OperationLogging operationLogging = AnnotatedElementUtils.findMergedAnnotation(method, OperationLogging.class);
        // 获取操作日志 DTO
        Assert.notNull(operationLogging, "日志注解不能为空");

        OperationLog operationLog = new OperationLog();
        operationLog.setType(operationLogging.type().getValue());
        operationLog.setUsername(SecurityUtils.getCurrentUsername());
        operationLog.setMsg(operationLogging.msg());
        operationLog.setIp(IpUtil.getIp(request));
        operationLog.setMethod(request.getMethod());
        operationLog.setUrl(request.getRequestURI());
        operationLog.setStatus(true);
        operationLog.setTime(executionTime);
        operationLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        // 请求参数
        if (operationLogging.recordParams()) {
            operationLog.setParams(objectMapper.writeValueAsString(joinPoint.getArgs()));
        }
        // 返回参数
        if (operationLogging.recordResult()) {
            operationLog.setResult(objectMapper.writeValueAsString(result));
        }
        // 请求失败
        if (errorMsg != null) {
            operationLog.setStatus(false);
            operationLog.setErrorMsg(errorMsg);
        }

        // 异步保存
        logService.create(operationLog);
    }
}
