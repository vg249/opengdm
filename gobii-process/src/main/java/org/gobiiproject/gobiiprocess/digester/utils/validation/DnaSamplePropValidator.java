package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.ArrayList;
import java.util.List;

class DnaSamplePropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        ErrorLogger.logDebug("DNA Sample-prop validation ", " started.");
        List<String> dnaSample = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), dnaSample)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(filePath, validationUnit.getConditions());
            validateRequiredUniqueColumns(filePath, validationUnit.getConditions());
            validateUniqueColumnList(filePath, validationUnit);
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                    validateColumnBetweenFiles(filePath, condition);
                }
            }
        }
        ErrorLogger.logDebug("DNA Sample-prop validation ", " done.");
    }
}
