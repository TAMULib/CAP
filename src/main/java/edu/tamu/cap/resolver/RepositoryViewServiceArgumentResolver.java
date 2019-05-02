package edu.tamu.cap.resolver;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.service.RepositoryViewResolver;
import edu.tamu.cap.service.RepositoryViewService;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.weaver.context.SpringContext;

public final class RepositoryViewServiceArgumentResolver extends RepositoryViewResolver implements HandlerMethodArgumentResolver {

    private final static Logger logger = LoggerFactory.getLogger(RepositoryViewServiceArgumentResolver.class);

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

        Optional<Long> repositoryViewId = Optional.empty();
        try {
            repositoryViewId = Optional.of(Long.parseLong(pathVariables.get("repositoryViewId")));
        } catch (NumberFormatException e) {
            logger.info("Id not provided in path!");
        }

        RepositoryViewService<?> repositoryViewService = SpringContext.bean(type.getGloss());
        if (repositoryViewId.isPresent()) {
            repositoryViewService.setRepositoryView(repositoryViewRepo.read(repositoryViewId.get()));
        } else {
            repositoryViewService.setRepositoryView(getRepositoryViewFromRequest(request));
        }

        return repositoryViewService;
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}
