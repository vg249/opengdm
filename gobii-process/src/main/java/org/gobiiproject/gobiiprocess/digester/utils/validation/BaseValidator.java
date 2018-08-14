package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.printMissingFieldError;

public abstract class BaseValidator {
    abstract void validate(ValidationUnit conditions, String dir);

    /**
     * Checks that the fileName exists only once. Returns true if file exists once else false.
     *
     * @param dir         directory
     * @param fileName    file-name
     * @param listOfFiles list to store files with required extension
     * @return boolean value if there is a single file or not.
     */
    boolean checkForSingleFileExistence(String dir, String fileName, List<String> listOfFiles) {
        // If there is an error in accessing path. Error already printed.
        getFilesWithExtension(dir, fileName, listOfFiles);
        if (listOfFiles.size() != 1) {
            ErrorLogger.logError("There should be only one " + fileName + " file in the folder ", dir);
            return false;
        } else
            return true;
    }

    boolean getFilesWithExtension(String dir, String fileExtension, List<String> filesWithExtension) {
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(Paths.get(dir), fileExtension);
            for (Path entry : files) {
                filesWithExtension.add(entry.getFileName().toString());
            }
        } catch (Exception e) {
            ErrorLogger.logError("Error in accessing path", dir);
            return false;
        }
        return true;
    }

    /**
     * Parses the validation rules and gives the rules which are required and not  unique
     *
     * @param fileName   name of file
     * @param conditions conditions
     */
    void validateRequiredColumns(String fileName, List<ConditionUnit> conditions) {
        List<String> requiredFields = new ArrayList<>();
        for (ConditionUnit condition : conditions) {
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && !(condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES))) {
                if (!requiredFields.contains(condition.columnName))
                    requiredFields.add(condition.columnName);
            }
        }
        if (requiredFields.size() > 0)
            validateColumns(fileName, requiredFields);
    }

    /**
     * Parses the validation rules and gives the rules which are required and unique
     *
     * @param fileName   name of file
     * @param conditions conditions
     */
    void validateRequiredUniqueColumns(String fileName, List<ConditionUnit> conditions) {
        List<String> requiredUniqueColumns = new ArrayList<>();
        for (ConditionUnit condition : conditions) {
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && (condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES))) {
                if (!requiredUniqueColumns.contains(condition.columnName))
                    requiredUniqueColumns.add(condition.columnName);
            }
        }
        if (requiredUniqueColumns.size() > 0)
            validateColumns(fileName, requiredUniqueColumns);
    }

    /**
     * Validates that the combination of columns is unique
     *
     * @param fileName       fileName
     * @param validationUnit validation conditions
     */
    void validateUniqueColumnList(String fileName, ValidationUnit validationUnit) {
        for (ConditionUnit condition : validationUnit.getConditions()) {
            if (condition.uniqueColumns != null && condition.uniqueColumns.size() > 0) {
                List<String> uniqueColumns = condition.uniqueColumns;
                List<List<String>> fileColumns = new ArrayList<>();
                for (String column : uniqueColumns) {
                    List<String> fileColumn = getFileColumn(fileName, column);
                    if (fileColumn.size() != 0) {
                        fileColumns.add(getFileColumn(fileName, column));
                    } else {
                        ErrorLogger.logError(column, " doesnot exist in file " + fileName);
                    }
                }
                int size = fileColumns.get(0).size();
                for (List<String> column : fileColumns) {
                    if (column.size() != size) {
                        ErrorLogger.logError(fileName, " has file columns of irregular size.");
                        return;
                    }
                }
                List<String> concatList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    String value = null;
                    for (List<String> column : fileColumns) {
                        value = value + "$@$" + column.get(i);
                    }
                    if (concatList.contains(value)) {
                        ErrorLogger.logError(String.valueOf(uniqueColumns), " combination is not unique");
                        return;
                    } else concatList.add(value);
                }
            }
        }
    }

    /**
     * Validates required columns are present and are not null or empty.
     *
     * @param fileName fileName
     * @param columns  Columns
     */
    private void validateColumns(String fileName, List<String> columns) {
        List<String[]> collect = readFileIntoMemory(fileName);
        if (collect != null) {
            List<String> fileHeaders = Arrays.asList(collect.remove(0));
            fileHeaders = fileHeaders.stream().map(String::trim).collect(Collectors.toList());
            Set<Integer> sortedColumnNumbers = new TreeSet<>();
            for (String columnName : columns) {
                if (fileHeaders.contains(columnName)) {
                    sortedColumnNumbers.add(fileHeaders.indexOf(columnName));
                } else {
                    ErrorLogger.logError("Could not find required column : " + columnName + " in input file", fileName);
                }
            }
            if (sortedColumnNumbers.size() == 0) return;
            for (String[] line : collect) {
                if (line.length <= ((TreeSet<Integer>) sortedColumnNumbers).last()) {
                    ErrorLogger.logError(fileName, " is corrupted. Please check file for irregular size columns.");
                    return;
                }
                for (Integer colNo : sortedColumnNumbers) {
                    if (ValidationUtil.isNullAndEmpty(line[colNo])) {
                        ErrorLogger.logError("In file " + fileName, "column " + colNo + " is required. It should not be null or empty.");
                    }
                }
            }
        }
    }

    /**
     * Read file into a list
     *
     * @param fileName file name
     * @return list
     */
    List<String[]> readFileIntoMemory(String fileName) {
        List<String[]> collect = null;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            collect = stream.map(line -> line.split("\t")).collect(Collectors.toList());
            if (collect == null || collect.size() == 0) {
                ErrorLogger.logError(fileName, " is empty.");
                collect = null;
            }
        } catch (IOException e) {
            ErrorLogger.logError("Could not read the input file", fileName);
        }
        return collect;
    }

    /**
     * Validates that a particular column is same in both the files
     *
     * @param filePath  File Path
     * @param condition Condition Unit
     */
    void validateColumnBetweenFiles(String filePath, ConditionUnit condition) {
        if (condition.typeName != null) {
            if (ValidationUtil.getAllowedExtensions().contains(condition.typeName.substring(condition.typeName.indexOf('.') + 1))) {
                if (condition.fieldToCompare != null) {
                    String comparisonFileName = condition.typeName;
                    String columnName = condition.columnName;
                    String fieldToCompare = condition.fieldToCompare;
                    List<String> filesList = new ArrayList<>();
                    if (getFilesWithExtension(new File(filePath).getParent(), comparisonFileName, filesList)) {
                        if (filesList.size() != 1) {
                            ErrorLogger.logError("There should be only one file in the folder ", new File(filePath).getParent());
                            return;
                        }
                        String comparisonFilePath = new File(filePath).getParent() + "/" + comparisonFileName;
                        List<String> fileColumnElements = getFileColumn(filePath, columnName);
                        if (fileColumnElements.size() == 0) return;
                        else {
                            fileColumnElements = fileColumnElements.stream().distinct().collect(Collectors.toList());
                            Collections.sort(fileColumnElements);
                        }
                        List<String> comparisonFileColumnElements = getFileColumn(comparisonFilePath, fieldToCompare);
                        if (comparisonFileColumnElements.size() == 0) {
                            ErrorLogger.logError(fieldToCompare, " doesnot exist in file " + comparisonFilePath);
                            return;
                        } else {
                            comparisonFileColumnElements = comparisonFileColumnElements.stream().distinct().collect(Collectors.toList());
                            Collections.sort(comparisonFileColumnElements);
                        }
                        if (!fileColumnElements.equals(comparisonFileColumnElements)) {
                            ErrorLogger.logError(fieldToCompare, "is not same in " + filePath + "\t" + comparisonFilePath);
                        }
                    }
                } else {
                    printMissingFieldError("File", "fieldToCompare");
                }
            } else
                ErrorLogger.logError(condition.typeName, " is not a valid file extension.");
        } else {
            printMissingFieldError("File", "typeName");
        }
    }

    /**
     * Reads the particular column from the file.
     * If column does not exist it returns an empty set.
     *
     * @param filepath filePath
     * @param column   column name
     * @return column values
     */
    private List<String> getFileColumn(String filepath, String column) {
        List<String> fileColumnElements = new ArrayList<>();
        List<String[]> file = readFileIntoMemory(filepath);
        List<String> fileHeaders = Arrays.asList(file.remove(0));
        fileHeaders = fileHeaders.stream().map(String::trim).collect(Collectors.toList());
        int fileCoulmnIndex = fileHeaders.indexOf(column);
        if (fileCoulmnIndex >= 0) {
            for (String[] line : file) {
                if (line.length > fileCoulmnIndex) {
                    fileColumnElements.add(line[fileCoulmnIndex]);
                } else {
                    ErrorLogger.logError(column, " value doesnot exist in file. Please check the contents of file. " + filepath);

                }
            }
        }
        return fileColumnElements;
    }
}
