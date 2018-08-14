package org.gobiiproject.gobiiprocess.digester.utils.validation;


import java.util.*;

class GermplasmPropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        System.out.println("Germplasm-prop validation Started.");
        List<String> digestGermplasmProp = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestGermplasmProp)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(filePath, validationUnit.getConditions());
            validateRequiredUniqueColumns(filePath, validationUnit.getConditions());
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                    validateColumnBetweenFiles(filePath, condition);
                }
            }
        }
    }


}
