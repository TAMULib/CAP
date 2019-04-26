package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.UPDATE;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.Role;
import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.weaver.auth.annotation.WeaverUser;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/repository-view")
public class RepositoryViewController {

	@Autowired
	private RepositoryViewRepo repositoryViewRepo;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('ADMIN')")
  @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
  public ApiResponse createRepositoryView(@RequestBody @WeaverValidatedModel RepositoryView repositoryView) {
      logger.info("Creating Repository View:  " + repositoryView.getName() + " with schema " + repositoryView.getSchemas());
      return new ApiResponse(SUCCESS, repositoryViewRepo.create(repositoryView));
  }

	@RequestMapping(method = RequestMethod.GET)
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

	@RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('CURATOR')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
	public ApiResponse updateRV(@WeaverUser User user, @RequestBody @WeaverValidatedModel RepositoryView repositoryView) {
        if (checkRepositoryViewAccess(user,repositoryView.getId())) {
            logger.info("Updating Repository View:  " + repositoryView.getName());
            return new ApiResponse(SUCCESS, repositoryViewRepo.update(repositoryView));
        } else {
            return new ApiResponse(ERROR, "Unauthorized RepositoryView update attempt by: "+user.getId());
        }


	}

	@RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
	public ApiResponse deleteRepositoryView(@RequestBody @WeaverValidatedModel RepositoryView repositoryView) {
		logger.info("Deleting Repository View:  " + repositoryView.getName());
		repositoryViewRepo.delete(repositoryView);
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('CURATOR')")
    public ApiResponse getRepositoryView(@WeaverUser User user, @PathVariable Long repositoryViewId) {
        if (checkRepositoryViewAccess(user,repositoryViewId)) {
            return new ApiResponse(SUCCESS, repositoryViewRepo.read(repositoryViewId));
        } else {
            return new ApiResponse(ERROR, "Unauthorized RepositoryView GET attempt by: "+user.getId());
        }
    }

	@RequestMapping(value = "/types", method = RequestMethod.GET)
	@PreAuthorize("hasRole('CURATOR')")
	public ApiResponse getRepositoryViewTypes() {
		return new ApiResponse(SUCCESS, RepositoryViewType.getValues());
	}

	protected boolean checkRepositoryViewAccess(User user, Long repositoryViewId) {
        return ((user.getRole().ordinal() == Role.ROLE_ADMIN.ordinal()) || (user.getRole().ordinal() == Role.ROLE_CURATOR.ordinal() && user.hasRepositoryView(repositoryViewId)));
	}

}
