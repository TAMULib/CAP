package edu.tamu.cap.controller.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.exceptions.IRInjectionException;
import edu.tamu.cap.model.IR;
import edu.tamu.cap.service.ir.IRService;
import edu.tamu.cap.service.ir.IRType;
import edu.tamu.weaver.context.SpringContext;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
public class IRInjectionAspect {
	
	@Autowired
    private ObjectMapper objectMapper;

	@Around("@annotation(edu.tamu.cap.controller.aspect.InjectIRService)")
	public ApiResponse runWithInjectedIRService(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("\n\n***Injecting an IR Service at joinPoint "+ joinPoint.toLongString());
		Optional<IR> ir = getIRArgument(joinPoint.getArgs());
		if (ir.isPresent() && ir.get().getType() != null) {
			return (ApiResponse) joinPoint.proceed(injectIrService(joinPoint, ir.get().getType()));
		} else {
			throw new IRInjectionException("No IR model type available!");
		}

	}

	private Optional<IR> getIRArgument(Object[] arguments) {
		
		System.out.println("\n\n Aspect \n\n");
		
		Optional<IR> ir = Optional.empty();
		for (Object argument : arguments) {
			if (argument.getClass().isAssignableFrom(IR.class)) {
				ir = Optional.of((IR) argument);
				System.out.println("IR to inject will be the entirety of request body: " + ir.toString());
				
			} else  if(argument instanceof Map) {
				Map<String, Object> data = (Map<String, Object>) argument;
				
				
				System.out.println("data.keySet() "+data.keySet().size());
				data.keySet().forEach(key->{
					System.out.println("key.toString() "+key.toString());
				});
				
				ir = Optional.of((IR) objectMapper.convertValue(data.get("ir"), IR.class));
				System.out.println("IR to inject will be just the ir node on request body: " + ir.toString());
			}
		}
		System.out.println("Based on injection logic, will return ir: " + ir );
		return ir;
	}

	private Object[] injectIrService(ProceedingJoinPoint joinPoint, IRType irType) throws IRInjectionException {
		Object[] arguments = joinPoint.getArgs();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Optional<IRService> irService = Optional.empty();
		int i = 0;
		for (Parameter parameter : method.getParameters()) {
			if (parameter.getType().equals(IRService.class)) {
				irService = Optional.of(SpringContext.bean(irType.getGloss()));
				break;
			}
			i++;
		}
		if (irService.isPresent()) {
			arguments[i] = irService.get();
		} else {
			throw new IRInjectionException("No IR service argument!");
		}
		return arguments;
	}

}
