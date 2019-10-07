package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}")
public class RepositoryViewContextController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse get(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getRepositoryViewContext(contextUri));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse delete(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        try {
            repositoryViewService.deleteRepositoryViewContext(contextUri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse(SUCCESS);
    }

    @GetMapping("/triples")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getTriples(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getTriples(repositoryViewService, contextUri));
    }

}
