package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/metadata")
public class RepositoryViewContextMetadataController {

    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createMetadata(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri, @RequestBody Triple triple) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.createMetadata(contextUri, triple));
    }

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getMetadata(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getMetadata(contextUri));
    }

    @RequestMapping(method = PUT)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse updateMetadata(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri, @Param("newValue") String newValue, @RequestBody Triple triple) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.updateMetadata(contextUri, triple, newValue));
    }

    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteMetadata(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri, @RequestBody Triple triple) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.deleteMetadata(contextUri, triple));
    }

}
