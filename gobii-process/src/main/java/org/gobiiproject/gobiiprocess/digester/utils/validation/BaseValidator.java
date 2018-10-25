package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.DigesterFileExtensions;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.addMessageToList;
import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.printMissingFieldError;

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

    private boolean getFilesWithExtension(String dir, String fileExtension, List<String> filesWithExtension, List<Failure> failureList) throws MaximumErrorsValidationException {
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(Paths.get(dir), fileExtension);
            for (Path entry : files) {
                filesWithExtension.add(entry.getFileName().toString());
            }
        } catch (Exception e) {
            Failure failure = new Failure();
            failure.reason = FailureTypes.ERROR_ACCESSING_FILE;
            failure.values.add(dir);
            addMessageToList(failure, failureList);
            return false;
        }
        return true;
    }

    /**
     * Parses the validation rules and gives the rules which are required and not  unique
     *
     * @param fileName    name of file
     * @param conditions  conditions
     * @param failureList failure list
     */
    void validateRequiredColumns(String fileName, List<ConditionUnit> conditions, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> requiredFields = new ArrayList<>();
        for (ConditionUnit condition : conditions)
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && !(condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES)))
                if (!requiredFields.contains(condition.columnName))
                    requiredFields.add(condition.columnName);
        if (requiredFields.size() > 0)
            validateColumns(fileName, requiredFields, failureList);
    }

    /**
     * Parses the validation rules and gives the rules which are required and not unique
     *
     * @param fileName    name of file
     * @param conditions  conditions
     * @param failureList failure list
     */
    void validateNotNullOptionalColumns(String fileName, List<ConditionUnit> conditions, List<Failure> failureList) throws MaximumErrorsValidationException {
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
     * Parses the validation rules and gives the rules which are required and unique
     *
     * @param fileName    name of file
     * @param conditions  conditions
     * @param failureList failure list
     */
    void validateRequiredUniqueColumns(String fileName, List<ConditionUnit> conditions, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> requiredUniqueColumns = new ArrayList<>();
        for (ConditionUnit condition : conditions)
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && (condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES)))
                if (!requiredUniqueColumns.contains(condition.columnName))
                    requiredUniqueColumns.add(condition.columnName);

        if (requiredUniqueColumns.size() > 0)
            validateUniqueColumns(fileName, requiredUniqueColumns, failureList);
    }

    /**
     * Validates that the combination of columns is unique
     *
     * @param fileName       fileName
     * @param validationUnit validation conditions
     * @param failureList    failure list
     */
    void validateUniqueColumnList(String fileName, ValidationUnit validationUnit, List<Failure> failureList) throws MaximumErrorsValidationException {
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
     * Validates required unique columns are present and are not null or empty.
     *
     * @param fileName    fileName
     * @param columns     Columns
     * @param failureList failure list
     */
    private void validateUniqueColumns(String fileName, List<String> columns, List<Failure> failureList) throws MaximumErrorsValidationException {
        if (columns.size() == 0) return;
        List<String[]> collect = readFileIntoMemory(fileName, failureList);
        TreeSet<Integer> sortedColumnNumbers = new TreeSet<>();
        if (collect != null && getColumnIndices(fileName, columns, collect, sortedColumnNumbers, failureList))
            for (Integer colNo : sortedColumnNumbers) {
                TreeSet<String> map = new TreeSet<>();
                for (String[] line : collect)
                    if (ValidationUtil.isNullAndEmpty(line[colNo])) {
                        Failure failure = new Failure();
                        failure.reason = FailureTypes.NULL_VALUE;
                        failure.columnName.add(columns.get(colNo));
                        addMessageToList(failure, failureList);
                    } else {
                        if (map.contains(line[colNo])) {
                            Failure failure = new Failure();
                            failure.reason = FailureTypes.DUPLICATE_FOUND;
                            failure.columnName.add(columns.get(colNo));
                            failure.values.add(line[colNo]);
                            addMessageToList(failure, failureList);
                        } else map.add(line[colNo]);
                    }
            }
    }

    /**
     * Validates required columns are present and are not null or empty.
     *
     * @param fileName    fileName
     * @param columns     Columns
     * @param failureList failure list
     */
    private void validateColumns(String fileName, List<String> columns, List<Failure> failureList) throws MaximumErrorsValidationException {
        if (columns.size() == 0) return;
        List<String[]> collect = readFileIntoMemory(fileName, failureList);
        TreeSet<Integer> sortedColumnNumbers = new TreeSet<>();
        if (collect != null && getColumnIndices(fileName, columns, collect, sortedColumnNumbers, failureList))
            for (String[] line : collect)
                for (Integer colNo : sortedColumnNumbers)
                    if (ValidationUtil.isNullAndEmpty(line[colNo])) {
                        Failure failure = new Failure();
                        failure.reason = FailureTypes.NULL_VALUE;
                        failure.columnName.add(columns.get(colNo));
                        addMessageToList(failure, failureList);
                    }
    }

    /**
     * Gets the column numbers.
     *
     * @param fileName            File Name
     * @param columns             Column Names
     * @param collect             inMemoryFile
     * @param sortedColumnNumbers sorted column numbers
     * @param failureList         failure list
     * @return status
     */
    private boolean getColumnIndices(String fileName, List<String> columns, List<String[]> collect, TreeSet<Integer> sortedColumnNumbers, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> fileHeaders = Arrays.asList(collect.remove(0));

        fileHeaders = fileHeaders.stream().map(String::trim).collect(Collectors.toList());
        for (String columnName : columns)
            if (fileHeaders.contains(columnName))
                sortedColumnNumbers.add(fileHeaders.indexOf(columnName));
            else {
                Failure failure = new Failure();
                failure.reason = FailureTypes.COLUMN_NOT_FOUND;
                failure.columnName.add(columnName);
                addMessageToList(failure, failureList);
            }
        if (sortedColumnNumbers.size() == 0) return false;
        for (String[] line : collect)
            if (line.length <= sortedColumnNumbers.last()) {
                Failure failure = new Failure();
                failure.reason = FailureTypes.CORRUPTED_FILE;
                failure.values.add(fileName);
                addMessageToList(failure, failureList);
                return false;
            }
        return true;
    }

    /**
     * Read file into a list
     *
     * @param fileName    file name
     * @param failureList failure list
     * @return list
     */
    List<String[]> readFileIntoMemory(String fileName, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String[]> collect = null;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            collect = stream.map(line -> line.split("\t")).collect(Collectors.toList());
            if (collect == null || collect.size() == 0) {
                Failure failure = new Failure();
                failure.reason = FailureTypes.EMPTY_FILE;
                failure.values.add(fileName);
                addMessageToList(failure, failureList);
                collect = null;
            }
        } catch (IOException e) {
            Failure failure = new Failure();
            failure.reason = FailureTypes.EXCEPTION_IN_READING_FILE;
            failure.values.add(fileName);
            addMessageToList(failure, failureList);
        }
        return collect;
    }


    /**
     * Checks if there is file comparision and does that
     *
     * @param filePath       File Path
     * @param validationUnit Validation Unit
     * @param failureList    failure list
     * @throws MaximumErrorsValidationException maximum error exception
     */
    void validateColumnsBetweenFiles(String filePath, ValidationUnit validationUnit, List<Failure> failureList) throws MaximumErrorsValidationException {
        for (ConditionUnit condition : validationUnit.getConditions()) {
            if (condition.fileExistenceCheck != null) {
                validateFileExistenceCheck(filePath, validationUnit, failureList);
            } else if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                validateColumnBetweenFiles(filePath, condition, failureList);
            }
        }
    }

    /**
     * Validates that a particular column is same in both the files
     *
     * @param filePath    File Path
     * @param condition   Condition Unit
     * @param failureList failure list
     */
    private void validateColumnBetweenFiles(String filePath, ConditionUnit condition, List<Failure> failureList) throws MaximumErrorsValidationException {
        String parentDirectory = new File(filePath).getParent();
        if (condition.typeName != null) {
            if (DigesterFileExtensions.allowedExtensions.contains(condition.typeName.substring(condition.typeName.indexOf('.') + 1))) {
                if (condition.fieldToCompare != null) {
                    String comparisonFileName = condition.typeName;
                    List<String> fieldColumns = condition.fieldColumns;
                    List<String> fieldToCompare = condition.fieldToCompare;
                    List<String> filesList = new ArrayList<>();
                    if (getFilesWithExtension(parentDirectory, comparisonFileName, filesList, failureList)) {
                        if (filesList.size() != 1) {
                            Failure failure = new Failure();
                            failure.reason = FailureTypes.MORE_THAN_ONE_FILE;
                            failure.values.add(comparisonFileName);
                            addMessageToList(failure, failureList);
                            return;
                        }
                        String comparisonFilePath = parentDirectory + "/" + comparisonFileName;
                        List<String> fileColumnElements;
                        if (fieldToCompare.size() > 1) {
                            fileColumnElements = getFileColumns(comparisonFilePath, fieldColumns, failureList);
                        } else
                            fileColumnElements = getFileColumn(comparisonFilePath, fieldColumns.get(0), failureList);
                        if (fileColumnElements.size() == 0) return;
                        else Collections.sort(fileColumnElements);

                        List<String> comparisonFileColumnElements;
                        if (fieldToCompare.size() > 1) {
                            comparisonFileColumnElements = getFileColumns(comparisonFilePath, fieldToCompare, failureList);
                        } else
                            comparisonFileColumnElements = getFileColumn(comparisonFilePath, fieldToCompare.get(0), failureList);
                        if (comparisonFileColumnElements.size() == 0) return;
                        else Collections.sort(comparisonFileColumnElements);

                        // If it is only unique list
                        if (condition.uniqueFileCheck != null && condition.uniqueFileCheck.equalsIgnoreCase(ValidationConstants.YES)) {
                            fileColumnElements = fileColumnElements.stream().distinct().collect(Collectors.toList());
                            comparisonFileColumnElements = comparisonFileColumnElements.stream().distinct().collect(Collectors.toList());
                        }

                        if (!fileColumnElements.containsAll(comparisonFileColumnElements)) {
                            Failure failure = new Failure();
                            failure.reason = FailureTypes.VALUE_MISMATCH;
                            failure.columnName.add(fieldColumns.stream().collect(Collectors.joining(",")));
                            failure.columnName.add(fieldToCompare.stream().collect(Collectors.joining(",")));
                            addMessageToList(failure, failureList);
                        }
                    }
                } else {
                    printMissingFieldError("File", "fieldToCompare", FailureTypes.CORRUPTED_VALIDATION_FILE, failureList);
                }
            } else {
                Failure failure = new Failure();
                failure.reason = FailureTypes.INVALID_FILE_EXTENSIONS;
                failure.values.add(condition.typeName);
                addMessageToList(failure, failureList);
            }
        } else {
            printMissingFieldError("File", "typeName", FailureTypes.CORRUPTED_VALIDATION_FILE, failureList);
        }

    }


    /**
     * Validation based on the existence of a file.
     * Do something if a file exists and something else if it doesn't exist.
     *
     * @param fileName       Name of the file
     * @param validationUnit Validation Unit
     * @param failureList    failure list
     */
    private void validateFileExistenceCheck(String fileName, ValidationUnit validationUnit, List<Failure> failureList) throws MaximumErrorsValidationException {
        for (ConditionUnit condition : validationUnit.getConditions())
            if (condition.fileExistenceCheck != null) {
                String existenceFile = condition.fileExistenceCheck;
                List<String> files = new ArrayList<>();
                boolean shouldFileExist = condition.fileExists.equalsIgnoreCase(ValidationConstants.YES);
                getFilesWithExtension(new File(fileName).getParent(), existenceFile, files, failureList);
                if (files.size() > 1) {
                    Failure failure = new Failure();
                    failure.reason = FailureTypes.MORE_THAN_ONE_FILE;
                    failure.values.add(fileName);
                    addMessageToList(failure, failureList);
                    return;
                }
                if ((shouldFileExist && files.size() == 1) || (!shouldFileExist && files.size() == 0)) {
                    // Condition is satisfied
                    if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.DB)) {
                        //TODO: Implement the database call
                    } else if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE))
                        validateColumnBetweenFiles(fileName, condition, failureList);
                    else {
                        Failure failure = new Failure();
                        failure.reason = FailureTypes.UNDEFINED_CONDITION_TYPE;
                        failure.values.add(condition.type);
                        addMessageToList(failure, failureList);
                    }
                }
            }
    }


    /**
     * Reads the particular column from the file.
     * If column does not exist it returns an empty set.
     *
     * @param filepath    filePath
     * @param column      column name
     * @param failureList failure list
     * @return column values
     */
    private List<String> getFileColumn(String filepath, String column, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String> fileColumnElements = new ArrayList<>();
        List<String[]> file = readFileIntoMemory(filepath, failureList);
        List<String> fileHeaders = Arrays.asList(file.remove(0));
        fileHeaders = fileHeaders.stream().map(String::trim).collect(Collectors.toList());
        int fileColumnIndex = fileHeaders.indexOf(column);
        if (fileColumnIndex >= 0)
            for (String[] line : file)
                if (line.length > fileColumnIndex)
                    fileColumnElements.add(line[fileColumnIndex]);
                else {
                    Failure failure = new Failure();
                    failure.reason = FailureTypes.COLUMN_VALUE_NOT_FOUND;
                    failure.columnName.add(column);
                    addMessageToList(failure, failureList);
                }
        return fileColumnElements;
    }

    /**
     * If all the elements are found it adds the columns combination to a list and returns it.
     * Duplicated are allowed here
     * Else returns dummy list
     *
     * @param filepath    file name
     * @param columns     columns list
     * @param failureList failure list
     * @return list
     * @throws MaximumErrorsValidationException maximum error exception
     */
    private List<String> getFileColumns(String filepath, List<String> columns, List<Failure> failureList) throws MaximumErrorsValidationException {

        List<List<String>> fileColumns = new ArrayList<>();
        for (String column : columns) {
            List<String> fileColumn = getFileColumn(filepath, column, failureList);
            if (fileColumn.size() != 0) {
                fileColumns.add(fileColumn);
            } else {
                Failure failure = new Failure();
                failure.reason = FailureTypes.COLUMN_NOT_FOUND;
                failure.columnName.add(column);
                addMessageToList(failure, failureList);
                return new ArrayList<>();
            }
        }

        if (!verifyEqualSizeColumn(failureList, fileColumns)) return new ArrayList<>();

        List<String> concatList = new ArrayList<>();
        for (int i = 0; i < fileColumns.get(0).size(); i++) {
            String value = "";
            for (List<String> column : fileColumns)
                if (value.equals("")) value = column.get(i);
                else value = value + "$@$" + column.get(i);
            concatList.add(value);
        }
        return concatList;
    }

    /**
     * Verifies all the columns are of equal size
     *
     * @param failureList failure list
     * @param fileColumns file columns
     * @return status
     */
    private boolean verifyEqualSizeColumn(List<Failure> failureList, List<List<String>> fileColumns) throws MaximumErrorsValidationException {
        int size = fileColumns.get(0).size();
        for (List<String> column : fileColumns)
            if (column.size() != size) {
                Failure failure = new Failure();
                failure.reason = FailureTypes.INVALID_COLUMN_SIZE;
                addMessageToList(failure, failureList);
                return false;
            }
        return true;
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
    boolean checkForHeaderExistence(String fileName, ConditionUnit conditionUnit, List<Failure> failureList) throws MaximumErrorsValidationException {
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

    boolean getHeaders(String fileName, List<String> headers, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<String[]> collectList = readFileIntoMemory(fileName, failureList);
        if (collectList == null) return false;
        else {
            headers.addAll(Arrays.asList(collectList.get(0)));
            return true;
        }
    }


}