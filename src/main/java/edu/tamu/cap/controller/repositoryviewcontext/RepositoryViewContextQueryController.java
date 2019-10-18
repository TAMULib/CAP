package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.repositoryview.QueryableRepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/query")
public class RepositoryViewContextQueryController {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse submitQuery(QueryableRepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri, @RequestBody String query) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.query(contextUri, query));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getQueryHelp(QueryableRepositoryViewService<?> repositoryViewService) throws Exception {
        return new ApiResponse(SUCCESS, "Successfully retrieved query help.", repositoryViewService.getQueryHelp());
    }

}
