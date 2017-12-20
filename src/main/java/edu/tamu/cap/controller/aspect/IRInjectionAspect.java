package edu.tamu.cap.controller.aspect;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import edu.tamu.cap.service.ir.IRService;
import edu.tamu.cap.service.ir.IRType;
import edu.tamu.weaver.context.SpringContext;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
public class IRInjectionAspect {

	@Around("@annotation(edu.tamu.cap.controller.aspect.InjectIRService)")
	public ApiResponse runWithInjectedIRService(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] arguments = joinPoint.getArgs();

		Optional<IRType> type = Optional.empty();

		for (Object argument : arguments) {
			if (arguments.getClass().equals(IRType.class)) {
				type = Optional.of((IRType) argument);
			}
		}

		if (type.isPresent()) {
			for (int i = 0; i < arguments.length; i++) {
				if (arguments.getClass().equals(IRService.class)) {
					arguments[i] = SpringContext.bean(type.get().getName());
				}
			}
		} else {
			// TODO: throw exception
		}

		return (ApiResponse) joinPoint.proceed(arguments);
	}

}
