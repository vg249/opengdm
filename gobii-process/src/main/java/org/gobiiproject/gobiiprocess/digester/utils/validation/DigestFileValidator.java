package org.gobiiproject.gobiiprocess.digester.utils.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.ValidationError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.gobiiproject.gobiiprocess.digester.DigesterFileExtensions.*;
import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationUtil.addMessageToList;
import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationWebServicesUtil.loginIntoServer;

public class DigestFileValidator {

    static class InputParameters {
        String rootDir, validationFile, url, password, userName;
    }

    private String rootDir, rulesFile, url, password, username;
    //="q";;
    //="mcs397";;="http://192.168.121.3:8081/gobii-dev/";

    public DigestFileValidator(String rootDir, String validationFile, String url, String username, String password) {
        this.rootDir = rootDir;
        System.out.println("YE YE YE YE");
        this.url = url;
        this.rulesFile = validationFile;
        this.username = username;
        this.password = password;
        //  this.url = "http://192.168.56.101:8081/gobii-dev/";
        //  this.username = "mcs397";
        //  this.password = "q";
    }

    public static void main(String[] args) {
        DigestFileValidator digestFileValidator;
        InputParameters inputParameters = new InputParameters();
        readInputParameters(args, inputParameters);
        digestFileValidator = new DigestFileValidator(inputParameters.rootDir, inputParameters.validationFile, inputParameters.url, inputParameters.userName, inputParameters.password);

        String validationOutput = digestFileValidator.rootDir + "/" + "ValidationResult-" + new SimpleDateFormat("hhmmss").format(new Date()) + ".json";
        List<String> errorList = new ArrayList<>();
        List<ValidationUnit> validations = null;

        //Read validation Rules
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(validationOutput))) {
            validations = digestFileValidator.readRules(errorList);
            writer.newLine();
            if (errorList.size() > 0) {
                for (String error : errorList) {
                    writer.write(error);
                    writer.newLine();
                }
                writer.write(" validation failed.");
                writer.flush();
                System.exit(1);
            }
            if (validations.size() == 0) {
                writer.write("No validations defined.");
                writer.flush();
                System.exit(0);
            } else {
                writer.write("Validation started.");
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            ErrorLogger.logError("Unable to write to the file ", validationOutput);
            if (validations == null) validations = new ArrayList<>();
        } catch (MaximumErrorsValidationException e) {
            ErrorLogger.logError("Exception in reading validation rules.", "");
        }

        // Login into server
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(validationOutput))) {
            if (!loginIntoServer(digestFileValidator.url, digestFileValidator.username, digestFileValidator.password, null, errorList)) {
                writer.write("Could not log into server with below details. " + "\n URL:" + inputParameters.url + "\n Username:" + inputParameters.userName + "\n Password:" + inputParameters.password);
                writer.newLine();
                for (String error : errorList) {
                    writer.write(error);
                    writer.newLine();
                }
            } else {
                doValidations(digestFileValidator, validationOutput, validations);
            }
            writer.flush();
        } catch (Exception e) {
            ErrorLogger.logError("Unable to write to the file ", validationOutput);
        }

        // READ ERRORS
        //            ValidationError[] fileErrors = mapper.readValue(new File("D:\\writer\\validationError.json"), ValidationError[].class);

    }

    /**
     * Run Validations
     *
     * @param digestFileValidator digest validations
     * @param validationOutput    file
     * @param validations         validations
     */
    private static void doValidations(DigestFileValidator digestFileValidator, String validationOutput, List<ValidationUnit> validations) {
        try {
            FileWriter fileWriter = new FileWriter(new File(validationOutput));
            ObjectMapper mapper = new ObjectMapper();
            SequenceWriter seqWriter = mapper.writer().writeValuesAsArray(fileWriter);
            for (ValidationUnit validation : validations) {
                ValidationError validationError = new ValidationError();
                validationError.digestFileName = validation.getDigestFileName();
                validationError.failures = new ArrayList<>();
                List<Failure> failureList = new ArrayList<>();
                try {
                    digestFileValidator.validate(validation, failureList);
                } catch (MaximumErrorsValidationException e) {
                    //Don't do any thing. This implies that error list is full. Later code handles it.
                } catch (Exception e) {
                    Failure failure = new Failure();
                    failure.reason = FailureTypes.EXCEPTION;
                    failure.values.add(e.getMessage());
                    failureList.add(failure);
                }
                if (failureList.size() > 0) {
                    validationError.status = "FAILED";
                    validationError.failures.addAll(failureList);
                    seqWriter.write(validationError);
                } else {
                    validationError.status = "SUCCESS";
                    seqWriter.write(validationError);
                }
                seqWriter.flush();
            }
            seqWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the command line arguments
     *
     * @param args            arguments
     * @param inputParameters input parameters
     */
    private static void readInputParameters(String[] args, InputParameters inputParameters) {
        Options o = new Options()
                .addOption("r", true, "Fully qualified path to digest directory")
                .addOption("v", true, "Validation rule file path")
                .addOption("h", true, "Host server URL")
                .addOption("u", true, "User Name")
                .addOption("p", true, "Password");
        if (args.length != 8 && args.length != 10) {
            new HelpFormatter().printHelp("DigestFileValidator", o);
            System.exit(1);
        }
        try {
            CommandLine cli = new DefaultParser().parse(o, args);
            if (cli.hasOption("r")) inputParameters.rootDir = cli.getOptionValue("r");
            if (cli.hasOption("v")) inputParameters.validationFile = cli.getOptionValue("v");
            if (cli.hasOption("h")) inputParameters.url = cli.getOptionValue("h");
            if (cli.hasOption("u")) inputParameters.userName = cli.getOptionValue("u");
            if (cli.hasOption("p")) inputParameters.password = cli.getOptionValue("p");

            if (ValidationUtil.isNullAndEmpty(inputParameters.rootDir) || ValidationUtil.isNullAndEmpty(inputParameters.url) || ValidationUtil.isNullAndEmpty(inputParameters.userName) || ValidationUtil.isNullAndEmpty(inputParameters.password)) {
                new HelpFormatter().printHelp("DigestFileValidator", o);
                System.exit(1);
            }
            if (inputParameters.validationFile == null) {
                inputParameters.validationFile = "validationConfig.json";
            }
            if (inputParameters.url.charAt(inputParameters.url.length() - 1) != '/')
                inputParameters.url = inputParameters.url + "/";
        } catch (org.apache.commons.cli.ParseException exp) {
            new HelpFormatter().printHelp("DigestFileValidator", o);
            System.exit(1);
        }
        ErrorLogger.logDebug("Entered Options are ", inputParameters.rootDir + " , " + inputParameters.validationFile + " , " + inputParameters.url + " , " + inputParameters.userName + " , " + inputParameters.password);
    }

    /**
     * Reads rules JSON file stores in an object and returns it.
     *
     * @param errorList error list
     */
    private List<ValidationUnit> readRules(List<String> errorList) throws MaximumErrorsValidationException {
        List<ValidationUnit> validations = new ArrayList<>();
        try {
            // Convert JSON string from file to Object
            File rules = new File(rulesFile);
            ValidationRules validationRules;
            if (rules.isFile())
                validationRules = new ObjectMapper().readValue(rules, ValidationRules.class);
            else
                validationRules = new ObjectMapper()
                        .readValue(getClass().getClassLoader().getResourceAsStream(rulesFile), ValidationRules.class);
            validations = validationRules.getValidations();
        } catch (IOException e) {
            errorList.add("Exception in reading rules file." + e);
        }
        validateRules(validations, errorList);
        return validations;
    }

    /**
     * Validates rules defined in the JSON
     * Checks if digestFileName is valid or not.
     * Checks if column name and required fields are defined in all conditionUnits or not.
     *
     * @param validations List of validations read from JSON
     * @param errorList   error list
     */
    private void validateRules(List<ValidationUnit> validations, List<String> errorList) {

        List<String> encounteredDigestExtensions = new ArrayList<>();
        for (ValidationUnit validation : validations) {
            if (!allowedExtensions.contains(FilenameUtils.getExtension(validation.getDigestFileName()))) {
                errorList.add("Entered digestFileName is not a valid " + validation.getDigestFileName());
                return;
            } else if (encounteredDigestExtensions.contains(validation.getDigestFileName())) {
                errorList.add("DigestFileName" + validation.getDigestFileName() + " is already defined.");
                return;
            } else {
                encounteredDigestExtensions.add(validation.getDigestFileName());
            }
            List<ConditionUnit> conditions = validation.getConditions();
            for (ConditionUnit condition : conditions)
                if (condition.columnName == null || condition.required == null)
                    errorList.add("DigestFileName :" + validation.getDigestFileName() + " conditions does not have all required fields.");
        }
    }

    public void validate(ValidationUnit validation, List<Failure> failureList) throws MaximumErrorsValidationException {
        trimSpaces(validation);
        switch (FilenameUtils.getExtension(validation.getDigestFileName())) {
            case "germplasm":
                new GermplasmValidator().validate(validation, rootDir, failureList);
                break;
            case "germplasm_prop":
                new GermplasmPropValidator().validate(validation, rootDir, failureList);
                break;
            case "dnasample":
                new DnaSampleValidator().validate(validation, rootDir, failureList);
                break;
            case "dnasample_prop":
                new DnaSamplePropValidator().validate(validation, rootDir, failureList);
                break;
            case "dnarun":
                new DnarunValidator().validate(validation, rootDir, failureList);
                break;
            case "dnarun_prop":
                new DnarunPropValidator().validate(validation, rootDir, failureList);
                break;
            case "marker":
                new MarkerValidator().validate(validation, rootDir, failureList);
                break;
            case "marker_prop":
                new MarkerPropValidator().validate(validation, rootDir, failureList);
                break;
            case "linkage_group":
                new LinkageGroupValidator().validate(validation, rootDir, failureList);
                break;
            case "marker_linkage_group":
                new MarkerLinkageGroupValidator().validate(validation, rootDir, failureList);
                break;
            case "dataset_dnarun":
                new DatasetDnarunValidator().validate(validation, rootDir, failureList);
                break;
            case "dataset_marker":
                new DatasetMarkerValidator().validate(validation, rootDir, failureList);
                break;
            case "matrix": // Validate has to include matrix validation
                break;
            default:
                Failure failure = new Failure();
                failure.reason = FailureTypes.INVALID_FILE_EXTENSIONS;
                failure.values.add(" Given extension " + validation.getDigestFileName() + " is invalid.");
                addMessageToList(failure, failureList);
        }
//        System.out.println("YELLO");
    }

    private void trimSpaces(ValidationUnit validationUnit) {
        validationUnit.setDigestFileName(validationUnit.getDigestFileName().trim());
        for (ConditionUnit conditionUnit : validationUnit.getConditions()) {
            if (conditionUnit.columnName != null) conditionUnit.columnName = conditionUnit.columnName.trim();
            if (conditionUnit.required != null) conditionUnit.required = conditionUnit.required.trim();
            if (conditionUnit.nullAllowed != null) conditionUnit.nullAllowed = conditionUnit.nullAllowed.trim();
            if (conditionUnit.unique != null) conditionUnit.unique = conditionUnit.unique.trim();
            if (conditionUnit.fileExistenceCheck != null)
                conditionUnit.fileExistenceCheck = conditionUnit.fileExistenceCheck.trim();
            if (conditionUnit.fileExists != null) conditionUnit.fileExists = conditionUnit.fileExists.trim();
            if (conditionUnit.type != null) conditionUnit.type = conditionUnit.type.trim();
            if (conditionUnit.typeName != null) conditionUnit.typeName = conditionUnit.typeName.trim();
            if (conditionUnit.fieldToCompare != null)
                conditionUnit.fieldToCompare = conditionUnit.fieldToCompare.trim();
            if (conditionUnit.uniqueColumns != null)
                conditionUnit.uniqueColumns = conditionUnit.uniqueColumns.stream().map(String::trim).collect(Collectors.toList());
        }
    }

}

