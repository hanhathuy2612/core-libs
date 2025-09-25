package com.hnh.enterprise.core.logging;

import com.hnh.enterprise.core.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * LoggingAspect
 */
@Slf4j
@Aspect
public class LoggingAspect {

    @Pointcut("@within(com.hnh.enterprise.core.logging.Logging) || @annotation(com.hnh.enterprise.core.logging.Logging)")
    public void applicationPackagePointcut() {
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().toShortString(),
                joinPoint.getSignature().getName(), MapperUtil.convertToString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().toShortString(),
                    joinPoint.getSignature().getName(), MapperUtil.convertToString(result));
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", MapperUtil.convertToString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().toShortString(),
                joinPoint.getSignature().getName(), getErrorMessage(e));
    }

    private Object getErrorMessage(Throwable e) {
        if (e.getCause() != null) {
            return e.getCause();
        }
        if (e.getMessage() != null) {
            return e.getMessage();
        }
        return e;
    }
}
