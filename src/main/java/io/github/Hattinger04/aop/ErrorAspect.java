package io.github.Hattinger04.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorAspect {
	@AfterThrowing(value="execution(* *..*..*(..))" + " && (bean(*Controller) || bean(*Service) || bean(*Repository))", throwing="ex")
	public void throwingException(Exception ex) {
		System.out.println("==============================");
		System.out.println("Exception has occurred. :" + ex.getMessage());
		System.out.println("==============================");
	}
}
