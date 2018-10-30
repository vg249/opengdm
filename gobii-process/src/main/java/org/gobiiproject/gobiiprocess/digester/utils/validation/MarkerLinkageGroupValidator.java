package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;

import java.util.ArrayList;
import java.util.List;

public class MarkerLinkageGroupValidator extends BaseValidator {
    @Override
    List<Failure> validate(ValidationUnit validationUnit, String dir) {
        List<String> digestMarkerProp = new ArrayList<>();
        List<Failure> failureList = new ArrayList<>();
        try {
            if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestMarkerProp, failureList)) {
                String fileName = dir + "/" + validationUnit.getDigestFileName();
                List<Failure> failures = beginValidation(fileName, validationUnit);
                failureList.addAll(failures);
            }
        } catch (MaximumErrorsValidationException e) {
            //////Don't do any thing. This implies that particular error list is full.;
        }
        return failureList;
    }

}
