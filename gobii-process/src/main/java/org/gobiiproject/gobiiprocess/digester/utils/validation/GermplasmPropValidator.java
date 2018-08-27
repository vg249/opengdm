package org.gobiiproject.gobiiprocess.digester.utils.validation;


import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.*;

class GermplasmPropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        boolean status = true;
        ErrorLogger.logDebug("Germplasm-prop validation ", " started.");
        List<String> digestGermplasmProp = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestGermplasmProp)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            boolean returnValue = validateRequiredColumns(filePath, validationUnit.getConditions());
            if (status) status = returnValue;
            returnValue = validateRequiredUniqueColumns(filePath, validationUnit.getConditions());
            if (status) status = returnValue;
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                    returnValue = validateColumnBetweenFiles(filePath, condition);
                    if (status) status = returnValue;
                }
            }
        } else {
            if (digestGermplasmProp.size() > 1)
                status = false;
        }
        printValidationDone("Germplasm-prop", status);
    }


}
