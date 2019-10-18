package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.service.repositoryview.FixityRepositoryViewService;
import edu.tamu.cap.service.repositoryview.RepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/resource")
public class RepositoryViewContextResourcesController {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createResource(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri, @RequestParam("file") MultipartFile file) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.createResource(contextUri, file));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getResources(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getResource(contextUri));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteResources(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        repositoryViewService.deleteResource(contextUri);
        return new ApiResponse(SUCCESS);
    }

    @GetMapping("/fixity")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse checkFixity(FixityRepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.resourceFixity(contextUri));
    }

}
