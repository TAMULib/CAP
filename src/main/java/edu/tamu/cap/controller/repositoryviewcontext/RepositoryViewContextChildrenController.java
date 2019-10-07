package edu.tamu.cap.controller.repositoryviewcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view-context/{type}/{repositoryViewId}/children")
public class RepositoryViewContextChildrenController {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createChildContainer(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri, @RequestBody ArrayList<HashMap<String, String>> tripleMaps) throws Exception {
        List<Triple> metadata = new ArrayList<Triple>();
        tripleMaps.forEach(tripleMap -> {
            metadata.add(Triple.of(tripleMap));
        });
        return new ApiResponse(SUCCESS, repositoryViewService.createChild(contextUri, metadata));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getChildContainer(RepositoryViewService<?> repositoryViewService, @RequestParam("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, repositoryViewService.getChildren(contextUri));
    }

}
