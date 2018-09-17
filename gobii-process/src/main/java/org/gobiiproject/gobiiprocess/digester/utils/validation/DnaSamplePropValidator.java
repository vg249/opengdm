package org.gobiiproject.gobiiprocess.digester.utils.validation;

import java.util.ArrayList;
import java.util.List;

class DnaSamplePropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir, List<String> errorList) throws MaximumErrorsValidationException {
        List<String> dnaSample = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), dnaSample, errorList)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(filePath, validationUnit.getConditions(), errorList);
            validateRequiredUniqueColumns(filePath, validationUnit.getConditions(), errorList);
            validateUniqueColumnList(filePath, validationUnit, errorList);
            validateColumnsBetweenFiles(filePath, validationUnit, errorList);
        }
    }
}
