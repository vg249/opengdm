package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;

import java.util.*;

import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.*;

public abstract class BaseValidator {
    abstract void validate(ValidationUnit validationUnit, String dir, List<Failure> errorList) throws MaximumErrorsValidationException;

    /**
     * Checks that the fileName exists only once. Returns true if file exists once else false.
     *
     * @param dir         directory
     * @param fileName    file-name
     * @param listOfFiles list to store files with required extension
     * @param failureList error list
     * @return boolean value if there is a single file or not.
     */
    boolean checkForSingleFileExistence(String dir, String fileName, List<String> listOfFiles, List<Failure> failureList) throws MaximumErrorsValidationException {
        // If there is an error in accessing path. Error already printed.
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

    void beginValidation(String fileName, ValidationUnit validationUnit, List<Failure> failureList) throws MaximumErrorsValidationException {
        validateRequiredColumns(fileName, validationUnit.getConditions(), failureList);
        validateRequiredUniqueColumns(fileName, validationUnit.getConditions(), failureList);
        validateOptionalNotNullColumns(fileName, validationUnit.getConditions(), failureList);
        validateUniqueColumnList(fileName, validationUnit, failureList);
        validateColumnsBetweenFiles(fileName, validationUnit, failureList);
        validateDataBasecalls(validationUnit, failureList, fileName);
    }

    /**
     * Parses the validation rules and gives the rules which are required and not  unique
     *
     * @param fileName    name of file
     * @param conditions  conditions
     * @param failureList failure list
     */
    private void validateRequiredColumns(String fileName, List<ConditionUnit> conditions, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> requiredFields = new ArrayList<>();
        for (ConditionUnit condition : conditions)
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && !(condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES)))
                if (!requiredFields.contains(condition.columnName))
                    requiredFields.add(condition.columnName);
        if (requiredFields.size() > 0)
            validateColumns(fileName, requiredFields, failureList);
    }

    /**
     * Parses the validation rules and gives the rules which are required and unique
     *
     * @param fileName    name of file
     * @param conditions  conditions
     * @param failureList failure list
     */
    private void validateRequiredUniqueColumns(String fileName, List<ConditionUnit> conditions, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> requiredUniqueColumns = new ArrayList<>();
        for (ConditionUnit condition : conditions)
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && (condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES)))
                if (!requiredUniqueColumns.contains(condition.columnName))
                    requiredUniqueColumns.add(condition.columnName);

        if (requiredUniqueColumns.size() > 0)
            validateUniqueColumns(fileName, requiredUniqueColumns, failureList);
    }

    /**
     * Parses the validation rules and gives the rules which are required and not unique
     *
     * @param fileName    name of file
     * @param conditions  conditions
     * @param failureList failure list
     */
    private void validateOptionalNotNullColumns(String fileName, List<ConditionUnit> conditions, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> requiredFields = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        getHeaders(fileName, headers, failureList);
        for (ConditionUnit condition : conditions)
            if (condition.required.equalsIgnoreCase(ValidationConstants.NO) && !(condition.nullAllowed != null && condition.nullAllowed.equalsIgnoreCase(ValidationConstants.NO)))
                if (headers.contains(condition.columnName) && !requiredFields.contains(condition.columnName))
                    requiredFields.add(condition.columnName);
        if (requiredFields.size() > 0)
            validateColumns(fileName, requiredFields, failureList);
    }

    /**
     * Validates that the combination of columns is unique
     *
     * @param fileName       fileName
     * @param validationUnit validation conditions
     * @param failureList    failure list
     */
    private void validateUniqueColumnList(String fileName, ValidationUnit validationUnit, List<Failure> failureList) throws MaximumErrorsValidationException {
        for (ConditionUnit condition : validationUnit.getConditions())
            if (condition.uniqueColumns != null && condition.uniqueColumns.size() > 0) {
                List<String> uniqueColumns = condition.uniqueColumns;
                List<List<String>> fileColumns = new ArrayList<>();
                for (String column : uniqueColumns) {
                    List<String> fileColumn = getFileColumn(fileName, column, failureList);
                    if (fileColumn.size() != 0) {
                        fileColumns.add(fileColumn);
                    } else {
                        Failure failure = new Failure();
                        failure.reason = FailureTypes.COLUMN_NOT_FOUND;
                        failure.columnName.add(column);
                        addMessageToList(failure, failureList);
                        return;
                    }
                }
                if (!verifyEqualSizeColumn(failureList, fileColumns)) return;

                List<String> concatList = new ArrayList<>();
                for (int i = 0; i < fileColumns.get(0).size(); i++) {
                    String value = "";
                    for (List<String> column : fileColumns) {
                        if (value.equals("")) value = column.get(i);
                        else value = value + "$@$" + column.get(i);
                    }
                    if (concatList.contains(value)) {
                        Failure failure = new Failure();
                        failure.reason = FailureTypes.NOT_UNIQUE;
                        failure.columnName.addAll(uniqueColumns);
                        addMessageToList(failure, failureList);
                    } else concatList.add(value);
                }
            }
    }

    /**
     * Checks if there is file comparision and does that
     *
     * @param filePath       File Path
     * @param validationUnit Validation Unit
     * @param failureList    failure list
     * @throws MaximumErrorsValidationException maximum error exception
     */
    private void validateColumnsBetweenFiles(String filePath, ValidationUnit validationUnit, List<Failure> failureList) throws MaximumErrorsValidationException {
        for (ConditionUnit condition : validationUnit.getConditions()) {
            if (condition.fileExistenceCheck != null) {
                validateFileExistenceCheck(filePath, validationUnit, failureList);
            } else if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                validateColumnBetweenFiles(filePath, condition, failureList);
            }
        }
    }

    private void validateDataBasecalls(ValidationUnit validationUnit, List<Failure> failureList, String fileName) throws MaximumErrorsValidationException {
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

    /**
     * Check for the header in a file.
     * Check the required field and returns status appropriately
     *
     * @param fileName      fileName
     * @param conditionUnit condition
     * @param failureList   failure list
     * @return header exists or not
     * @throws MaximumErrorsValidationException exception
     */
    private boolean checkForHeaderExistence(String fileName, ConditionUnit conditionUnit, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> headers = new ArrayList<>();
        if (getHeaders(fileName, headers, failureList)) {
            boolean headerExist = headers.contains(conditionUnit.columnName);
            // If header exists proceed
            if (headerExist) return true;
                // Header does not exist and condition states this as required
            else if (conditionUnit.required.equalsIgnoreCase("yes")) {
                Failure failure = new Failure();
                failure.reason = FailureTypes.COLUMN_NOT_FOUND;
                failure.columnName.add(conditionUnit.columnName);
                addMessageToList(failure, failureList);
                return false;
            } else return false;
        } else return false;
    }

    private boolean getHeaders(String fileName, List<String> headers, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String[]> collectList = readFileIntoMemory(fileName, failureList);
        if (collectList == null) return false;
        else {
            headers.addAll(Arrays.asList(collectList.get(0)));
            return true;
        }
    }
}