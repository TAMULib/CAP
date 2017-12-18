package edu.tamu.cap.model.validation;

import edu.tamu.weaver.validation.model.InputValidationType;
import edu.tamu.weaver.validation.utility.ValidationUtility;
import edu.tamu.weaver.validation.validators.BaseModelValidator;
import edu.tamu.weaver.validation.validators.InputValidator;

public class IRValidator extends BaseModelValidator {

    public IRValidator() {
        String nameProperty = "name";
        this.addInputValidator(new InputValidator(InputValidationType.required, "IR requires a name", nameProperty, true));

        String uriProperty = "uri";
        this.addInputValidator(new InputValidator(InputValidationType.required, "IR requires a URI", uriProperty, true));
        
        this.addInputValidator(new InputValidator(InputValidationType.pattern, "IR requires a valid URI", uriProperty, ValidationUtility.URL_REGEX));
        
    }
}
