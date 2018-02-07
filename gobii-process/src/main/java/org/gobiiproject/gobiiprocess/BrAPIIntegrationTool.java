package org.gobiiproject.gobiiprocess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.apache.commons.cli.*;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiclient.core.common.GenericClientContext;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiimodel.config.ServerBase;
import org.springframework.util.Assert;

import java.net.URL;

/**
 * Created by VCalaminos on 2/6/2018.
 */
public class BrAPIIntegrationTool {

    private static String INPUT_BDMS = "bdms";
    private static String INPUT_STS = "sts";
    private static String INPUT_GDS = "gds";
    private static String INPUT_BDMS_AUTH = "ba";
    private static String INPUT_STS_AUTH = "sa";
    private static String INPUT_GDS_AUTH = "ga";
    private static String INPUT_STUDY_NAME = "study";

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static JsonParser jsonParser = new JsonParser();
    private static ServerBase serverBase;
    private static String jsonPayload;

    private static void setOption(Options options,
                                  String argument,
                                  boolean requiresValue,
                                  String helpText,
                                  String shortName) throws Exception {

        if (options.getOption(argument) != null) {

            throw new Exception("Option is already defined: " + argument);

        }

        options.addOption(argument, requiresValue, helpText)
                .getOption(argument)
                .setArgName(shortName);


    }

    private static String getMessageForMissingOption(String optionName, Options options) {

        Option option = options.getOption(optionName);

        if (option.equals(null)) {

            return "Invalid argument: " + optionName;

        }

        return "Value is required: " + option.getArgName() + ", " + option.getDescription();

    }

    private static void checkIfNullOptionForRequiredArgs(CommandLine commandLine, String argumentName, Options options) {

        if (!commandLine.hasOption(argumentName)) {

            String message = getMessageForMissingOption(argumentName, options);

            System.out.println(message);
            System.exit(1);

        }

    }

    public static boolean didHttpMethodSucceed(HttpMethodResult httpMethodResult) {

        boolean returnVal = true;

        if (httpMethodResult.getResponseCode() != HttpStatus.SC_OK) {
            String message = "http method failed: "
                    + httpMethodResult.getUri().toString()
                    + "; failure mode: "
                    + Integer.toString(httpMethodResult.getResponseCode())
                    + " ("
                    + httpMethodResult.getReasonPhrase()
                    + ")";

            System.out.print(message);

            returnVal = false;
        }

        return returnVal;
    }

