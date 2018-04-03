package edu.tamu.cap.controller.ircontext;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.data.repository.query.Param;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.QueryableIRService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("ir-context/{type}/{irid}/query")
public class IRContextQueryController {
    
    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse makeQuery(QueryableIRService<?> irService, @Param("contextUri") String contextUri, @RequestBody String query) throws Exception {
        return new ApiResponse(SUCCESS, irService.query(contextUri, query));
    }
    
    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getQueryHelp(QueryableIRService<?> irService) throws Exception {
        return new ApiResponse(SUCCESS, "Successfully retrieved query help.", irService.getQueryHelp());
    }

}
