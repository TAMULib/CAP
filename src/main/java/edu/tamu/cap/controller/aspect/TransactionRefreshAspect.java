package edu.tamu.cap.controller.aspect;

import static edu.tamu.cap.utility.ContextUtility.buildFullContextURI;
import static edu.tamu.cap.utility.ContextUtility.getTransactionToken;
import static edu.tamu.weaver.response.ApiAction.BROADCAST;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.response.TransactionDetails;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.cap.service.repositoryview.RepositoryViewService;
import edu.tamu.cap.service.repositoryview.TransactingRepositoryViewService;
import edu.tamu.weaver.context.SpringContext;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
@Scope(value = SCOPE_REQUEST)
public class TransactionRefreshAspect {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private RepositoryViewRepo repositoryViewRepo;

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
        String contextUri = request.getParameter("contextUri");
        Optional<String> transactionToken = getTransactionToken(contextUri);
        if (transactionToken.isPresent()) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            for (Parameter parameter : method.getParameters()) {
                if (RepositoryViewService.class.isAssignableFrom(parameter.getType())) {

                    @SuppressWarnings("unchecked")
                    Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

                    Optional<String> repositoryViewId = Optional.ofNullable(pathVariables.get("repositoryViewId"));
                    if (repositoryViewId.isPresent()) {
                        RepositoryView repositoryView = repositoryViewRepo.read(Long.parseLong(repositoryViewId.get()));
                        if (repositoryView != null) {
                            RepositoryViewType type = RepositoryViewType.valueOf(pathVariables.get("type"));

                            TransactingRepositoryViewService<?> repositoryViewService = SpringContext.bean(type.getGloss());

                            repositoryViewService.setRepositoryView(repositoryView);

                            String longContextUri = buildFullContextURI(repositoryView.getRootUri(), transactionToken.get());

                            TransactionDetails transactionDetails = repositoryViewService.refreshTransaction(longContextUri);

                            simpMessagingTemplate.convertAndSend("/queue/transaction/" + request.getUserPrincipal().getName(), new ApiResponse(SUCCESS, BROADCAST, transactionDetails));
                        }
                    }
                }
            }
        }
    }

}