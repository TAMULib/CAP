package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}")
public class RepositoryViewContextController {

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse get(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getRepositoryViewContext(contextUri));
    }

    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse delete(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        try {
            repositoryViewService.deleteRepositoryViewContext(contextUri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(value = "/triples", method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getTriples(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getTriples(repositoryViewService, contextUri));
    }

}
