package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;

import java.util.ArrayList;
import java.util.List;

class DatasetDnarunValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> digestGermplasmProp = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestGermplasmProp, failureList)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            beginValidation(filePath, validationUnit, failureList);
        }
    }
}
