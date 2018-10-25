package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;

import java.util.ArrayList;
import java.util.List;

class DnaSampleValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> dnaSample = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), dnaSample, failureList)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(filePath, validationUnit.getConditions(), failureList);
            validateRequiredUniqueColumns(filePath, validationUnit.getConditions(), failureList);
            validateUniqueColumnList(filePath, validationUnit, failureList);
            validateColumnsBetweenFiles(filePath, validationUnit, failureList);
        }
    }
}
