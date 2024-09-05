package com.ing.assessment.util.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggerAOP {

    @Before("execution(* com.ing.assessment..*(..))")
    public void logMethodInputs(JoinPoint joinPoint) {
        log.info("Logging method inputs for class: " + joinPoint.getTarget().getClass().getName() +
                ", method: " + joinPoint.getSignature().getName() + " with args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* com.ing.assessment..*(..))", returning = "result")
    public void logMethodOutputs(JoinPoint joinPoint, Object result) {
        log.info("Logging method outputs for class: " + joinPoint.getTarget().getClass().getName() +
                ", method: " + joinPoint.getSignature().getName() + " with result: " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.ing.assessment..*(..))", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Exception ex) {
        log.error("Exception in method for class: " + joinPoint.getTarget().getClass().getName() +
                ", method: " + joinPoint.getSignature().getName() + " with exception: " + ex.getMessage());
    }
}