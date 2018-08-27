package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class DnaSampleValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        boolean status = true;
        ErrorLogger.logDebug("DNA Sample validation ", " started.");
        List<String> dnaSample = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), dnaSample)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            boolean returnValue = validateRequiredColumns(filePath, validationUnit.getConditions());
            if (status) status = returnValue;
            returnValue = validateRequiredUniqueColumns(filePath, validationUnit.getConditions());
            if (status) status = returnValue;
            returnValue = validateUniqueColumnList(filePath, validationUnit);
            if (status) status = returnValue;
            returnValue = validateFileExistenceCheck(filePath, validationUnit);
            if (status) status = returnValue;
        } else {
            if (dnaSample.size() > 1)
                status = false;
        }
        printValidationDone("DNA Sample", status);
    }
}
