package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/children")
public class RepositoryViewContextChildrenController {
    
    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse create(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri, ArrayList<HashMap<String, String>> tripleMaps) throws Exception {
        
        List<Triple> metadata = new ArrayList<Triple>();
        tripleMaps.forEach(tripleMap->{
            metadata.add(Triple.of(tripleMap));
        });
        
        return new ApiResponse(SUCCESS, repositoryViewService.createChild(contextUri, metadata));
    }
    
    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getContainer(RepositoryViewService<?> repositoryViewService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getChildren(contextUri));
    }

}
