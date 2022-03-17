package io.github.Hattinger04.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	private static Logger logger;
	static {
		PatternLayout layout = new PatternLayout();
		String conversionPattern = "%-7p %d [%t] %c %x - %m%n";
		layout.setConversionPattern(conversionPattern);

		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setLayout(layout);
		consoleAppender.activateOptions();

		FileAppender fileAppender = new FileAppender();
		fileAppender.setFile("src/main/resources/log4j.log");
		fileAppender.setLayout(layout);
		fileAppender.activateOptions();

		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);
		rootLogger.addAppender(consoleAppender);
		rootLogger.addAppender(fileAppender);
		
		logger = Logger.getLogger(LogAspect.class); 
	}

	
	@Around(("execution(* io.github.Hattinger04.user.UserController.logoutPage(..))"))
	public Object logoutLog(ProceedingJoinPoint jp) throws Throwable {
		Object o; 
		HttpServletRequest request = (HttpServletRequest) jp.getArgs()[0]; 
		String username = request.getRemoteUser(); 
		System.out.println(username);
		o = jp.proceed(); 
		logger.info("User " + username + " logged out"); 
		return o; 
	}
	
}