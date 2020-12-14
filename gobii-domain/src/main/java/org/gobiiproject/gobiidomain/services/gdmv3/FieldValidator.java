package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MarkerTable;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class FieldValidator {
    private static  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = factory.getValidator();

    public static void validate (Object validationObject) throws GobiiDomainException {
        Set<ConstraintViolation<Object>> violations = validator.validate(validationObject);
        if(violations.size() > 0) {
            String violationMessage = "";
            for(ConstraintViolation<?> violation : violations) {
                violationMessage += violation.getMessage() + "; ";
            }
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                violationMessage);
        }
    }

}
