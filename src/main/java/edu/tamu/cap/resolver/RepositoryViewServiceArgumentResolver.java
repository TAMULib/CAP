package edu.tamu.cap.resolver;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.weaver.context.SpringContext;

public final class RepositoryViewServiceArgumentResolver implements HandlerMethodArgumentResolver {

    private RepositoryViewRepo repositoryViewRepo;

    private ObjectMapper objectMapper;

    public RepositoryViewServiceArgumentResolver(RepositoryViewRepo repositoryViewRepo, ObjectMapper objectMapper) {
        this.repositoryViewRepo = repositoryViewRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return RepositoryViewService.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        RepositoryViewType type = RepositoryViewType.valueOf(pathVariables.get("type"));

        RepositoryViewService<?> repositoryViewService = SpringContext.bean(type.getGloss());

        Optional<String> repositoryViewId = Optional.ofNullable(pathVariables.get("repositoryViewId"));

        if (repositoryViewId.isPresent()) {
            RepositoryView repositoryView = repositoryViewRepo.read(Long.parseLong(repositoryViewId.get()));
            if (repositoryView != null) {
                repositoryViewService.setRepositoryView(repositoryView);
            }
            return repositoryViewService;
        }

        // NOTE: This should be only if repository view id is not present!
        //
        // The actual use case of the request body being a valid repository view
        // is during verification. There is probably a better way to verify
        // a repository view before creation.
        //
        // Tests rely on the ability for the request body be the repository view
        // rather then acquire a persisted one.
        RepositoryView repositoryView = objectMapper.readValue(request.getInputStream(), RepositoryView.class);
        repositoryViewService.setRepositoryView(repositoryView);

        return repositoryViewService;
    }

}
