package at.ac.htlinn.aop;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	// TODO: is logger really needed when using nginx? 
	
	private static Logger logger;
	static {
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.ALL);
	}

	@After(("execution(* at.ac.htlinn.configuration.AuthController.login(..))"))
	public void loginLog(JoinPoint jp) throws Throwable {
		FileHandler fileHandler = new FileHandler("src/main/resources/allLogs.log", true);
		logger.addHandler(fileHandler);
		SimpleFormatter formatter = new SimpleFormatter(); 
		fileHandler.setFormatter(formatter); 
		logger.fine(String.format("%s - [logged in]", jp.getArgs()[0])); 
		logger.removeHandler(fileHandler); 
		fileHandler.close(); 
	}
	
	@Around(("execution(* at.ac.htlinn.user.UserController.logoutPage(..))"))
	public Object logoutLog(ProceedingJoinPoint jp) throws Throwable {
		Object o; 
		HttpServletRequest request = (HttpServletRequest) jp.getArgs()[0]; 
		String ip = request.getRemoteAddr();
		int port = request.getRemotePort(); 
		String username = request.getRemoteUser(); 
		o = jp.proceed(); 
		FileHandler fileHandler = new FileHandler("src/main/resources/allLogs.log", true);
		logger.addHandler(fileHandler);
		SimpleFormatter formatter = new SimpleFormatter(); 
		fileHandler.setFormatter(formatter); 
		logger.fine(String.format("%s - %s - %d 	[logged out]", username, ip, port)); 
		logger.removeHandler(fileHandler); 
		fileHandler.close();
		return o; 
	}
}