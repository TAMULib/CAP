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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.controller.aspect.annotation.PayloadArgName;
import edu.tamu.cap.exceptions.IRInjectionException;
import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.repo.IRRepo;
import edu.tamu.weaver.context.SpringContext;

@Service
@Scope(value = SCOPE_REQUEST)
public class ArgumentResolver {

    private static final List<String> REQUEST_METHODS_WITH_PAYLOAD = Arrays.asList(new String[] { "POST", "PUT", "PATCH" });

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IRRepo irRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest request;
    
    private Optional<IRService<?>> irService = Optional.empty();

    public void injectIrService(ProceedingJoinPoint joinPoint) throws IRInjectionException, JsonProcessingException, IOException {
        Object[] arguments = joinPoint.getArgs();
        Method method = getMethodFromJoinPoint(joinPoint);
        Optional<IRService<?>> irService = Optional.empty();
        int i = 0;
        for (Parameter parameter : method.getParameters()) {
            irService = getIrService(parameter);
            if (irService.isPresent()) {
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

    public void injectRequestPayload(ProceedingJoinPoint joinPoint) throws IOException, IRInjectionException {
        Method method = getMethodFromJoinPoint(joinPoint);

        boolean hasRequestParam = methodHasRequestParameterAnnotation(method);

        if (!hasRequestParam && REQUEST_METHODS_WITH_PAYLOAD.contains(request.getMethod())) {
            String payload = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.name());
            Optional<JsonNode> payloadNode = getPayloadNode(payload);
            Object[] arguments = joinPoint.getArgs();
            int i = 0;
            for (Parameter parameter : method.getParameters()) {
                Optional<PayloadArgName> payloadArgName = Optional.ofNullable(parameter.getAnnotation(PayloadArgName.class));
                if (!IRService.class.isAssignableFrom(parameter.getType()) && injectArgument(parameter)) {
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

    public void augmentContextUri(ProceedingJoinPoint joinPoint) throws IOException, IRInjectionException {
        Optional<Cookie> cookie = Optional.empty();
        
        Cookie[] cookies = request.getCookies(); 
        
        if(cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("transaction")) {
                    cookie = Optional.of(c);
                    break;
                }
            }
        }
        
        if (cookie.isPresent()) {
            String frx = cookie.get().getValue();
            Method method = getMethodFromJoinPoint(joinPoint);
            Object[] arguments = joinPoint.getArgs();
            int i = 0;
            Optional<IRService<?>> irService = Optional.empty();
            for (Parameter parameter : method.getParameters()) {
                irService = getIrService(parameter);
                if (irService.isPresent()) {
                    break;
                }
            }
            for (Parameter parameter : method.getParameters()) {
                if (Optional.ofNullable(parameter.getAnnotation(Param.class)).isPresent()) {
                    String contextUri = (String) arguments[i];
                    if (!contextUri.contains(frx)) {
                        arguments[i] = contextUri.replace(irService.get().getIR().getRootUri(), frx+"/");
                    }
                    break;
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

    private IR getIRFromRequest() throws JsonProcessingException, IOException, IRInjectionException {
        JsonNode payloadNode = objectMapper.readTree(request.getInputStream());
        return (IR) getArgumentFromBody(IR.class, Optional.empty(), payloadNode);
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

    private Object getArgumentFromBody(Class<?> argClass, Optional<PayloadArgName> payloadArgName, JsonNode payloadNode) throws IRInjectionException {
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
                throw new IRInjectionException("No " + argClass.getSimpleName() + " argument!");
            }
        }
        return argValue.get();
    }

    private Optional<Object> mapObjectFromNode(Class<?> argClass, JsonNode node) {
        Optional<Object> argValue = Optional.empty();
        try {
            if (!IRService.class.isAssignableFrom(argClass)) {
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

    private IRType getIRType() {
        return IRType.valueOf(getPathVariable("type"));
    }

    private Optional<Long> getIRId() {
        Optional<Long> id = Optional.empty();
        try {
            id = Optional.of(Long.parseLong(getPathVariable("irid")));
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
    
    protected Optional<IRService<?>> getIrService(Parameter parameter) throws IRInjectionException,IOException {
        if (!irService.isPresent()) {
            if (IRService.class.isAssignableFrom(parameter.getType())) {
                IRService<?> irs = SpringContext.bean(getIRType().getGloss());
                Optional<Long> irid = getIRId();
                if (irid.isPresent()) {
                    irs.setIr(irRepo.read(irid.get()));
                } else {
                    irs.setIr(getIRFromRequest());
                }
                irService = Optional.of(irs);
            }
        }
        return irService;      
    }

}
