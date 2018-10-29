package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;

import java.util.ArrayList;
import java.util.List;

class MarkerPropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> digestMarkerProp = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestMarkerProp, failureList)) {
            String fileName = dir + "/" + validationUnit.getDigestFileName();
            beginValidation(fileName, validationUnit, failureList);
        }
    }
}
