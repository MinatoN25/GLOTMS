package com.glotms.ticketservice.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	public static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

//	@Around("execution(* com.glotms.ticketservice.service.*.*(..))")
//	public void beforeAllServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
//
//		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//
//		String className = methodSignature.getDeclaringType().getSimpleName();
//		String methodName = methodSignature.getName();
//		logger.info(methodName + " is executing of class " + className);
//		joinPoint.proceed();
//		logger.info(methodName + " is executed successfully in class " + className);
//
//	}

	@AfterThrowing(pointcut = "execution(* com.glotms.ticketservice.*.*.*(..))", throwing = "error")
	public void afterThrowing(JoinPoint joinPoint, Throwable error) {

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

		String className = methodSignature.getDeclaringType().getSimpleName();
		String methodName = methodSignature.getName();
		logger.error("Exception occured in " + methodName + " of class " + className);
		logger.error("Exception", error);
	}

}
