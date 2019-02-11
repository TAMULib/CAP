package edu.tamu.cap.controller.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import edu.tamu.cap.service.ArgumentResolver;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
public class RVInjectionAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Lazy
    @Autowired
    private ArgumentResolver argumentResolver;

    @Around("execution(* edu.tamu.cap.controller.VerifyRVSettingsController.*(..))")
    public ApiResponse testRVSettingsInjection(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("Injecting an VR Service at joinPoint: {}", joinPoint.toString());
        // inject appropriate VR service
        argumentResolver.injectRVService(joinPoint);
        return (ApiResponse) joinPoint.proceed(joinPoint.getArgs());
    }

    @Around("execution(* edu.tamu.cap.controller.rvcontext..*(..))")
    public ApiResponse rvContextInjection(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("Injecting an RV Service at joinPoint: {}", joinPoint.toString());
        // inject applicable end point arguments from request body
        argumentResolver.injectRequestPayload(joinPoint);
        // inject appropriate RV service
        argumentResolver.injectRVService(joinPoint);
        return (ApiResponse) joinPoint.proceed(joinPoint.getArgs());
    }

}
