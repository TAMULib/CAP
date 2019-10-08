package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.repositoryview.VersioningRepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/version")
public class RepositoryViewContextVersionController {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createVersion(VersioningRepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri, @RequestParam(value = "name", defaultValue = "") String name) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.createVersion(contextUri, name));
    }

    @PatchMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse restoreVersion(VersioningRepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.restoreVersion(contextUri));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse deleteVersion(VersioningRepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        repositoryViewService.deleteVersion(contextUri);
        return new ApiResponse(SUCCESS);
    }

}
