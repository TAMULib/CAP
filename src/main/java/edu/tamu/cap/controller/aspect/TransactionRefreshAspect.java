package edu.tamu.cap.controller.aspect;

import static edu.tamu.weaver.response.ApiAction.BROADCAST;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
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
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.repositoryviewcontext.TransactionDetails;
import edu.tamu.cap.service.RepositoryViewResolver;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.service.TransactingRepositoryViewService;
import edu.tamu.weaver.context.SpringContext;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
@Scope(value = SCOPE_REQUEST)
public class TransactionRefreshAspect extends RepositoryViewResolver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RepositoryViewRepo repositoryViewRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest request;

    // @formatter:off
    @AfterReturning(
        pointcut = "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextChildrenController.createChildContainer(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextController.delete(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextMetadataController.createMetadata(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextMetadataController.updateMetadata(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextMetadataController.deleteMetadata(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextQueryController.submitQuery(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextResourcesController.createResource(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextResourcesController.deleteResources(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextVersionController.createVersion(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextVersionController.restoreVersion(..)) || " +
                   "execution(* edu.tamu.cap.controller.repositoryviewcontext.RepositoryViewContextVersionController.deleteVersion(..))",
        returning = "apiResponse"
    )
    // @formatter:on
    public void refreshTransaction(JoinPoint joinPoint, ApiResponse apiResponse) throws Throwable {
        Optional<Cookie> cookie = Optional.empty();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("transaction")) {
                    cookie = Optional.of(c);
                    break;
                }
            }
        }

        if (cookie.isPresent()) {
            JsonNode cookieObject = objectMapper.readTree(URLDecoder.decode(cookie.get().getValue(), "UTF-8"));
            String tokenUri = cookieObject.get("transactionToken").asText();

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();

            for (Parameter parameter : method.getParameters()) {
                System.out.println("\n\n" + parameter + "\n\n");
                if (RepositoryViewService.class.isAssignableFrom(parameter.getType())) {
                    TransactingRepositoryViewService<?> repositoryViewService = SpringContext.bean(getRepositoryViewType().getGloss());
                    Optional<Long> repositoryViewid = getRepositoryViewId();
                    if (repositoryViewid.isPresent()) {
                        repositoryViewService.setRepositoryView(repositoryViewRepo.read(repositoryViewid.get()));
                    } else {
                        repositoryViewService.setRepositoryView(getRepositoryViewFromRequest(request));
                    }
                    TransactionDetails transactionDetails = repositoryViewService.refreshTransaction(tokenUri);
                    simpMessagingTemplate.convertAndSend("/queue/transaction/" + request.getUserPrincipal().getName(), new ApiResponse(SUCCESS, BROADCAST, transactionDetails));
                }
            }
        }
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

    @Override
    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}