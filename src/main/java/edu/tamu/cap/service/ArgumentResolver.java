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
import edu.tamu.cap.exceptions.RepositoryViewInjectionException;
import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.weaver.context.SpringContext;

@Service
@Scope(value = SCOPE_REQUEST)
public class ArgumentResolver {

    private static final List<String> REQUEST_METHODS_WITH_PAYLOAD = Arrays.asList(new String[] { "POST", "PUT", "PATCH" });

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RepositoryViewRepo rvRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest request;
    
    private Optional<RepositoryViewService<?>> repositoryViewService = Optional.empty();
    
    public void injectRepositoryViewService(ProceedingJoinPoint joinPoint) throws RepositoryViewInjectionException, JsonProcessingException, IOException {
        Object[] arguments = joinPoint.getArgs();
        Method method = getMethodFromJoinPoint(joinPoint);
        Optional<RepositoryViewService<?>> repositoryViewService = Optional.empty();
        int i = 0;
        for (Parameter parameter : method.getParameters()) {
            repositoryViewService = getRepositoryViewService(parameter);
            if (repositoryViewService.isPresent()) {
                break;
            }
            i++;
        }
        if (repositoryViewService.isPresent()) {
            arguments[i] = repositoryViewService.get();
        } else {
            throw new RepositoryViewInjectionException("No Repository View service argument!");
        }
    }

    public void injectRequestPayload(ProceedingJoinPoint joinPoint) throws IOException, RepositoryViewInjectionException {
        Method method = getMethodFromJoinPoint(joinPoint);

        boolean hasRequestParam = methodHasRequestParameterAnnotation(method);

        if (!hasRequestParam && REQUEST_METHODS_WITH_PAYLOAD.contains(request.getMethod())) {
            String payload = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.name());
            Optional<JsonNode> payloadNode = getPayloadNode(payload);
            Object[] arguments = joinPoint.getArgs();
            int i = 0;
            for (Parameter parameter : method.getParameters()) {
                Optional<PayloadArgName> payloadArgName = Optional.ofNullable(parameter.getAnnotation(PayloadArgName.class));
                if (!RepositoryViewService.class.isAssignableFrom(parameter.getType()) && injectArgument(parameter)) {
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

    private RepositoryView getRepositoryViewFromRequest() throws JsonProcessingException, IOException, RepositoryViewInjectionException {
        JsonNode payloadNode = objectMapper.readTree(request.getInputStream());
        return (RepositoryView) getArgumentFromBody(RepositoryView.class, Optional.empty(), payloadNode);
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

    private Object getArgumentFromBody(Class<?> argClass, Optional<PayloadArgName> payloadArgName, JsonNode payloadNode) throws RepositoryViewInjectionException {
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
                throw new RepositoryViewInjectionException("No " + argClass.getSimpleName() + " argument!");
            }
        }
        return argValue.get();
    }

    private Optional<Object> mapObjectFromNode(Class<?> argClass, JsonNode node) {
        Optional<Object> argValue = Optional.empty();
        try {
            if (!RepositoryViewService.class.isAssignableFrom(argClass)) {
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

    private RepositoryViewType getRepositoryViewType() {
        return RepositoryViewType.valueOf(getPathVariable("type"));
    }

    private Optional<Long> getRepositoryViewId() {
        Optional<Long> id = Optional.empty();
        try {
            id = Optional.of(Long.parseLong(getPathVariable("repositoryViewId")));
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
    
    protected Optional<RepositoryViewService<?>> getRepositoryViewService(Parameter parameter) throws RepositoryViewInjectionException,IOException {
        if (!repositoryViewService.isPresent()) {
            if (RepositoryViewService.class.isAssignableFrom(parameter.getType())) {
                RepositoryViewService<?> rvs = SpringContext.bean(getRepositoryViewType().getGloss());
                Optional<Long> repositoryViewId = getRepositoryViewId();
                if (repositoryViewId.isPresent()) {
                    rvs.setRepositoryView(rvRepo.read(repositoryViewId.get()));
                } else {
                    rvs.setRepositoryView(getRepositoryViewFromRequest());
                }
                repositoryViewService = Optional.of(rvs);
            }
        }
        return repositoryViewService;      
    }

}
