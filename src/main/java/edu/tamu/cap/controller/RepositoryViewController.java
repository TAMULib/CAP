package edu.tamu.cap.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.UPDATE;

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
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.RepositoryViewType;
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
    @PreAuthorize("hasRole('MANAGER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse createRepositoryView(@RequestBody @WeaverValidatedModel RepositoryView repositoryView) {
        logger.info("Creating Repository View:  " + repositoryView.getName() + " with schema " + repositoryView.getSchemas());
        return new ApiResponse(SUCCESS, repositoryViewRepo.create(repositoryView));
    }

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasRole('MANAGER')")
	public ApiResponse allRepositoryViews() {
		return new ApiResponse(SUCCESS, repositoryViewRepo.findAll());
	}

	@RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('MANAGER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
	public ApiResponse updateRV(@RequestBody @WeaverValidatedModel RepositoryView repositoryView) {
		logger.info("Updating Repository View:  " + repositoryView.getName());
		return new ApiResponse(SUCCESS, repositoryViewRepo.update(repositoryView));
	}

	@RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('MANAGER')")
	@WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
	public ApiResponse deleteRepositoryView(@RequestBody @WeaverValidatedModel RepositoryView repositoryView) {
		logger.info("Deleating Repository View:  " + repositoryView.getName());
		repositoryViewRepo.delete(repositoryView);
		return new ApiResponse(SUCCESS);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse getRepositoryView(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, repositoryViewRepo.read(id));
    }

	@RequestMapping(value = "/types", method = RequestMethod.GET)
	@PreAuthorize("hasRole('MANAGER')")
	public ApiResponse getRepositoryViewTypes() {
		return new ApiResponse(SUCCESS, RepositoryViewType.getValues());
	}

}
