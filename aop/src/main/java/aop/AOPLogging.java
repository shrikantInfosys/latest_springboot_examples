package aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AOPLogging {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String LOGGING_METHOD_NAME = "fetchLoggingFields";
	public static final String LOG_MESSAGE = "Processing request";
	
	@Around("execution(@org.springframework.web.bind.annotation.GetMapping * *(..)) " +
	"|| execution(@org.springframework.web.bind.annotation.PostMapping * *(..)) " +
	"|| execution(@org.springframework.web.bind.annotation.PutMapping * *(..)) " +
	"|| execution(@org.springframework.web.bind.annotation.DeleteMapping * *(..)) " )
	public Object logMetrics(final ProceedingJoinPoint proceedingJointPoint) throws Throwable {
		
		String serviceName = proceedingJointPoint.getSignature() !=null ? proceedingJointPoint.getSignature().getName() : "";
		long startTime = System.currentTimeMillis();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		logRequest(proceedingJointPoint, request);
		
		RequestContextHolder.currentRequestAttributes().setAttribute("REQUEST_START_TIME", startTime, 0);
		RequestContextHolder.currentRequestAttributes().setAttribute("REQUEST_SERVICE_NAME", serviceName, 0);
		
		Object value = proceedingJointPoint.proceed();
		long executionTime = System.currentTimeMillis() - startTime;
		
		Map<String, Object> logMap = new HashMap<>();
		logMap.put("serviceName", serviceName);
		//getLoggerMap(request, logMap);
		logMap.put("methodResult", "success");
		if(value instanceof ResponseEntity) {
			logMap.put("httpStatus", String.valueOf(((ResponseEntity) value).getStatusCode().value()));
		}
		
		logMap.put("duration", String.valueOf(executionTime));
		//logger.info("success request details", logger.v("details", logMap));
		
		return value;
	}

	private void logRequest(ProceedingJoinPoint proceedingJointPoint, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Object[] args = proceedingJointPoint.getArgs();
		try {
			switch (request.getMethod()) {
			case "POST":
				logRequest(args[0]);
				break;
			case "PUT":
				logRequest(args[1]);
				break;	
			default:
				//log.info(LOG_MESSAGE);
			}
		} catch (Exception ex) {
			//log.warn("Error fetching request details, exception : {}", ex.getMessage());
		}
	}

	private void logRequest(Object request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		// TODO Auto-generated method stub
		Method method = request.getClass().getMethod(LOGGING_METHOD_NAME);
		//log.info(LOG_MESSAGE, v("details", method.invoke(request)));
	}
}
