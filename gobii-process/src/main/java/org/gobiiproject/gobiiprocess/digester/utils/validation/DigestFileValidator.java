package org.gobiiproject.gobiiprocess.digester.utils.validation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.gobiiproject.gobiiprocess.digester.DigesterFileExtensions.*;
import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationWebServicesUtil.loginIntoServer;

public class DigestFileValidator {

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
        //  this.url = "http://192.168.121.3:8081/gobii-dev/";
        //  this.username = "mcs397";
        //  this.password = "q";
    }

    boolean loginServer() {
        return loginIntoServer(url, username, password, null);
    }

    public static void main(String[] args) {
        String rootDir = null, validationFile = null, url = null, userName = null, password = null;

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
            if (cli.hasOption("r")) rootDir = cli.getOptionValue("r");
            if (cli.hasOption("v")) validationFile = cli.getOptionValue("v");
            if (cli.hasOption("h")) url = cli.getOptionValue("h");
            if (cli.hasOption("u")) userName = cli.getOptionValue("u");
            if (cli.hasOption("p")) password = cli.getOptionValue("p");

            if (ValidationUtil.isNullAndEmpty(rootDir) || ValidationUtil.isNullAndEmpty(url) || ValidationUtil.isNullAndEmpty(userName) || ValidationUtil.isNullAndEmpty(password)) {
                new HelpFormatter().printHelp("DigestFileValidator", o);
                System.exit(1);
            }
            if (validationFile == null) {
                validationFile = "validationConfig.json";
            }
            if (url.charAt(url.length() - 1) != '/') url = url + "/";
        } catch (org.apache.commons.cli.ParseException exp) {
            new HelpFormatter().printHelp("DigestFileValidator", o);
            System.exit(1);
        }

        ErrorLogger.logDebug("Entered Options are ", rootDir + " , " + validationFile + " , " + url + " , " + userName + " , " + password);
        DigestFileValidator digestFileValidator = new DigestFileValidator(rootDir, validationFile, url, userName, password);
        List<ValidationUnit> validations = digestFileValidator.readRules();
        if (validations.size() == 0) System.exit(1);
        if (digestFileValidator.loginServer()) {
            for (ValidationUnit validation : validations)
                digestFileValidator.validate(validation);
        } else {
            ErrorLogger.logError("Could not log into server with below details.", "\n URL:" + url + "\n Username:" + userName + "\n Password:" + password);
            System.exit(1);
        }
    }

    /**
     * Reads rules JSON file stores in an object and returns it.
     */
    private List<ValidationUnit> readRules() {
        // Get allowed digest extensions
        List<ValidationUnit> validations;
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
            ErrorLogger.logError("Exception in reading rules file.", e);
            validations = new ArrayList<>();
            return validations;
        }
        if (!validateRules(validations)) {
            validations = new ArrayList<>();
            return validations;
        }
        return validations;
    }


    /**
     * Validates rules defined in the JSON
     * Checks if digestFileName is valid or not.
     * Checks if column name and required fields are defined in all conditionUnits or not.
     *
     * @param validations List of validations read from JSON
     * @return whether it is a valid JSON or not.
     */
    private boolean validateRules(List<ValidationUnit> validations) {
        List<String> allowedExtensions = ValidationUtil.getAllowedExtensions();
        if (allowedExtensions.size() == 0) return false;
        List<String> encounteredDigestExtensions = new ArrayList<>();
        for (ValidationUnit validation : validations) {
            if (!allowedExtensions.contains(FilenameUtils.getExtension(validation.getDigestFileName()))) {
                ErrorLogger.logError("Entered digestFileName is not a valid", validation.getDigestFileName());
                return false;
            } else if (encounteredDigestExtensions.contains(validation.getDigestFileName())) {
                ErrorLogger.logError("DigestFileName", validation.getDigestFileName() + " is already defined.");
                return false;
            } else {
                encounteredDigestExtensions.add(validation.getDigestFileName());
            }
            List<ConditionUnit> conditions = validation.getConditions();
            for (ConditionUnit condition : conditions) {
                if (condition.columnName == null || condition.required == null) {
                    ErrorLogger.logError("DigestFileName :" + validation.getDigestFileName() + " conditions does not have all required fields.", "");
                    return false;
                }
            }

        }
        return true;
    }

    public void validate(ValidationUnit validation) {
        trimSpaces(validation);
        switch (FilenameUtils.getExtension(validation.getDigestFileName())) {
            case GERMPLASM_TABNAME:
                new GermplasmValidator().validate(validation, rootDir);
                break;
            case GERMPLASM_PROP_TABNAME:
                new GermplasmPropValidator().validate(validation, rootDir);
                break;
            case DNA_SAMPLE_TABNAME:
                new DnaSampleValidator().validate(validation, rootDir);
                break;
            case DNA_SAMPLE_PROP_TABNAME:
                new DnaSamplePropValidator().validate(validation, rootDir);
                break;
            case SAMPLE_TABNAME:
                new DnarunValidator().validate(validation, rootDir);
                break;
            case SAMPLE_PROP_TABNAME:
                new DnarunPropValidator().validate(validation, rootDir);
                break;
            case MARKER_TABNAME:
                new MarkerValidator().validate(validation, rootDir);
                break;
            case MARKER_PROP_TABNAME:
                new MarkerPropValidator().validate(validation, rootDir);
                break;
            case LINKAGE_GROUP_TABNAME:
                new LinkageGroupValidator().validate(validation, rootDir);
                break;
            case MARKER_LINKAGE_GROUP_TABNAME:
                new MarkerLinkageGroupValidator().validate(validation, rootDir);
                break;
            case DS_SAMPLE_TABNAME:
                new DatasetDnarunValidator().validate(validation, rootDir);
                break;
            case DS_MARKER_TABNAME:
                new DatasetMarkerValidator().validate(validation, rootDir);
                break;
            case VARIANT_CALL_TABNAME: // Validate has to include matrix validation
                break;
            default:
                ErrorLogger.logError("DigestFileValidator", "Given extension " + validation.getDigestFileName() + " is not valid.");
                System.exit(1);
        }
//        System.out.println("YELLO");
    }

    void trimSpaces(ValidationUnit validationUnit) {
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

