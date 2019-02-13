package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.data.repository.query.Param;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.QueryableRepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/query")
public class RepositoryViewContextQueryController {
    
    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse makeQuery(QueryableRepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri, @RequestBody String query) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.query(contextUri, query));
    }
    
    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getQueryHelp(QueryableRepositoryViewService<?> rvService) throws Exception {
        return new ApiResponse(SUCCESS, "Successfully retrieved query help.", rvService.getQueryHelp());
    }

}
