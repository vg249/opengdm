package org.gobiiproject.gobiiprocess.digester.utils.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.DigesterFileExtensions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.gobiiproject.gobiiprocess.digester.DigesterFileExtensions.*;

public class DigestFileValidator {

    private String rootDir, rulesFile, digestFileExtension;

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
        if (!digestFileValidator.readRules()) return;
        digestFileValidator.validate();
    }

    /**
     * Reads rules JSON file stores in an object and returns it.
     *
     * @throws IllegalAccessException Illegal Access Exception
     */
    private boolean readRules() throws IllegalAccessException {

        // Get allowed digest extensions
        Map<String, String> allowedExtensions = new HashMap<>();
        List<ValidationUnit> validations;
        Field[] fields = DigesterFileExtensions.class.getDeclaredFields();
        for (Field field : fields) {
            allowedExtensions.put(field.getName(), String.valueOf(field.get(null)));
        }

        try {
            // Convert JSON string from file to Object
            ValidationRules validationRules = new ObjectMapper().readValue(new File(rulesFile), ValidationRules.class);
            validations = validationRules.getValidations();
        } catch (IOException e) {
            ErrorLogger.logError("Exception in reading rules file.", e);
            return false;
        }
        return validateRules(allowedExtensions, validations);
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
        List<String> values = allowedExtensions.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        for (ValidationUnit validation : validations) {
            if (!values.contains(validation.getDigestFileName())) {
                ErrorLogger.logError("Entered digestFileName is not a valid", validation.getDigestFileName());
                return false;
            } else {
                List<ConditionUnit> conditions = validation.getConditions();
                for (ConditionUnit condition : conditions) {
                    if (condition.columnName == null || condition.required == null) {
                        ErrorLogger.logError("DigestFileName :" + validation.getDigestFileName() + " conditions does not have all required fields.", "");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void validate() {

        switch (digestFileExtension) {
            case GERMPLASM_TABNAME:
                new GermplasmValidator().validate();
                break;
            case GERMPLASM_PROP_TABNAME:
                new GermplasmPropValidator().validate();
                break;
            case DNA_SAMPLE_TABNAME:
                new DnaSampleValidator().validate();
                break;
            case DNA_SAMPLE_PROP_TABNAME:
                new DnaSamplePropValidator().validate();
                break;
            case SAMPLE_TABNAME:
                new DnarunValidator().validate();
                break;
            case SAMPLE_PROP_TABNAME:
                new DnarunPropValidator().validate();
                break;
            case MARKER_TABNAME:
                new MarkerValidator().validate();
                break;
            case MARKER_PROP_TABNAME:
                new MarkerPropValidator().validate();
                break;
            case LINKAGE_GROUP_TABNAME:
                new LinkageGroupValidator().validate();
                break;
            case MARKER_LINKAGE_GROUP_TABNAME:
                new MarkerLinkageGroupValidator().validate();
                break;
            case DS_SAMPLE_TABNAME:
                new DatasetDnarunValidator().validate();
                break;
            case DS_MARKER_TABNAME:
                new DatasetMarkerValidator().validate();
                break;
            case VARIANT_CALL_TABNAME: // Validate has to include matrix validation
                break;
            default:
                ErrorLogger.logError("DigestFileValidator", "Given extension " + digestFileExtension + " is not valid.");
                System.exit(1);
        }
    }
}

