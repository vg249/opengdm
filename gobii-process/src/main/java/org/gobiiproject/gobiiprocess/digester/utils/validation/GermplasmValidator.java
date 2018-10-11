package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;

import java.util.*;

import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.isNullAndEmpty;
import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.printMissingFieldError;

class GermplasmValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> digestGermplasm = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestGermplasm, failureList)) {
            String fileName = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(fileName, validationUnit.getConditions(), failureList);
            validateRequiredUniqueColumns(fileName, validationUnit.getConditions(), failureList);
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.DB)) {
                    if (condition.typeName != null) {
                        if (condition.typeName.equalsIgnoreCase(ValidationConstants.CV))
                            if (condition.fieldToCompare != null) {
                                if (checkForHeaderExistence(fileName, condition, failureList))
                                    validateCV(fileName, condition.fieldToCompare, failureList);
                            } else
                                printMissingFieldError("DB", "fieldToCompare", FailureTypes.CORRUPTED_VALIDATION_FILE, failureList);
                    } else
                        printMissingFieldError("DB", "typeName", FailureTypes.CORRUPTED_VALIDATION_FILE, failureList);
                }
            }
        }
    }

    /**
     * Validate terms in CV table
     *
     * @param fileName       fileName
     * @param fieldToCompare field to check
     * @param failureList    failure list
     */
    private void validateCV(String fileName, String fieldToCompare, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String[]> collect = readFileIntoMemory(fileName, failureList);
        List<String> headers = Arrays.asList(collect.get(0));
        int fieldIndex = headers.indexOf(fieldToCompare);
        collect.remove(0);
        Set<String> fieldNameList = new HashSet<>();
        for (String[] fileRow : collect) {
            List<String> fileRowList = Arrays.asList(fileRow);
            if (fileRowList.size() > fieldIndex)
                if (!isNullAndEmpty(fileRowList.get(fieldIndex)))
                    fieldNameList.add(fileRowList.get(fieldIndex));
        }
        List<NameIdDTO> nameIdDTOList = new ArrayList<>();
        for (String fieldName : fieldNameList) {
            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(fieldName);
            nameIdDTOList.add(nameIdDTO);
        }
        ValidationWebServicesUtil.validateCVName(nameIdDTOList, fieldToCompare, failureList);
    }
}
