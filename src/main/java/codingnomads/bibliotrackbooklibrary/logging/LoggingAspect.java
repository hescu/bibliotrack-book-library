package codingnomads.bibliotrackbooklibrary.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    private void executeLogging() { }

    @Pointcut("execution(public * codingnomads.bibliotracklibrary.service.*.*(..))")
    private void publicMethodsFromServicePackage() { }

//    @Before("executeLogging()")
//    public void logBefore(JoinPoint joinPoint) {
//        StringBuilder message = new StringBuilder("Method Name : ");
//        message.append(joinPoint.getSignature().getName());
//        final Object[] args = joinPoint.getArgs();
//        if (args != null && args.length > 0) {
//            message.append("method args=|");
//            Arrays.asList(args).forEach(arg -> {
//                message.append("arg=").append(arg).append("|");
//            });
//        }
//        LOGGER.info(message.toString());
//    }

    @Before("annotation(Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.debug(">> {}() - {}", methodName, Arrays.toString(args));
    }

    @AfterReturning(value = "publicMethodsFromServicePackage()", returning = "result")
    public void logAfterServiceMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        LOGGER.debug("<< {}() - {}", methodName, result);
    }
}
