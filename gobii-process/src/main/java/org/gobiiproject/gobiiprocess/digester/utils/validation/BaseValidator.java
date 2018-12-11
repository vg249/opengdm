package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;

import java.io.File;
import java.util.*;

import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.*;

abstract class BaseValidator {
    abstract boolean validate(ValidationUnit validationUnit, String dir, List<Failure> failureList);

    /**
     * Checks that the fileName exists only once. Returns true if file exists once else false.
     *
     * @param dir         directory
     * @param fileName    file-name
     * @param failureList error list
     * @return boolean value if there is a single file or not.
     */
    boolean checkForSingleFileExistence(String dir, String fileName, List<Failure> failureList) throws MaximumErrorsValidationException {
        // If there is an error in accessing path. Error already printed.
        List<String> listOfFiles = new ArrayList<>();
        getFilesWithExtension(dir, fileName, listOfFiles, failureList);
        if (listOfFiles.size() < 1) return false;
        if (listOfFiles.size() > 1) {
            Failure failure = new Failure();
            failure.reason = FailureTypes.MORE_THAN_ONE_FILE;
            failure.values.add(fileName);
            failure.values.add(dir);
            addMessageToList(failure, failureList);
            return false;
        } else
            return true;
    }

    List<Failure> beginValidation(String fileName, ValidationUnit validationUnit) {
        List<Failure> failureList = new ArrayList<>();
        failureList.addAll(validateRequiredColumns(fileName, validationUnit.getConditions()));
        failureList.addAll(validateRequiredUniqueColumns(fileName, validationUnit.getConditions()));
        failureList.addAll(validateOptionalNotNullColumns(fileName, validationUnit.getConditions()));
        failureList.addAll(validateUniqueColumnList(fileName, validationUnit));
        failureList.addAll(validateFileShouldExist(fileName, validationUnit));
        failureList.addAll(validateColumnDBandFile(fileName, validationUnit));
        return failureList;
    }

    /**
     * Parses the validation rules and gives the rules which are required and not  unique
     *
     * @param fileName   name of file
     * @param conditions conditions
     */
    private List<Failure> validateRequiredColumns(String fileName, List<ConditionUnit> conditions) {
        List<String> requiredFields = new ArrayList<>();
        for (ConditionUnit condition : conditions)
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && !(condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES))) {
                if (!requiredFields.contains(condition.columnName)) {
                    requiredFields.add(condition.columnName);
                }
            }
        List<Failure> failureList = new ArrayList<>();
        if (requiredFields.size() > 0) {
            try {
                validateColumns(fileName, requiredFields, failureList);
            } catch (MaximumErrorsValidationException e) {
                //Don't do any thing. This implies that particular error list is full.
            }
        }
        return failureList;
    }

    /**
     * Parses the validation rules and gives the rules which are required and unique
     *
     * @param fileName   name of file
     * @param conditions conditions
     */
    private List<Failure> validateRequiredUniqueColumns(String fileName, List<ConditionUnit> conditions) {
        List<String> requiredUniqueColumns = new ArrayList<>();
        for (ConditionUnit condition : conditions)
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && (condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES)))
                if (!requiredUniqueColumns.contains(condition.columnName))
                    requiredUniqueColumns.add(condition.columnName);

        List<Failure> failureList = new ArrayList<>();
        if (requiredUniqueColumns.size() > 0) {
            try {
                validateUniqueColumns(fileName, requiredUniqueColumns, failureList);
            } catch (MaximumErrorsValidationException e) {
                //Don't do any thing. This implies that particular error list is full.
            }
        }
        return failureList;
    }

    /**
     * Parses the validation rules and gives the rules which are required and not unique
     *
     * @param fileName   name of file
     * @param conditions conditions
     */
    private List<Failure> validateOptionalNotNullColumns(String fileName, List<ConditionUnit> conditions) {
        List<String> requiredFields = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Failure> failureList = new ArrayList<>();
        try {
            getHeaders(fileName, headers, failureList);
        } catch (MaximumErrorsValidationException e) {
            // Maximum errors of this type came in. return.
            return failureList;
        }
        for (ConditionUnit condition : conditions)
            if (condition.required.equalsIgnoreCase(ValidationConstants.NO) && (condition.nullAllowed != null && condition.nullAllowed.equalsIgnoreCase(ValidationConstants.NO)))
                if (headers.contains(condition.columnName) && !requiredFields.contains(condition.columnName))
                    requiredFields.add(condition.columnName);
        if (requiredFields.size() > 0) {
            try {
                validateColumns(fileName, requiredFields, failureList);
            } catch (MaximumErrorsValidationException e) {
                //Don't do any thing. This implies that particular error list is full.
            }
        } else {
            failureList = new ArrayList<>();
        }
        return failureList;
    }

    /**
     * Validates that the combination of columns is unique
     *
     * @param fileName       fileName
     * @param validationUnit validation conditions
     */
    private List<Failure> validateUniqueColumnList(String fileName, ValidationUnit validationUnit) {
        List<Failure> failures = new ArrayList<>();
        for (ConditionUnit condition : validationUnit.getConditions()) {
            if (condition.uniqueColumns != null && condition.uniqueColumns.size() > 0) {
                List<Failure> failureList = new ArrayList<>();
                try {
                    validateUniqueColumnListHelper(fileName, condition, failureList);
                } catch (MaximumErrorsValidationException e) {
                    ////Don't do any thing. This implies that particular error list is full.
                }
                failures.addAll(failureList);
            }
        }
        return failures;
    }


    /**
     * Checks that the file exists
     *
     * @param fileName       file name
     * @param validationUnit validation unit
     */
    private List<Failure> validateFileShouldExist(String fileName, ValidationUnit validationUnit) {
        List<Failure> failures = new ArrayList<>();
        for (ConditionUnit condition : validationUnit.getConditions()) {
            List<Failure> failureList = new ArrayList<>();
            if (condition.fileShouldExist != null) {
                String existenceFile = condition.fileShouldExist;
                List<String> files = new ArrayList<>();
                try {
                    getFilesWithExtension(new File(fileName).getParent(), existenceFile, files, failureList);
                    if (files.size() != 1) {
                        processFileError(existenceFile, files.size(), failureList);
                    }
                } catch (MaximumErrorsValidationException e) {
                    ////Don't do any thing. This implies that particular error list is full.
                }
            }
            failures.addAll(failureList);
        }
        return failures;
    }


    /**
     * Checks if there is file comparision and does that
     *
     * @param filePath       File Path
     * @param validationUnit Validation Unit
     */
    private List<Failure> validateColumnDBandFile(String filePath, ValidationUnit validationUnit) {
        List<Failure> failures = new ArrayList<>();
        for (ConditionUnit condition : validationUnit.getConditions()) {
            List<Failure> failureList = new ArrayList<>();
            try {
                if (condition.fileExistenceCheck != null) {
                    validateFileExistenceCheck(filePath, condition, failureList);
                } else if (condition.type != null) {
                    if (condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                        validateColumnBetweenFiles(filePath, condition, failureList);
                    } else if (condition.type.equalsIgnoreCase(ValidationConstants.DB)) {
                        validateDataBasecalls(filePath, condition, failureList);
                    } else {
                        Failure failure = new Failure();
                        failure.reason = FailureTypes.UNDEFINED_CONDITION_TYPE;
                        failure.columnName.add(condition.type);
                        addMessageToList(failure, failureList);
                    }
                }
            } catch (MaximumErrorsValidationException e) {
                ////Don't do any thing. This implies that particular error list is full.;
            }
            failures.addAll(failureList);
        }
        return failures;
    }
}