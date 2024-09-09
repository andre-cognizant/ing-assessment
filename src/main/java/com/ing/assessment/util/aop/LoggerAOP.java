package com.ing.assessment.util.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("!test")
@Aspect
@Component
@Slf4j
public class LoggerAOP {

	private static final String POINTCUT = "execution(* com.ing.assessment..*(..))";

	@Before(POINTCUT)
	public void logMethodInputs(JoinPoint joinPoint) {
		log.info("Logging method inputs for class: " + joinPoint.getTarget().getClass().getName() + ", method: "
				+ joinPoint.getSignature().getName() + " with args: " + Arrays.toString(joinPoint.getArgs()));
	}

	@AfterReturning(pointcut = POINTCUT, returning = "result")
	public void logMethodOutputs(JoinPoint joinPoint, Object result) {
		log.info("Logging method outputs for class: " + joinPoint.getTarget().getClass().getName() + ", method: "
				+ joinPoint.getSignature().getName() + " with result: " + result);
	}

	@AfterThrowing(pointcut = POINTCUT, throwing = "ex")
	public void logMethodException(JoinPoint joinPoint, Exception ex) {
		log.error("Exception in method for class: " + joinPoint.getTarget().getClass().getName() + ", method: "
				+ joinPoint.getSignature().getName() + " with exception: " + ex.getMessage());
	}

}