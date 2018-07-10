package org.gobiiproject.gobiiprocess.digester.utils.validation;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.File;
import java.io.IOException;

import static org.gobiiproject.gobiiprocess.digester.DigesterFileExtensions.*;

public class DigestFileValidator {

    private String rootDir, digestFileExtension;

    public DigestFileValidator(String rootDir, String digestFileExtension) {
        this.rootDir = rootDir;
        this.digestFileExtension = digestFileExtension;
    }

    public static void main(String[] args)  {

        String rootDir = null, digestFileExtension = null;

        Options o = new Options()
                .addOption("r", true, "Fully qualified path to digest directory")
                .addOption("e", true, "Digest file extension");
        if (args.length != 4) {
            new HelpFormatter().printHelp("DigestFileValidator", o);
            System.exit(1);
        }

        try {
            CommandLine cli = new DefaultParser().parse(o, args);
            if (cli.hasOption("r")) rootDir = cli.getOptionValue("r");
            if (cli.hasOption("e")) digestFileExtension = cli.getOptionValue("e").toLowerCase();

            if (rootDir == null || digestFileExtension == null) {
                new HelpFormatter().printHelp("DigestFileValidator", o);
                System.exit(1);
            }
            DigestFileValidator digestFileValidator = new DigestFileValidator(rootDir, digestFileExtension);
            digestFileValidator.readValidationRules();
            digestFileValidator.validate();

        } catch (org.apache.commons.cli.ParseException exp) {
            new HelpFormatter().printHelp("DigestFileValidator", o);
            System.exit(1);
        }

        ErrorLogger.logDebug("Entered Options are: " + rootDir + "," + digestFileExtension, "");


    }

    private void readValidationRules() {
        ObjectMapper mapper = new ObjectMapper();

        try {

            // Convert JSON string from file to Object
            ValidationUnit validationRules = mapper.readValue(new File("E:\\staff.json"), ValidationUnit.class);
            System.out.println(validationRules);

            //Pretty print
            String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(validationRules);
            System.out.println(prettyStaff1);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

