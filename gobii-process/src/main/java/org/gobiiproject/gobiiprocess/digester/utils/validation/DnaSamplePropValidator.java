package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.ArrayList;
import java.util.List;

class DnaSamplePropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        ErrorLogger.logDebug("DNA Sample-prop validation ", " started.");
        boolean status = true;
        List<String> dnaSample = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), dnaSample)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            boolean returnValue = validateRequiredColumns(filePath, validationUnit.getConditions());
            if (status) status = returnValue;
            returnValue = validateRequiredUniqueColumns(filePath, validationUnit.getConditions());
            if (status) status = returnValue;
            returnValue = validateUniqueColumnList(filePath, validationUnit);
            if (status) status = returnValue;
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                    returnValue = validateColumnBetweenFiles(filePath, condition);
                    if (status) status = returnValue;
                }
            }
        } else {
            if (dnaSample.size() > 1)
                status = false;
        }
        printValidationDone("DNA Sample-prop", status);
    }
}
