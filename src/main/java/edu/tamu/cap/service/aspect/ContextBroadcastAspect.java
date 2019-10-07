package edu.tamu.cap.service.aspect;

import static edu.tamu.weaver.response.ApiAction.BROADCAST;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
@Scope(value = SCOPE_REQUEST)
public class ContextBroadcastAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // @formatter:off
    @AfterReturning(
        pointcut = "execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.createChild(..)) || " + 
                   "execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.deleteRepositoryViewContext(..)) || " +
                   "execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.createResource(..)) || " +
                   "execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.deleteResource(..)) || " +
                   "execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.createMetadata(..)) || " +
                   "execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.updateMetadata(..)) || " +
                   "execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.deleteMetadata(..))",
        returning = "context")
    // @formatter:on
    public void broadcastContext(RepositoryViewContext context) throws Throwable {
        logger.info("Broadcasting " + context.getTriple().getSubject());
        simpMessagingTemplate.convertAndSend("/queue/context", new ApiResponse(SUCCESS, BROADCAST, context));
    }

}