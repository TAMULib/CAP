package edu.tamu.cap.service.aspect;

import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.weaver.messaging.model.MessageAction;
import edu.tamu.weaver.messaging.service.MessagingService;

@Aspect
@Component
@Scope(value = SCOPE_REQUEST)
public class MessagingAspect {

    private final static Logger logger = LoggerFactory.getLogger(MessagingAspect.class);

    @Value("${app.messaging.channel:cap}")
    private String MESSAGING_CHANNEL;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private RepositoryViewRepo repositoryViewRepo;

    @Autowired
    private HttpServletRequest request;

    @AfterReturning(pointcut = "execution(* edu.tamu.cap.service.RepositoryViewService.createMetadata(..))", returning = "context")
    public void createMetadataMessage(RepositoryViewContext context) throws Throwable {
        logger.info(String.format("Messaging create metadata action"));
        messagingService.sendMessage(MESSAGING_CHANNEL, buildMessage(getContextPath(context), MessageAction.CREATE));
    }

    @AfterReturning(pointcut = "execution(* edu.tamu.cap.service.RepositoryViewService.updateMetadata(..))", returning = "context")
    public void updateMetadataMessage(RepositoryViewContext context) throws Throwable {
        logger.info(String.format("Messaging update metadata action"));
        messagingService.sendMessage(MESSAGING_CHANNEL, buildMessage(getContextPath(context), MessageAction.UPDATE));
    }

    @AfterReturning(pointcut = "execution(* edu.tamu.cap.service.RepositoryViewService.deleteMetadata(..))", returning = "context")
    public void deleteMetadataMessage(RepositoryViewContext context) throws Throwable {
        logger.info(String.format("Messaging delete metadata action"));
        messagingService.sendMessage(MESSAGING_CHANNEL, buildMessage(getContextPath(context), MessageAction.DELETE));
    }

    @AfterReturning(pointcut = "execution(* edu.tamu.cap.service.RepositoryViewService.createResource(..))", returning = "context")
    public void createResourceMessage(RepositoryViewContext context) throws Throwable {
        logger.info(String.format("Messaging create resource action"));
        messagingService.sendMessage(MESSAGING_CHANNEL, buildMessage(getParentContextPath(context), MessageAction.CREATE));
    }

    @AfterReturning(pointcut = "execution(* edu.tamu.cap.service.RepositoryViewService.deleteResource(..))", returning = "context")
    public void deleteResourceMessage(RepositoryViewContext context) throws Throwable {
        logger.info(String.format("Messaging delete resource action"));
        messagingService.sendMessage(MESSAGING_CHANNEL, buildMessage(getParentContextPath(context), MessageAction.DELETE));
    }

    @AfterReturning(pointcut = "execution(* edu.tamu.cap.service.RepositoryViewService.createChild(..))", returning = "context")
    public void createChild(RepositoryViewContext context) throws Throwable {
        logger.info(String.format("Messaging create child action"));
        messagingService.sendMessage(MESSAGING_CHANNEL, buildMessage(getParentContextPath(context), MessageAction.CREATE));
    }

    @AfterReturning(pointcut = "execution(* edu.tamu.cap.service.RepositoryViewService.deleteRepositoryViewContext(..))", returning = "context")
    public void deleteChild(RepositoryViewContext context) throws Throwable {
        logger.info(String.format("Messaging delete child action"));
        messagingService.sendMessage(MESSAGING_CHANNEL, buildMessage(getParentContextPath(context), MessageAction.DELETE));
    }

    private String getContextPath(RepositoryViewContext context) {
        RepositoryView repositoryView = getRepositoryViewService();
        return context.getTriple().getSubject().replace(repositoryView.getRootUri(), "");
    }

    private String getParentContextPath(RepositoryViewContext context) {
        RepositoryView repositoryView = getRepositoryViewService();
        return context.getParent().getSubject().replace(repositoryView.getRootUri(), "");
    }

    private Map<String, String> buildMessage(String contextPath, MessageAction action) throws Exception {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("id", contextPath);
        payload.put("action", action.toString());
        return payload;
    }

    private RepositoryView getRepositoryViewService() {
        Long repositoryViewId = Long.parseLong(getPathVariable("repositoryViewId"));
        return repositoryViewRepo.read(repositoryViewId);
    }

    @SuppressWarnings("unchecked")
    private String getPathVariable(String pathKey) {
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return pathVariables.get(pathKey);
    }

}