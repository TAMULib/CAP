/*
 * UserController.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *     $Log$
 */
package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.EmailTemplate;
import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.UserRepo;
import edu.tamu.cap.service.EmailTemplateService;
import edu.tamu.weaver.auth.annotation.WeaverCredentials;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.email.service.EmailSender;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.user.model.IRole;

/**
 * User Controller
 *
 * @author
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private UserRepo userRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Websocket endpoint to request credentials.
     *
     * @param credentials
     * @ApiCredentials Credentials
     *
     * @return ApiResponse
     *
     */
    @RequestMapping("/credentials")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse credentials(@WeaverCredentials Credentials credentials) {
        return new ApiResponse(SUCCESS, credentials);
    }

    /**
     * Endpoint to return all users.
     *
     * @return ApiResponse
     *
     */
    @RequestMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse allUsers() {
        return new ApiResponse(SUCCESS, userRepo.findAll());
    }

    /**
     * Endpoint to update users role.
     *
     * @param user
     * @ApiModel AppUser
     *
     * @return ApiResponse
     *
     */
    @RequestMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse update(@RequestBody User user) {
        Optional<User> currentUser = userRepo.findByUsername(user.getUsername());
        if (currentUser.isPresent()) {
            IRole originalRole = userRepo.getOne(user.getId()).getRole();

            user.setPassword(currentUser.get().getPassword());
            user = userRepo.update(user);
            IRole updatedRole = userRepo.getOne(user.getId()).getRole();

            if (originalRole != updatedRole) {
                HashMap<String, String> emailData = new HashMap<String, String>();
                emailData.put("USER_ROLE_FROM", originalRole.toString().replaceFirst("ROLE_", ""));
                emailData.put("USER_ROLE_TO", updatedRole.toString().replaceFirst("ROLE_", ""));

                EmailTemplate template = emailTemplateService.buildEmail("user_role_changed", emailData);

                try {
                    emailSender.sendEmail(user.getEmail(), template.getSubject(), template.getMessage());
                } catch (javax.mail.MessagingException e) {
                    logger.debug("Unable to send user role changed email! " + user.getEmail());
                }
            }

            return new ApiResponse(SUCCESS, user);
        } else {
            return new ApiResponse(ERROR, "User not found");
        }
    }

    /**
     * Endpoint to delete user.
     *
     * @param user
     * @ApiModel AppUser
     *
     * @return ApiResponse
     *
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse delete(@RequestBody User user) throws Exception {
        userRepo.delete(user);
        return new ApiResponse(SUCCESS);
    }

}
