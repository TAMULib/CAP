package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.UPDATE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.EmailTemplate;
import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.Role;
import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.EmailTemplateService;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.weaver.auth.annotation.WeaverUser;
import edu.tamu.weaver.email.service.EmailSender;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/repository-view")
public class RepositoryViewController {

    @Autowired
    private RepositoryViewRepo repositoryViewRepo;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailTemplateService emailTemplateService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse createRepositoryView(@RequestBody @WeaverValidatedModel RepositoryView repositoryView) {
        logger.info("Creating Repository View:  " + repositoryView.getName() + " with schema " + repositoryView.getSchemas());
        return new ApiResponse(SUCCESS, repositoryViewRepo.create(repositoryView));
    }

    @GetMapping
    @PreAuthorize("hasRole('CURATOR')")
    public ApiResponse allRepositoryViews(@WeaverUser User user) {
        List<RepositoryView> repositoryViews = new ArrayList<RepositoryView>();
        if (user.getRole().ordinal() == Role.ROLE_CURATOR.ordinal()) {
            repositoryViews = user.getRepositoryViews();
        } else {
            repositoryViews = repositoryViewRepo.findAll();
        }
        return new ApiResponse(SUCCESS, repositoryViews);
    }

    @PutMapping
    @PreAuthorize("hasRole('CURATOR')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
    public ApiResponse updateRV(@WeaverUser User user, @RequestBody @WeaverValidatedModel RepositoryView repositoryView) {
        if (checkRepositoryViewAccess(user, repositoryView.getId())) {
            logger.info("Updating Repository View:  " + repositoryView.getName());

            List<User> originalCurators = new ArrayList<User>();
            originalCurators.addAll(repositoryViewRepo.getOne(repositoryView.getId()).getCurators());

            RepositoryView updatedRepositoryView = repositoryViewRepo.update(repositoryView);
            List<User> updatedCurators = repositoryViewRepo.getOne(repositoryView.getId()).getCurators();

            HashMap<String, String> emailData = new HashMap<String, String>();
            emailData.put("REPOSITORY_VIEW", repositoryView.getName());

            for (User originalCurator : originalCurators) {
                boolean found = false;
                for (User updatedCurator : updatedCurators) {
                    if (originalCurator.getId() == updatedCurator.getId()) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    sendEmail(originalCurator.getEmail(), "repository_view_curator_removed", emailData);
                }
            }

            for (User updatedCurator : updatedCurators) {
                boolean found = false;
                for (User originalCurator : originalCurators) {
                    if (updatedCurator.getId() == originalCurator.getId()) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    sendEmail(updatedCurator.getEmail(), "repository_view_curator_added", emailData);
                }
            }

            return new ApiResponse(SUCCESS, updatedRepositoryView);
        } else {
            return new ApiResponse(ERROR, "Unauthorized RepositoryView update attempt by: " + user.getId());
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
    public ApiResponse deleteRepositoryView(@RequestBody @WeaverValidatedModel RepositoryView repositoryView) {
        logger.info("Deleting Repository View:  " + repositoryView.getName());
        repositoryViewRepo.delete(repositoryView);
        return new ApiResponse(SUCCESS);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CURATOR')")
    public ApiResponse getRepositoryView(@WeaverUser User user, @PathVariable("id") Long id) {
        if (checkRepositoryViewAccess(user, id)) {
            return new ApiResponse(SUCCESS, repositoryViewRepo.read(id));
        } else {
            return new ApiResponse(ERROR, "Unauthorized RepositoryView GET attempt by: " + user.getId());
        }
    }

    @GetMapping("/types")
    @PreAuthorize("hasRole('CURATOR')")
    public ApiResponse getRepositoryViewTypes() {
        return new ApiResponse(SUCCESS, RepositoryViewType.getValues());
    }

    private boolean checkRepositoryViewAccess(User user, Long repositoryViewId) {
        return ((user.getRole().ordinal() == Role.ROLE_ADMIN.ordinal()) || (user.getRole().ordinal() == Role.ROLE_CURATOR.ordinal() && user.hasRepositoryView(repositoryViewId)));
    }

    private void sendEmail(String address, String templateName, HashMap<String, String> emailData) {
        EmailTemplate template = emailTemplateService.buildEmail(templateName, emailData);

        try {
            emailSender.sendEmail(address, template.getSubject(), template.getMessage());
        } catch (javax.mail.MessagingException e) {
            logger.debug("Unable to send " + templateName + " email! " + address);
        }
    }

}
