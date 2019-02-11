package edu.tamu.cap.service;

import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.controller.aspect.annotation.PayloadArgName;
import edu.tamu.cap.exceptions.RVInjectionException;
import edu.tamu.cap.model.RV;
import edu.tamu.cap.model.repo.RVRepo;
import edu.tamu.weaver.context.SpringContext;

@Service
@Scope(value = SCOPE_REQUEST)
public class ArgumentResolver {

    private static final List<String> REQUEST_METHODS_WITH_PAYLOAD = Arrays.asList(new String[] { "POST", "PUT", "PATCH" });

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RVRepo rvRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest request;
    
    private Optional<RVService<?>> rvService = Optional.empty();
    
    public void injectRVService(ProceedingJoinPoint joinPoint) throws RVInjectionException, JsonProcessingException, IOException {
        Object[] arguments = joinPoint.getArgs();
        Method method = getMethodFromJoinPoint(joinPoint);
        Optional<RVService<?>> rvService = Optional.empty();
        int i = 0;
        for (Parameter parameter : method.getParameters()) {
            rvService = getRvService(parameter);
            if (rvService.isPresent()) {
                break;
            }
            i++;
        }
        if (rvService.isPresent()) {
            arguments[i] = rvService.get();
        } else {
            throw new RVInjectionException("No RV service argument!");
        }
    }

    public void injectRequestPayload(ProceedingJoinPoint joinPoint) throws IOException, RVInjectionException {
        Method method = getMethodFromJoinPoint(joinPoint);

        boolean hasRequestParam = methodHasRequestParameterAnnotation(method);

        if (!hasRequestParam && REQUEST_METHODS_WITH_PAYLOAD.contains(request.getMethod())) {
            String payload = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.name());
            Optional<JsonNode> payloadNode = getPayloadNode(payload);
            Object[] arguments = joinPoint.getArgs();
            int i = 0;
            for (Parameter parameter : method.getParameters()) {
                Optional<PayloadArgName> payloadArgName = Optional.ofNullable(parameter.getAnnotation(PayloadArgName.class));
                if (!RVService.class.isAssignableFrom(parameter.getType()) && injectArgument(parameter)) {
                    if (payloadNode.isPresent()) {
                        arguments[i] = getArgumentFromBody(parameter.getType(), payloadArgName, payloadNode.get());
                    } else {
                        arguments[i] = objectMapper.convertValue(payload, parameter.getType());
                    }
                }
                i++;
            }
        }
    }

    private boolean methodHasRequestParameterAnnotation(Method method) {
        boolean hasAnnotation = false;
        for (Parameter parameter : method.getParameters()) {
            if (Optional.ofNullable(parameter.getAnnotation(RequestParam.class)).isPresent()) {
                hasAnnotation = true;
                break;
            }
        }
        return hasAnnotation;
    }

    private RV getRVFromRequest() throws JsonProcessingException, IOException, RVInjectionException {
        JsonNode payloadNode = objectMapper.readTree(request.getInputStream());
        return (RV) getArgumentFromBody(RV.class, Optional.empty(), payloadNode);
    }

    private boolean injectArgument(Parameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();
        boolean inject = annotations.length == 0;
        if (annotations.length == 1) {
            inject = Optional.ofNullable(parameter.getAnnotation(PayloadArgName.class)).isPresent();
        }
        return inject;
    }

    private Method getMethodFromJoinPoint(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }

    private Optional<JsonNode> getPayloadNode(String payload) {
        Optional<JsonNode> payloadNode = Optional.empty();
        try {
            payloadNode = Optional.of(objectMapper.readTree(payload));
        } catch (IOException e) {
            logger.debug("Payload is a string literal!");
        }
        return payloadNode;
    }

    private Object getArgumentFromBody(Class<?> argClass, Optional<PayloadArgName> payloadArgName, JsonNode payloadNode) throws RVInjectionException {
        Optional<Object> argValue = mapObjectFromNode(argClass, payloadNode);
        if (!argValue.isPresent()) {
            Iterator<Map.Entry<String, JsonNode>> iterator = payloadNode.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> jsonNodeEntry = iterator.next();
                if (!payloadArgName.isPresent() || (payloadArgName.isPresent() && jsonNodeEntry.getKey().equals(payloadArgName.get().value()))) {
                    argValue = mapObjectFromNode(argClass, jsonNodeEntry.getValue());
                }
            }
        }
        if (!argValue.isPresent()) {
            if (argClass.equals(String.class)) {
                argValue = Optional.of("");
            } else {
                throw new RVInjectionException("No " + argClass.getSimpleName() + " argument!");
            }
        }
        return argValue.get();
    }

    private Optional<Object> mapObjectFromNode(Class<?> argClass, JsonNode node) {
        Optional<Object> argValue = Optional.empty();
        try {
            if (!RVService.class.isAssignableFrom(argClass)) {
                if (argClass.equals(String.class)) {
                    if (node.isTextual()) {
                        argValue = Optional.of(node.asText());
                    }
                } else {
                    argValue = Optional.of(objectMapper.convertValue(node, argClass));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return argValue;
    }

    private RVType getRVType() {
        return RVType.valueOf(getPathVariable("type"));
    }

    private Optional<Long> getRVId() {
        Optional<Long> id = Optional.empty();
        try {
            id = Optional.of(Long.parseLong(getPathVariable("rvid")));
        } catch (NumberFormatException e) {
            logger.info("Id not provided in path!");
        }
        return id;
    }

    @SuppressWarnings("unchecked")
    private String getPathVariable(String pathKey) {
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return pathVariables.get(pathKey);
    }
    
    protected Optional<RVService<?>> getRvService(Parameter parameter) throws RVInjectionException,IOException {
        if (!rvService.isPresent()) {
            if (RVService.class.isAssignableFrom(parameter.getType())) {
                RVService<?> rvs = SpringContext.bean(getRVType().getGloss());
                Optional<Long> rvid = getRVId();
                if (rvid.isPresent()) {
                    rvs.setRv(rvRepo.read(rvid.get()));
                } else {
                    rvs.setRv(getRVFromRequest());
                }
                rvService = Optional.of(rvs);
            }
        }
        return rvService;      
    }

}
