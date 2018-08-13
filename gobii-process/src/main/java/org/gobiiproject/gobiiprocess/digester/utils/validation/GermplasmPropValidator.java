package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.*;

class GermplasmPropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        System.out.println("Germplasm-prop validation Started.");
        List<String> digestGermplasmProp = new ArrayList<>();
        if (getFilesWithExtension(dir, validationUnit.getDigestFileName(), digestGermplasmProp)) {
            if (digestGermplasmProp.size() != 1) {
                ErrorLogger.logError("There should be only one germplasm-prop file in the folder ", dir);
                return;
            }
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(filePath, validationUnit.getConditions());
            validateRequiredUniqueColumns(filePath, validationUnit.getConditions());
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase("File")) {
                    validateColumnBetweenFiles(filePath, condition);
                }
            }
        }
    }




}
