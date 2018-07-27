package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.*;

import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.printMissingFieldError;

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
            String fileName = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(fileName, validationUnit.getConditions());
            validateRequiredUniqueColumns(fileName, validationUnit.getConditions());
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase("File")) {
                    if (condition.typeName != null) {
                        if (ValidationUtil.getAllowedExtensions().contains(condition.typeName.substring(condition.typeName.indexOf('.') + 1))) {
                            if (condition.fieldToCompare != null)
                                validateColumn(fileName, condition.typeName, condition.fieldToCompare);
                            else {
                                printMissingFieldError("File", "fieldToCompare");
                            }
                        } else
                            ErrorLogger.logError(condition.typeName, " is not a valid file extension.");
                    } else {
                        printMissingFieldError("File", "typeName");
                    }
                }
            }
        }
    }


}
