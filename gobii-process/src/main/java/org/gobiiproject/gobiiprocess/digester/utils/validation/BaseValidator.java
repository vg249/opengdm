package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseValidator {
    abstract void validate(ValidationUnit conditions, String dir);

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
     * @param conditions conditions
     * @return column Names
     */
    void validateRequiredColumns(String fileName, List<ConditionUnit> conditions) {
        List<String> requiredFields = new ArrayList<>();
        for (ConditionUnit condition : conditions) {
            if (condition.required.equalsIgnoreCase("YES") && !(condition.unique != null && condition.unique.equalsIgnoreCase("YES"))) {
                requiredFields.add(condition.columnName);
            }
        }
        validateColumns(fileName, requiredFields);
    }

    /**
     * Parses the validation rules and gives the rules which are required and unique
     *
     * @param conditions conditions
     * @return column Names
     */
    void validateRequiredUniqueColumns(String fileName, List<ConditionUnit> conditions) {
        List<String> requiredUniqueColumns = new ArrayList<>();
        for (ConditionUnit condition : conditions) {
            if (condition.required.equalsIgnoreCase("YES") && (condition.unique != null && condition.unique.equalsIgnoreCase("YES"))) {
                requiredUniqueColumns.add(condition.columnName);
            }
        }
        validateColumns(fileName, requiredUniqueColumns);
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
            Set<Integer> sortedColumnNumbers = new TreeSet<>();
            for (String columnName : columns) {
                if (fileHeaders.contains(columnName)) {
                    sortedColumnNumbers.add(fileHeaders.indexOf(columnName));
                } else {
                    ErrorLogger.logError("Could not find required column : " + columnName + " in input file", fileName);
                }
            }
            for (String[] line : collect) {
                if (line.length < ((TreeSet<Integer>) sortedColumnNumbers).last())
                    ErrorLogger.logError(fileName, " is corrupted. PLease check file for irregular size columns.");
                for (Integer colNo : sortedColumnNumbers) {
                    if (isNullAndEmpty(line[colNo])) {
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

    boolean isNullAndEmpty(String value) {
        return value == null || value.trim().equalsIgnoreCase("");
    }
}
