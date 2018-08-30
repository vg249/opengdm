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
    abstract void validate(ValidationUnit validationUnit, String dir, List<String> errorList);


    /**
     * Checks that the fileName exists only once. Returns true if file exists once else false.
     *
     * @param dir         directory
     * @param fileName    file-name
     * @param listOfFiles list to store files with required extension
     * @param errorList   error list
     * @return boolean value if there is a single file or not.
     */
    boolean checkForSingleFileExistence(String dir, String fileName, List<String> listOfFiles, List<String> errorList) {
        // If there is an error in accessing path. Error already printed.
        getFilesWithExtension(dir, fileName, listOfFiles, errorList);
        if (listOfFiles.size() < 1) return false;
        if (listOfFiles.size() > 1) {
            errorList.add("There should be only one " + fileName + " file in the folder " + dir);
            return false;
        } else
            return true;
    }

    private boolean getFilesWithExtension(String dir, String fileExtension, List<String> filesWithExtension, List<String> errorList) {
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(Paths.get(dir), fileExtension);
            for (Path entry : files) {
                filesWithExtension.add(entry.getFileName().toString());
            }
        } catch (Exception e) {
            errorList.add("Error in accessing path" + dir);
            return false;
        }
        return true;
    }

    /**
     * Parses the validation rules and gives the rules which are required and not  unique
     *
     * @param fileName   name of file
     * @param conditions conditions
     * @param errorList  error list
     */
    void validateRequiredColumns(String fileName, List<ConditionUnit> conditions, List<String> errorList) {
        List<String> requiredFields = new ArrayList<>();
        for (ConditionUnit condition : conditions) {
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && !(condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES))) {
                if (!requiredFields.contains(condition.columnName))
                    requiredFields.add(condition.columnName);
            }
        }
        if (requiredFields.size() > 0)
            validateColumns(fileName, requiredFields, errorList);
    }

    /**
     * Parses the validation rules and gives the rules which are required and unique
     *
     * @param fileName   name of file
     * @param conditions conditions
     * @param errorList  error list
     */
    void validateRequiredUniqueColumns(String fileName, List<ConditionUnit> conditions, List<String> errorList) {
        List<String> requiredUniqueColumns = new ArrayList<>();
        for (ConditionUnit condition : conditions) {
            if (condition.required.equalsIgnoreCase(ValidationConstants.YES) && (condition.unique != null && condition.unique.equalsIgnoreCase(ValidationConstants.YES))) {
                if (!requiredUniqueColumns.contains(condition.columnName))
                    requiredUniqueColumns.add(condition.columnName);
            }
        }
        if (requiredUniqueColumns.size() > 0)
            validateUniqueColumns(fileName, requiredUniqueColumns, errorList);
    }

    /**
     * Validates that the combination of columns is unique
     *
     * @param fileName       fileName
     * @param validationUnit validation conditions
     * @param errorList      error list
     */
    void validateUniqueColumnList(String fileName, ValidationUnit validationUnit, List<String> errorList) {
        for (ConditionUnit condition : validationUnit.getConditions()) {
            if (condition.uniqueColumns != null && condition.uniqueColumns.size() > 0) {
                List<String> uniqueColumns = condition.uniqueColumns;
                List<List<String>> fileColumns = new ArrayList<>();
                for (String column : uniqueColumns) {
                    List<String> fileColumn = getFileColumn(fileName, column, errorList);
                    if (fileColumn.size() != 0) {
                        fileColumns.add(getFileColumn(fileName, column, errorList));
                    } else {
                        errorList.add(column + " does not exist in file " + fileName);
                        return;
                    }
                }
                int size = fileColumns.get(0).size();
                for (List<String> column : fileColumns) {
                    if (column.size() != size) {
                        errorList.add(fileName + " has file columns of irregular size.");
                        return;
                    }
                }
                List<String> concatList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    String value = null;
                    for (List<String> column : fileColumns) {
                        value = value + "$@$" + column.get(i);
                    }
                    if (concatList.contains(value))
                        errorList.add(String.valueOf(uniqueColumns) + " combination is not unique");
                    else concatList.add(value);
                }
            }
        }
    }

    /**
     * Validates required unique columns are present and are not null or empty.
     *
     * @param fileName  fileName
     * @param columns   Columns
     * @param errorList error list
     */
    private void validateUniqueColumns(String fileName, List<String> columns, List<String> errorList) {
        if (columns.size() == 0) return;
        List<String[]> collect = readFileIntoMemory(fileName);
        if (collect != null) {
            TreeSet<Integer> sortedColumnNumbers = new TreeSet<>();
            if (getColumnIndices(fileName, columns, collect, sortedColumnNumbers, errorList)) {
                for (Integer colNo : sortedColumnNumbers) {
                    TreeSet<String> map = new TreeSet<>();
                    for (String[] line : collect) {
                        if (ValidationUtil.isNullAndEmpty(line[colNo])) {
                            errorList.add("In file " + fileName + " column " + colNo + " is required. It should not be null or empty.");
                        } else {
                            if (map.contains(line[colNo])) {
                                errorList.add("In file " + fileName + " column " + colNo + " value " + line[colNo] + " is duplicated. It should be unique.");
                            } else
                                map.add(line[colNo]);
                        }
                    }
                }
            }
        }
    }

    /**
     * Validates required columns are present and are not null or empty.
     *
     * @param fileName  fileName
     * @param columns   Columns
     * @param errorList error list
     */
    private void validateColumns(String fileName, List<String> columns, List<String> errorList) {
        if (columns.size() == 0) return;
        List<String[]> collect = readFileIntoMemory(fileName);
        if (collect != null) {
            TreeSet<Integer> sortedColumnNumbers = new TreeSet<>();
            if (getColumnIndices(fileName, columns, collect, sortedColumnNumbers, errorList)) {
                for (String[] line : collect) {
                    for (Integer colNo : sortedColumnNumbers) {
                        if (ValidationUtil.isNullAndEmpty(line[colNo])) {
                            errorList.add("In file " + fileName + " column " + colNo + " is required. It should not be null or empty.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the column numbers.
     *
     * @param fileName            File Name
     * @param columns             Column Names
     * @param collect             inMemoryFile
     * @param sortedColumnNumbers sorted column numbers
     * @param errorList           error list
     * @return status
     */
    private boolean getColumnIndices(String fileName, List<String> columns, List<String[]> collect, TreeSet<Integer> sortedColumnNumbers, List<String> errorList) {
        List<String> fileHeaders = Arrays.asList(collect.remove(0));

        fileHeaders = fileHeaders.stream().map(String::trim).collect(Collectors.toList());
        for (String columnName : columns) {
            if (fileHeaders.contains(columnName)) {
                sortedColumnNumbers.add(fileHeaders.indexOf(columnName));
            } else {
                errorList.add("Could not find required column : " + columnName + " in input file " + fileName);
            }
        }
        if (sortedColumnNumbers.size() == 0) return false;
        for (String[] line : collect) {
            if (line.length <= sortedColumnNumbers.last()) {
                errorList.add(fileName + " is corrupted. Please check file for irregular size columns.");
                return false;
            }
        }
        return true;
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
     * @param errorList error list
     */
    void validateColumnBetweenFiles(String filePath, ConditionUnit condition, List<String> errorList) {
        if (condition.typeName != null) {
            if (ValidationUtil.getAllowedExtensions().contains(condition.typeName.substring(condition.typeName.indexOf('.') + 1))) {
                if (condition.fieldToCompare != null) {
                    String comparisonFileName = condition.typeName;
                    String columnName = condition.columnName;
                    String fieldToCompare = condition.fieldToCompare;
                    List<String> filesList = new ArrayList<>();
                    if (getFilesWithExtension(new File(filePath).getParent(), comparisonFileName, filesList, errorList)) {
                        if (filesList.size() != 1) {
                            errorList.add("There should be only one file in the folder " + new File(filePath).getParent());
                            return;
                        }
                        String comparisonFilePath = new File(filePath).getParent() + "/" + comparisonFileName;
                        List<String> fileColumnElements = getFileColumn(filePath, columnName, errorList);
                        if (fileColumnElements.size() == 0) return;
                        else {
                            fileColumnElements = fileColumnElements.stream().distinct().collect(Collectors.toList());
                            Collections.sort(fileColumnElements);
                        }
                        List<String> comparisonFileColumnElements = getFileColumn(comparisonFilePath, fieldToCompare, errorList);
                        if (comparisonFileColumnElements.size() == 0) {
                            errorList.add(fieldToCompare + " does not exist in file " + comparisonFilePath);
                            return;
                        } else {
                            comparisonFileColumnElements = comparisonFileColumnElements.stream().distinct().collect(Collectors.toList());
                            Collections.sort(comparisonFileColumnElements);
                        }
                        if (!fileColumnElements.equals(comparisonFileColumnElements)) {
                            errorList.add(fieldToCompare + "is not same in " + filePath + "\t" + comparisonFilePath);
                        }
                    }
                } else {
                    printMissingFieldError("File", "fieldToCompare", errorList);
                }
            } else {
                errorList.add(condition.typeName + " is not a valid file extension.");
            }
        } else {
            printMissingFieldError("File", "typeName", errorList);
        }
    }

    /**
     * Validation based on the existence of a file.
     * Do something if a file exists and something else if it doesn't exist.
     *
     * @param fileName       Name of the file
     * @param validationUnit Validation Unit
     * @param errorList      error list
     */
    void validateFileExistenceCheck(String fileName, ValidationUnit validationUnit, List<String> errorList) {
        for (ConditionUnit condition : validationUnit.getConditions()) {
            if (condition.fileExistenceCheck != null) {
                String existenceFile = condition.fileExistenceCheck;
                List<String> digestGermplasm = new ArrayList<>();
                boolean shouldFileExist = condition.fileExists.equalsIgnoreCase(ValidationConstants.YES);
                getFilesWithExtension(new File(fileName).getParent(), existenceFile, digestGermplasm, errorList);
                if (digestGermplasm.size() > 1) {
                    errorList.add("There should be maximum one file in the folder " + new File(fileName).getParent());
                    return;
                }
                if ((shouldFileExist && digestGermplasm.size() == 1) || (!shouldFileExist && digestGermplasm.size() == 0)) {
                    // Condition is satisfied
                    if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.DB)) {
                        //TODO: Implement the database call
                    } else if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE))
                        validateColumnBetweenFiles(fileName, condition, errorList);
                    else
                        errorList.add("Unrecognised type defined in condition" + condition.type);
                }
            }
        }
    }


    /**
     * Reads the particular column from the file.
     * If column does not exist it returns an empty set.
     *
     * @param filepath  filePath
     * @param column    column name
     * @param errorList error list
     * @return column values
     */
    private List<String> getFileColumn(String filepath, String column, List<String> errorList) {
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
                    errorList.add(column + " value does not exist in file. Please check the contents of file. " + filepath);
                }
            }
        }
        return fileColumnElements;
    }
}
