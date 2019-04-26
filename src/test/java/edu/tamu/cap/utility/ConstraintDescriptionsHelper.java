package edu.tamu.cap.utility;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.util.StringUtils;

public final class ConstraintDescriptionsHelper {
    private final ConstraintDescriptions constraintDescriptions;

    public ConstraintDescriptionsHelper(Class<?> model) {
        constraintDescriptions = new ConstraintDescriptions(model);
    }

    public FieldDescriptor withField(String path, String description) {
        return fieldWithPath(path).description(description + getConstraintDescription(path));
    }

    public ParameterDescriptor withParameter(String path, String description) {
        return parameterWithName(path).description(description + getConstraintDescription(path));
    }

    private String getConstraintDescription(String path) {
        String description = StringUtils.collectionToDelimitedString(constraintDescriptions.descriptionsForProperty(path), ". ");
        return description.length() > 0 ? " " + description + "." : "";
    }
}
