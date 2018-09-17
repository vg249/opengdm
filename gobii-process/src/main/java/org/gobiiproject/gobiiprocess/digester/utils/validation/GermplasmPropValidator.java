package org.gobiiproject.gobiiprocess.digester.utils.validation;

import java.util.*;

class GermplasmPropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir, List<String> errorList) throws MaximumErrorsValidationException {
        List<String> digestGermplasmProp = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestGermplasmProp, errorList)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(filePath, validationUnit.getConditions(), errorList);
            validateRequiredUniqueColumns(filePath, validationUnit.getConditions(), errorList);
            validateColumnsBetweenFiles(filePath, validationUnit, errorList);
        }
    }




}
