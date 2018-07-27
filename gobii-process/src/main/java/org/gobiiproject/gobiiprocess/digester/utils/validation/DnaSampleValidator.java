package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.ArrayList;
import java.util.List;

class DnaSampleValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        System.out.println("DNA Sample validation Started.");
        List<String> dnaSample = new ArrayList<>();
        if (getFilesWithExtension(dir, validationUnit.getDigestFileName(), dnaSample)) {
            if (dnaSample.size() != 1) {
                ErrorLogger.logError("There should be only one dna-sample file in the folder ", dir);
                return;
            }
            String fileName = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(fileName, validationUnit.getConditions());
            validateRequiredUniqueColumns(fileName, validationUnit.getConditions());
            validateUniqueColumnList(fileName, validationUnit);
        }
    }


}
