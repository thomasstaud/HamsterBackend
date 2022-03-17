package io.github.Hattinger04.aop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	File file = new File("src/main/resources/hamster/logging/log.txt"); 
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    BufferedWriter writer;
    Date date;

    
	@Around(("execution(* io.github.Hattinger04.user.UserController.logoutPage(..))"))
	public Object startControllerLog(ProceedingJoinPoint jp) throws Throwable {
		Object result;
		writer = new BufferedWriter(new FileWriter(file, true)); 
    	for(Object o : jp.getArgs()) {
    		System.out.println("Object: " + o.toString());
    	}
		try {
			result = jp.proceed();
			date = new Date(System.currentTimeMillis()); 
			System.out.println("User logged out: " + jp.getSignature());
			writer.append("Logged out at: " + formatter.format(date) + "\n"); // No username or smth like that yet
			writer.close();
			return result; 
		} catch (Throwable e) {
			System.out.println("Method abnormal termination: " + jp.getSignature());
			e.printStackTrace();
			throw e; 
		}
	}
}