package com.capgemini.wsb.fitnesstracker.aop.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
              " || within(@org.springframework.stereotype.Service *)" +
              " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        				// Punkt cięcia dla komponentów Springa
    }

    @Around("springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();

        log.info("Enter: {}.{}() with argument[s] = {}", className, methodName, Arrays.toString(methodArgs));

        try {
            Object result = joinPoint.proceed();

            log.info("Exit: {}.{}() with result = {}", className, methodName, result);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(methodArgs), className, methodName);

            throw e;
        }
    }
}
