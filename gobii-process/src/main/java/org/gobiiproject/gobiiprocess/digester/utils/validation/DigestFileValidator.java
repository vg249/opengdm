package org.gobiiproject.gobiiprocess.digester.utils.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.DigesterFileExtensions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.gobiiproject.gobiiprocess.digester.DigesterFileExtensions.*;
import static org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationWebServicesUtil.loginIntoServer;

public class DigestFileValidator {

    private String rootDir, rulesFile;

    public DigestFileValidator(String rootDir) {
        this.rootDir = rootDir;
        // TODO
        //Delete below line it is used for testing at the moment
        this.rulesFile = getClass().getClassLoader().getResource("validationConfig.json").getPath();
    }

    DigestFileValidator(String rootDir, String validationFile) {
        this.rootDir = rootDir;
        this.rulesFile = validationFile;
    }


    public static void main(String[] args) throws IllegalAccessException {

        String rootDir = null, validationFile = null;

        Options o = new Options()
                .addOption("r", true, "Fully qualified path to digest directory")
                .addOption("v", true, "Validation rule file path");
        if (args.length != 4) {
            new HelpFormatter().printHelp("DigestFileValidator", o);
            System.exit(1);
        }

        try {
            CommandLine cli = new DefaultParser().parse(o, args);
            if (cli.hasOption("r")) rootDir = cli.getOptionValue("r");
            if (cli.hasOption("v")) validationFile = cli.getOptionValue("v").toLowerCase();
            if (rootDir == null || validationFile == null) {
                new HelpFormatter().printHelp("DigestFileValidator", o);
                System.exit(1);
            }
        } catch (org.apache.commons.cli.ParseException exp) {
            new HelpFormatter().printHelp("DigestFileValidator", o);
            System.exit(1);
        }

        ErrorLogger.logDebug("Entered Options are: " + rootDir + "," + validationFile, "");
        DigestFileValidator digestFileValidator = new DigestFileValidator(rootDir);
        //DigestFileValidator digestFileValidator = new DigestFileValidator(rootDir, validationFile);
        List<ValidationUnit> validations = digestFileValidator.readRules();
        if (validations.size() == 0) System.exit(1);
        for (ValidationUnit validation : validations)
            digestFileValidator.validate(validation);
    }

    /**
     * Reads rules JSON file stores in an object and returns it.
     *
     * @throws IllegalAccessException Illegal Access Exception
     */
    private List<ValidationUnit> readRules() throws IllegalAccessException {
        // Get allowed digest extensions
        Map<String, String> allowedExtensions = new HashMap<>();
        Field[] fields = DigesterFileExtensions.class.getDeclaredFields();
        for (Field field : fields) {
            allowedExtensions.put(String.valueOf(field.get(null)), field.getName());
        }

        List<ValidationUnit> validations;
        try {
            // Convert JSON string from file to Object
            ValidationRules validationRules = new ObjectMapper().readValue(new File(rulesFile), ValidationRules.class);
            validations = validationRules.getValidations();
        } catch (IOException e) {
            ErrorLogger.logError("Exception in reading rules file.", e);
            validations = new ArrayList<>();
            return validations;
        }
        if (!validateRules(allowedExtensions, validations)) {
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
     * @param allowedExtensions Allowed Digest Extensions
     * @param validations       List of validations read from JSON
     * @return whether it is a valid JSON or not.
     */
    private boolean validateRules(Map<String, String> allowedExtensions, List<ValidationUnit> validations) {
        List<String> values = allowedExtensions.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
        List<String> encounteredDigestExtensions = new ArrayList<>();
        for (ValidationUnit validation : validations) {
            if (!values.contains(FilenameUtils.getExtension(validation.getDigestFileName()))) {
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
        System.out.println("YELLO");
        String url = "http://192.168.121.3:8081/gobii-dev/";
        String username = "mcs397";
        String password = "q";
        String crop = null;
        loginIntoServer(url, username, password, crop);
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
    }
}

