package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/metadata")
public class RepositoryViewContextMetadataController {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createMetadata(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri, @RequestBody Triple triple) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.createMetadata(contextUri, triple));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getMetadata(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getMetadata(contextUri));
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse updateMetadata(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri, @RequestParam("value") String value, @RequestBody Triple triple) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.updateMetadata(contextUri, triple, value));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteMetadata(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri, @RequestBody Triple triple) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.deleteMetadata(contextUri, triple));
    }

}
