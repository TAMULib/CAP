package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.controller.aspect.annotation.PayloadArgName;
import edu.tamu.cap.service.VersioningRepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/version")
public class RepositoryViewContextVersionController {
    
    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createVersion(VersioningRepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri, @PayloadArgName("name") String name) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.createVersion(contextUri, name));
    }
    
    @RequestMapping(method = PATCH)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse restoreVersion(VersioningRepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.restoreVersion(contextUri));
    }
    
    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteVersion(VersioningRepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        repositoryViewService.deleteVersion(contextUri);
        return new ApiResponse(SUCCESS);
    }

}
