package edu.tamu.cap.controller.aspect;

import static edu.tamu.cap.utility.ContextUtility.getTransactionToken;
import static edu.tamu.weaver.response.ApiAction.BROADCAST;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.response.TransactionDetails;
import edu.tamu.cap.service.RepositoryViewResolver;
import edu.tamu.cap.service.TransactionService;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
@Scope(value = SCOPE_REQUEST)
public class TransactionRefreshAspect extends RepositoryViewResolver {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private TransactionService transactionService;

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
        String contextUri = request.getParameter("contextUri");
        Optional<String> transactionToken = getTransactionToken(contextUri);
        if (transactionToken.isPresent()) {
            Optional<ZonedDateTime> tokenExpiration = transactionService.get(transactionToken.get());
            if (tokenExpiration.isPresent()) {
                TransactionDetails transactionDetails = TransactionDetails.of(transactionToken.get(), tokenExpiration.get());
                simpMessagingTemplate.convertAndSend("/queue/transaction/" + request.getUserPrincipal().getName(), new ApiResponse(SUCCESS, BROADCAST, transactionDetails));
            }
        }
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}