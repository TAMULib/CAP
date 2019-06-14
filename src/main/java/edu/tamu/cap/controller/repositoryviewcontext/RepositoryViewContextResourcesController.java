package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.service.FixityRepositoryViewService;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/resource")
public class RepositoryViewContextResourcesController {

    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createResource(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri, @RequestParam("file") MultipartFile file) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.createResource(contextUri, file));
    }

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getResources(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getResource(contextUri));
    }

    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteResources(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        repositoryViewService.deleteResource(contextUri);
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(value = "/fixity", method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse checkFixity(FixityRepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.resourceFixity(contextUri));
    }

}
