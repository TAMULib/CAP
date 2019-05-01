package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.weaver.messaging.model.MessageAction;
import edu.tamu.weaver.messaging.service.MessagingService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@Profile("weaver-messaging")
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/message")
public class RepositoryViewContextMessageController {

    @Value("${app.messaging.channel:cap}")
    private String MESSAGING_CHANNEL;

    @Autowired
    private MessagingService messagingService;

    @RequestMapping(value = "refresh", method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse refresh(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) {
        Map<String, String> payload = new HashMap<String, String>();
        RepositoryView repositoryView = repositoryViewService.getRepositoryView();
        payload.put("id", contextUri.replace(repositoryView.getRootUri(), ""));
        payload.put("action", MessageAction.REFRESH.toString());
        messagingService.sendMessage(MESSAGING_CHANNEL, payload);
        return new ApiResponse(SUCCESS);
    }

}
