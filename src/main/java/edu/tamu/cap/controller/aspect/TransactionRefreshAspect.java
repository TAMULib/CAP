package edu.tamu.cap.controller.aspect;

import static edu.tamu.weaver.response.ApiAction.BROADCAST;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.controller.aspect.annotation.PayloadArgName;
import edu.tamu.cap.exceptions.RVInjectionException;
import edu.tamu.cap.model.RV;
import edu.tamu.cap.model.repo.RVRepo;
import edu.tamu.cap.model.rvcontext.TransactionDetails;
import edu.tamu.cap.service.RVService;
import edu.tamu.cap.service.RVType;
import edu.tamu.cap.service.TransactingRVService;
import edu.tamu.weaver.context.SpringContext;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
public class TransactionRefreshAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RVRepo rvRepo;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private HttpServletRequest request;

    @AfterReturning(pointcut = "execution(* edu.tamu.cap.controller.rvcontext..*(..))", returning="apiResponse")
    public void refreshTransaction(JoinPoint joinPoint, ApiResponse apiResponse) throws Throwable {
                
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
        
        String httpMethod = request.getMethod();
        
        if(cookie.isPresent() && (httpMethod.equals("POST") || httpMethod.equals("PUT") || httpMethod.equals("DELETE"))) {
            
            JsonNode cookieObject = objectMapper.readTree(URLDecoder.decode(cookie.get().getValue(), "UTF-8"));
            String transactionToken = cookieObject.get("transactionToken").asText();
            
            String transactionExpiration = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now().plusSeconds(180));
            
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();

            Optional<TransactingRVService<?>> transactingRVService = Optional.empty();
            for (Parameter parameter : method.getParameters()) {
                Optional<RVService<?>> rvService = Optional.empty();
                
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
                
                
                if (rvService.isPresent()) {
                    transactingRVService = Optional.of((TransactingRVService<?>) rvService.get());
                    break;
                }
            }
            
            TransactionDetails transactionDetails = transactingRVService.get().makeTransactionDetails(transactionToken, transactionExpiration);
            
            simpMessagingTemplate.convertAndSend("/queue/transaction/"+request.getUserPrincipal().getName(), new ApiResponse(SUCCESS, BROADCAST, transactionDetails));
            
        }  
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
    
    private RV getRVFromRequest() throws JsonProcessingException, IOException, RVInjectionException {
        JsonNode payloadNode = objectMapper.readTree(request.getInputStream());
        return (RV) getArgumentFromBody(RV.class, Optional.empty(), payloadNode);
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

}