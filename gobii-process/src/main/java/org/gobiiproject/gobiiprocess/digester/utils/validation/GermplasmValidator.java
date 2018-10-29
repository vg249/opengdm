package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;

import java.util.*;

class GermplasmValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> digestGermplasm = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestGermplasm, failureList)) {
            String fileName = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(fileName, validationUnit.getConditions(), failureList);
            validateRequiredUniqueColumns(fileName, validationUnit.getConditions(), failureList);
            validateDataBasecalls(validationUnit, failureList, fileName);
        }
    }
}
