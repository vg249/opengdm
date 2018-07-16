package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.ArrayList;
import java.util.List;

class GermplasmValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        System.out.println("Germplasm validation Started.");
        List<String> digestGermplasm = new ArrayList<>();
        if (getFilesWithExtension(dir, validationUnit.getDigestFileName(), digestGermplasm)) {
            if (digestGermplasm.size() != 1) {
                ErrorLogger.logError("There should be only one germplasm file in the folder ", dir);
                return;
            }
            String fileName = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(fileName, validationUnit.getConditions());
        } else {
            return;
        }


    }


}
