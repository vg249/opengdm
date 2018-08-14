package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class DnaSampleValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        System.out.println("DNA Sample validation Started.");
        List<String> dnaSample = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), dnaSample)) {
            String fileName = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(fileName, validationUnit.getConditions());
            validateRequiredUniqueColumns(fileName, validationUnit.getConditions());
            validateUniqueColumnList(fileName, validationUnit);
            validateFileExistenceCheck(fileName, validationUnit);
        }
    }

    private void validateFileExistenceCheck(String fileName, ValidationUnit validationUnit) {
        for (ConditionUnit condition : validationUnit.getConditions()) {
            if (condition.fileExistenceCheck != null) {
                String existenceFile = condition.fileExistenceCheck;
                List<String> digestGermplasm = new ArrayList<>();
                boolean shouldFileExist = condition.fileExists.equalsIgnoreCase(ValidationConstants.YES);
                getFilesWithExtension(new File(fileName).getParent(), existenceFile, digestGermplasm);
                if (digestGermplasm.size() > 1) {
                    ErrorLogger.logError("There should be maximum one file in the folder ", new File(fileName).getParent());
                    return;
                }
                if ((shouldFileExist && digestGermplasm.size() == 1) || (!shouldFileExist && digestGermplasm.size() == 0)) {
                    // Condition is satisfied
                    if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.DB)) {
                        //TODO: Implement the database call
                    } else if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                        validateColumnBetweenFiles(fileName, condition);
                    } else {
                        ErrorLogger.logError("Unrecognised type defined in condition", condition.type);
                    }
                }
            }
        }
    }
}
