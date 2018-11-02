package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;

import java.util.List;

class DatasetDnarunValidator extends BaseValidator {
    @Override
    boolean validate(ValidationUnit validationUnit, String dir, List<Failure> failureList) {
        try {
            if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), failureList)) {
                String filePath = dir + "/" + validationUnit.getDigestFileName();
                List<Failure> failures = beginValidation(filePath, validationUnit);
                failureList.addAll(failures);
                return true;
            } else {
                if (failureList.size() == 0) return false;
                else return true;
            }
        } catch (MaximumErrorsValidationException e) {
            //////Don't do any thing. This implies that particular error list is full.;
        }
        return true;
    }
}
