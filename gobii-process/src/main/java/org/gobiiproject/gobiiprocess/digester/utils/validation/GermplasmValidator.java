package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.*;

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
            for (ConditionUnit condition : validationUnit.getConditions()) {
                System.out.println(condition.type);
                System.out.println(condition.typeName);
                if (condition.type != null && condition.type.equalsIgnoreCase("DB") && condition.fieldToCompare != null && condition.fieldToCompare.equalsIgnoreCase("species_name")) {
                    validateSpeciesName(fileName);
                }
            }
        } else {
            return;
        }


    }

    private void validateSpeciesName(String fileName) {
        List<String[]> collect = readFileIntoMemory(fileName);
        if (collect != null) {
            List<String> headers = Arrays.asList(collect.get(0));
            if (headers.contains("species_name")) {
                int speciesNameIndex = headers.indexOf("species_name");
                collect.remove(0);
                Set<String> speciesNameList = new HashSet<String>();
                for (String[] fileRow : collect) {
                    List<String> fileRowList = Arrays.asList(fileRow);
                    if (isNullAndEmpty(fileRowList.get(speciesNameIndex)))
                        System.out.println();
                        speciesNameList.add(fileRowList.get(speciesNameIndex));
                }
                List<NameIdDTO> nameIdDTOList = new ArrayList<>();
                for (String speciesName : speciesNameList) {
                    NameIdDTO nameIdDTO = new NameIdDTO();
                    nameIdDTO.setName(speciesName);
                    nameIdDTOList.add(nameIdDTO);
                }
                ValidationWebServicesUtil.validateSpeciesName(nameIdDTOList);
            }
        }

    }
}
