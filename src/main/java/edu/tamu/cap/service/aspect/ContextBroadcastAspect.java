package edu.tamu.cap.service.aspect;

import static edu.tamu.weaver.response.ApiAction.BROADCAST;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.weaver.response.ApiResponse;

@Aspect
@Component
public class ContextBroadcastAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @AfterReturning(pointcut = "execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.*(..)) && !execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.featureSupport(..)) && !execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.getContainer(..)) && !execution(edu.tamu.cap.model.response.RepositoryViewContext edu.tamu.cap.service.RepositoryViewService.buildRepositoryViewContext(..))", returning = "context")
    public void broadcastContext(RepositoryViewContext context) throws Throwable {
        logger.info("Broadcasting " + context.getTriple().getSubject());
        simpMessagingTemplate.convertAndSend("/queue/context", new ApiResponse(SUCCESS, BROADCAST, context));
    }

}