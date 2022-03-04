package io.github.Hattinger04.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	// TODO: Java Logger
	@Around("bean(*Controller)")
	public Object startControllerLog(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("Controller called: " + jp.getSignature());
		Object result;
		try {
			result = jp.proceed();
			System.out.println("Method end: " + jp.getSignature());
			return result; 
		} catch (Throwable e) {
			System.out.println("Method abnormal termination: " + jp.getSignature());
			e.printStackTrace();
			throw e; 
		}
	}
}
