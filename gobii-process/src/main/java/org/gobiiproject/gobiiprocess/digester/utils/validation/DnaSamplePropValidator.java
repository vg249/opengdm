package org.gobiiproject.gobiiprocess.digester.utils.validation;

import java.util.ArrayList;
import java.util.List;

class DnaSamplePropValidator extends BaseValidator{
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        System.out.println("DNA Sample-prop validation Started.");
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

    }
}
