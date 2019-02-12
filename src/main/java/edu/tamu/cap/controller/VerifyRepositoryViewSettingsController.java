package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.VerifyingRepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view/{type}/verify")
public class VerifyRepositoryViewSettingsController {

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse verifyRVPing(VerifyingRepositoryViewService<?> rvService) throws Exception {
        rvService.verifyPing();
        return new ApiResponse(SUCCESS, "Ping was successful!");
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse verifyRepositoryViewAuth(VerifyingRepositoryViewService<?> repositoryViewService) throws Exception {
        repositoryViewService.verifyAuth();
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(value = "/content", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse verifyRepositoryViewContent(VerifyingRepositoryViewService<?> repositoryViewService) throws Exception {
        repositoryViewService.verifyRoot();
        return new ApiResponse(SUCCESS);
    }

}
