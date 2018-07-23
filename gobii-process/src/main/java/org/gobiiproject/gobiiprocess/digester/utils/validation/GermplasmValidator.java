package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.*;

import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.isNullAndEmpty;
import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.printMissingFieldError;

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
            validateRequiredUniqueColumns(fileName, validationUnit.getConditions());
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase("DB")) {
                    if (condition.typeName != null) {
                        if (condition.typeName.equalsIgnoreCase("CV"))
                            if (condition.fieldToCompare != null)
                                validateCV(fileName, condition.fieldToCompare);
                            else
                                printMissingFieldError("DB", "fieldToCompare");
                    } else
                        printMissingFieldError("DB", "typeName");
                }
            }
        } else {
            return;
        }
    }


    /**
     * Validate terms in CV table
     *
     * @param fileName       fileName
     * @param fieldToCompare field to check
     */
    private void validateCV(String fileName, String fieldToCompare) {
        List<String[]> collect = readFileIntoMemory(fileName);
        if (collect != null) {
            List<String> headers = Arrays.asList(collect.get(0));
            if (headers.contains(fieldToCompare)) {
                int fieldIndex = headers.indexOf(fieldToCompare);
                collect.remove(0);
                Set<String> fieldNameList = new HashSet<String>();
                for (String[] fileRow : collect) {
                    List<String> fileRowList = Arrays.asList(fileRow);
                    if (!isNullAndEmpty(fileRowList.get(fieldIndex)))
                        fieldNameList.add(fileRowList.get(fieldIndex));
                }
                for (String fieldName : fieldNameList) {
                    NameIdDTO nameIdDTO = new NameIdDTO();
                    nameIdDTO.setName(fieldName);
                    List<NameIdDTO> nameIdDTOList = new ArrayList<>();
                    nameIdDTOList.add(nameIdDTO);
                    ValidationWebServicesUtil.validateCVName(nameIdDTOList, fieldToCompare);

                }
            }
        }

    }
}
