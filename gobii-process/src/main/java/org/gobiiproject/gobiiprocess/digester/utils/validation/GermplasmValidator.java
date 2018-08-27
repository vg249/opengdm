package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.*;

import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.isNullAndEmpty;
import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.printMissingFieldError;

class GermplasmValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        boolean status = true;
        ErrorLogger.logDebug("Germplasm validation ", " started.");
        List<String> digestGermplasm = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestGermplasm)) {
            String fileName = dir + "/" + validationUnit.getDigestFileName();
            boolean returnValue = validateRequiredColumns(fileName, validationUnit.getConditions());
            if (status) status = returnValue;
            returnValue = validateRequiredUniqueColumns(fileName, validationUnit.getConditions());
            if (status) status = returnValue;
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.DB)) {
                    if (condition.typeName != null) {
                        if (condition.typeName.equalsIgnoreCase(ValidationConstants.CV))
                            if (condition.fieldToCompare != null) {
                                returnValue = validateCV(fileName, condition.fieldToCompare);
                                if (status) status = returnValue;
                            } else {
                                printMissingFieldError("DB", "fieldToCompare");
                                status = false;
                            }
                    } else {
                        printMissingFieldError("DB", "typeName");
                        status = false;
                    }
                }
            }
        } else {
            if (digestGermplasm.size() > 1)
                status = false;
        }
        printValidationDone("Germplasm", status);
    }


    /**
     * Validate terms in CV table
     *
     * @param fileName       fileName
     * @param fieldToCompare field to check
     */
    private boolean validateCV(String fileName, String fieldToCompare) {
        boolean status = true;
        List<String[]> collect = readFileIntoMemory(fileName);
        if (collect != null) {
            List<String> headers = Arrays.asList(collect.get(0));
            if (headers.contains(fieldToCompare)) {
                int fieldIndex = headers.indexOf(fieldToCompare);
                collect.remove(0);
                Set<String> fieldNameList = new HashSet<>();
                for (String[] fileRow : collect) {
                    List<String> fileRowList = Arrays.asList(fileRow);
                    if (fileRowList.size() > fieldIndex)
                        if (!isNullAndEmpty(fileRowList.get(fieldIndex)))
                            fieldNameList.add(fileRowList.get(fieldIndex));
                }
                for (String fieldName : fieldNameList) {
                    NameIdDTO nameIdDTO = new NameIdDTO();
                    nameIdDTO.setName(fieldName);
                    List<NameIdDTO> nameIdDTOList = new ArrayList<>();
                    nameIdDTOList.add(nameIdDTO);
                    boolean returnValue = ValidationWebServicesUtil.validateCVName(nameIdDTOList, fieldToCompare);
                    if (status) status = returnValue;
                }
            }
        }
        return status;
    }
}