    public static void main(String[] args) throws Exception {


        // define commandline options

        Options options = new Options();
        setOption(options, INPUT_BDMS, true, "URL to a Breeding Database Management Systems API", "Breeding Database Management Systems API");
        setOption(options, INPUT_BDMS_AUTH, false, "Indicates whether authentication is required", "BDMS Authentication");
        setOption(options, INPUT_STS, false, "URL to a Sample Tracking System", "Sample Tracking System");
        setOption(options, INPUT_STS_AUTH, false, "Indicates whether authentication is required", "STS Authentication");
        setOption(options, INPUT_GDS, true, "URL to a Genomic Data System API", "Genomic Data System");
        setOption(options, INPUT_GDS_AUTH, false, "Indicates whether authentication is required", "GDS Authentication");
        setOption(options, INPUT_STUDY_NAME, true, "Name of the study in the Breeding Database System", "Study Name");

        // parse the commandline

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        checkIfNullOptionForRequiredArgs(commandLine, INPUT_BDMS, options);
        checkIfNullOptionForRequiredArgs(commandLine, INPUT_GDS, options);
        checkIfNullOptionForRequiredArgs(commandLine, INPUT_STUDY_NAME, options);

        String stsUrl = null;
        String bdmsAuth = "false";
        String stsAuth = "false";
        String gdsAuth = "false";

        if (commandLine.hasOption(INPUT_STS)) {
            stsUrl = commandLine.getOptionValue(INPUT_STS);


            if (commandLine.hasOption(INPUT_STS_AUTH)) {
                stsAuth = commandLine.getOptionValue(INPUT_STS_AUTH);
            }

        }

        if (commandLine.hasOption(INPUT_BDMS_AUTH)) {
            bdmsAuth = commandLine.getOptionValue(INPUT_BDMS_AUTH);
        }

        if (commandLine.hasOption(INPUT_GDS_AUTH)) {
            gdsAuth = commandLine.getOptionValue(INPUT_GDS_AUTH);
        }

        String bdmsUrl = commandLine.getOptionValue(INPUT_BDMS);
        String gdsUrl = commandLine.getOptionValue(INPUT_GDS);
        String studyName = commandLine.getOptionValue(INPUT_STUDY_NAME);

        System.out.print("Breeding Database Management System URL: " + bdmsUrl + "\n");
        System.out.print("Genomic Data System URL: " + gdsUrl + "\n");

        // BYPASS AUTHENTICATION FOR B4R, FOR NOW

        URL bdmsUrlObj = new URL(bdmsUrl);

        // BYPASS AUTHENTICATION FOR B4R, FOR NOW

        String b4rAccessToken = "Bearer 7b7e071584e14fc2fe1df41257df61f72ac9689d";

        serverBase = new ServerBase(bdmsUrlObj.getHost(),
                "json-context",
                bdmsUrlObj.getPort(),
                true);
        GenericClientContext bdmsGenericClientContext = new GenericClientContext(serverBase);

        // get study

        // build post body

        JsonArray studyArray = new JsonArray();
        studyArray.add(studyName);
        JsonObject studyJson = new JsonObject();
        studyJson.add("systemNames", studyArray);

        String postBody = gson.toJson(studyJson);

        RestUri restUriBdmsCalls = new RestUri(bdmsUrlObj.getPath(),
                "",
                "studies-search");


        restUriBdmsCalls.getHttpHeaders().put("Authorization", b4rAccessToken);

        HttpMethodResult httpMethodResultBdms = bdmsGenericClientContext
                .post(restUriBdmsCalls, postBody);

        Assert.isTrue(didHttpMethodSucceed(httpMethodResultBdms));

        jsonPayload = gson.toJson(httpMethodResultBdms.getJsonPayload());

        System.out.print("\n" + "Start of B4R \n");

        JsonObject studyObject = (JsonObject) jsonParser.parse(jsonPayload);


        // get studyDbId
        JsonObject resultStudyObj = studyObject.get("result").getAsJsonObject();
        JsonArray dataStudyObj = resultStudyObj.get("data").getAsJsonArray();
        Integer studyDbId = dataStudyObj.get(0).getAsJsonObject().get("studyDbId").getAsInt();

        // get germplasm IDs
        restUriBdmsCalls = new RestUri(bdmsUrlObj.getPath(),
                "",
                "studies")
                .addUriParam("studyDbId")
                .setParamValue("studyDbId", studyDbId.toString())
                .appendSegment("germplasm");


        restUriBdmsCalls.getHttpHeaders().put("Authorization", b4rAccessToken);

        HttpMethodResult httpMethodResultGermplasm = bdmsGenericClientContext
                .get(restUriBdmsCalls);

        Assert.isTrue(didHttpMethodSucceed(httpMethodResultGermplasm));

        jsonPayload = gson.toJson(httpMethodResultGermplasm.getJsonPayload());

        JsonObject germplasmObject = (JsonObject) jsonParser.parse(jsonPayload);

        JsonArray germplasmData = germplasmObject.get("result").getAsJsonObject().get("data").getAsJsonArray();

        // Get allele matrix from Genomic data system


        URL gdsUrlObj = new URL(gdsUrl);

        System.out.print(gdsUrlObj);

        serverBase = new ServerBase(gdsUrlObj.getHost(),
                "json-context",
                gdsUrlObj.getPort(),
                true);
        GenericClientContext genericClientContext = new GenericClientContext(serverBase);

        RestUri restUriGobiiBrapiCalls = new RestUri(gdsUrlObj.getPath(),
                "",
                "allelematrix-search"
        );

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGobiiBrapiCalls);

        jsonPayload = gson.toJson(httpMethodResult.getJsonPayload());

        System.out.print(jsonPayload);
        

    }



}
