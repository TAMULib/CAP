package edu.tamu.cap.controller.rvcontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.RVService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("rv-context/{type}/{rvid}")
public class RVContextController {

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse get(RVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, rvService.getRVContext(contextUri));
    }

    @RequestMapping(method = DELETE)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse delete(RVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {
        try {
            rvService.deleteRVContext(contextUri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(value = "/triples", method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getTriples(RVService<?> rvService, @Param("contextUri") String contextUri) throws Exception {
        return new ApiResponse(SUCCESS, rvService.getTriples(rvService, contextUri));
    }

}
