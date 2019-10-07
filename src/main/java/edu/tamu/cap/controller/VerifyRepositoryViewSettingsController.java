package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.service.VerifyingRepositoryViewService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("repository-view/{type}/verify")
public class VerifyRepositoryViewSettingsController {

    @PostMapping(value = "/ping")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse verifyRepositoryViewPing(VerifyingRepositoryViewService<?> repositoryViewService) throws Exception {
        repositoryViewService.verifyPing();
        return new ApiResponse(SUCCESS, "Ping was successful!");
    }

    @PostMapping(value = "/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse verifyRepositoryViewAuth(VerifyingRepositoryViewService<?> repositoryViewService) throws Exception {
        repositoryViewService.verifyAuth();
        return new ApiResponse(SUCCESS);
    }

    @PostMapping(value = "/content")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse verifyRepositoryViewContent(VerifyingRepositoryViewService<?> repositoryViewService) throws Exception {
        repositoryViewService.verifyRoot();
        return new ApiResponse(SUCCESS);
    }

}
