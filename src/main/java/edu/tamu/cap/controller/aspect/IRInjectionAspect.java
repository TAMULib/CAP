package edu.tamu.cap.controller.aspect;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.exceptions.IRInjectionException;
import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.repo.IRRepo;
import edu.tamu.cap.service.ir.IRService;
import edu.tamu.cap.service.ir.IRType;
import edu.tamu.weaver.context.SpringContext;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
public class IRInjectionAspect {

	@Autowired
	private IRRepo irRepo;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HttpServletRequest request;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Around("execution(* edu.tamu.cap.controller.IRProxyController.*(..))")
	public ApiResponse runWithInjectedIRService(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.debug("Injecting an IR Service at joinPoint: {}", joinPoint.toString());
		injectRequestPayload(joinPoint);
		injectIrService(joinPoint);
		return (ApiResponse) joinPoint.proceed(joinPoint.getArgs());
	}

	private void injectRequestPayload(ProceedingJoinPoint joinPoint) throws JsonProcessingException, IOException, IRInjectionException {
		JsonNode payloadNode = objectMapper.readTree(request.getInputStream());
		Object[] arguments = joinPoint.getArgs();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		int i = 0;
		for (Parameter parameter : method.getParameters()) {
			if (!parameter.getType().equals(IRService.class)) {
				arguments[i] = getArgumentFromBody(parameter.getType(), payloadNode);
			}
			i++;
		}
	}

	private Object getArgumentFromBody(Class<?> argClass, JsonNode payloadNode) throws IRInjectionException {
		Optional<Object> argValue = mapObjectFromNode(argClass, payloadNode);
		if (!argValue.isPresent()) {
			Iterator<JsonNode> iterator = payloadNode.elements();
			while (iterator.hasNext()) {
				argValue = mapObjectFromNode(argClass, iterator.next());
			}
		}
		if (!argValue.isPresent()) {
			throw new IRInjectionException("No " + argClass.getSimpleName() + " argument!");
		}
		return argValue.get();
	}

	private Optional<Object> mapObjectFromNode(Class<?> argClass, JsonNode node) {
		Optional<Object> argValue = Optional.empty();
		try {
			if (!argClass.equals(IRService.class)) {
				if (argClass.equals(String.class)) {
					argValue = Optional.of(node.asText());
				} else {
					argValue = Optional.of(objectMapper.convertValue(node, argClass));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return argValue;
	}

	private IR getIRArgument(ProceedingJoinPoint joinPoint) throws IRInjectionException {
		Optional<IR> ir = Optional.empty();
		Object[] arguments = joinPoint.getArgs();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		int i = 0;
		for (Parameter parameter : method.getParameters()) {
			if (parameter.getType().equals(IR.class)) {
				ir = Optional.of((IR) arguments[i]);
			}
			i++;
		}
		if (!ir.isPresent()) {
			throw new IRInjectionException("No IR model type available!");
		}
		return ir.get();
	}

	private void injectIrService(ProceedingJoinPoint joinPoint) throws IRInjectionException {
		Object[] arguments = joinPoint.getArgs();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Optional<IRService> irService = Optional.empty();
		int i = 0;
		for (Parameter parameter : method.getParameters()) {
			if (parameter.getType().equals(IRService.class)) {
				IRService irs = SpringContext.bean(getIRType().getGloss());

				Long irid = null;

				try {
					irid = getIRId();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

				if (irid != null) {
					irs.setIr(irRepo.read(irid));
				} else {
					irs.setIr(getIRArgument(joinPoint));
				}

				irService = Optional.of(irs);
				break;
			}
			i++;
		}
		if (irService.isPresent()) {
			arguments[i] = irService.get();
		} else {
			throw new IRInjectionException("No IR service argument!");
		}
	}

	private IRType getIRType() {
		return IRType.valueOf(getPathVariable("type"));
	}

	private Long getIRId() {
		return Long.parseLong(getPathVariable("irid"));
	}

	private String getPathVariable(String pathKey) {
		@SuppressWarnings("unchecked")
		Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		return pathVariables.get(pathKey);
	}

}
