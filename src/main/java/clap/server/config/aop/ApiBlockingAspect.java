package clap.server.config.aop;

import clap.server.common.utils.SpringEnvironmentHelper;
import clap.server.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static clap.server.exception.code.GlobalErrorCode.BLOCKED_API;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiBlockingAspect {

    private final SpringEnvironmentHelper springEnvironmentHelper;

    @Around("@annotation(clap.server.common.annotation.swagger.DevelopOnlyApi)")
    public Object checkApiAcceptingCondition(ProceedingJoinPoint joinPoint) throws Throwable {
        if (springEnvironmentHelper.isProdProfile()) {
            throw new ApplicationException(BLOCKED_API) ;
        }
        return joinPoint.proceed();
    }
}
