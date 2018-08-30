package org.gobiiproject.gobiiprocess.digester.utils.validation;

import java.util.*;

class GermplasmPropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir, List<String> errorList) {
        List<String> digestGermplasmProp = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestGermplasmProp, errorList)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(filePath, validationUnit.getConditions(), errorList);
            validateRequiredUniqueColumns(filePath, validationUnit.getConditions(), errorList);
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                    validateColumnBetweenFiles(filePath, condition, errorList);
                }
            }
        }
    }


}
