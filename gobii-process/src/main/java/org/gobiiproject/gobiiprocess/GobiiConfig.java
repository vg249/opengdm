package org.gobiiproject.gobiiprocess;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestPing;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.types.GobiiFileLocationType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Checks configurations of a Gobii instance for sanity.
 * Created by Phil on 6/24/2016.
 */
public class GobiiConfig {

    private static String NAME_COMMAND = "GobiiConfig";
    private static String TOMCAT_BASE_DIR = "wbase";
    private static String CONFIG_BASE_URL = "wurl";
    private static String CONFIG_MKDIRS = "wdirs";
    private static String COPY_WARS = "wcopy";
    private static String PROP_FILE_FQPN = "wfqpn";
    private static String PROP_FILE_PROPS_TO_XML = "wxml";

    private static String CONFIG_ADD_ITEM = "a";
    private static String CONFIG_REMOVE_CROP = "cR";
    private static String CONFIG_GLOBAL_DEFAULT_CROP = "gD";
    private static String CONFIG_GLOBAL_FILESYS_ROOT = "gR";


    private static String CONFIG_SVR_GLOBAL_EMAIL = "stE"; // does not require -c
    private static String CONFIG_SVR_GLOBAL_EMAIL_TYPE = "stT"; // does not require -c
    private static String CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE = "stH"; // does not require -c
    private static String CONFIG_SVR_CROP_WEB = "stW";
    private static String CONFIG_SVR_CROP_POSTGRES = "stP";
    private static String CONFIG_SVR_CROP_MONET = "stM";

    private static String CONFIG_CROP_ID = "c";

    private static String CONFIG_SVR_OPTIONS_HOST = "soH";
    private static String CONFIG_SVR_OPTIONS_PORT = "soN";
    private static String CONFIG_SVR_OPTIONS_CONTEXT_ROOT = "soR";
    private static String CONFIG_SVR_OPTIONS_USER_NAME = "soU";
    private static String CONFIG_SVR_OPTIONS_PASSWORD = "soP";


    private static String WAR_FILES_DIR = "wars/";

    private static void printSeparator() {
        System.out.println("\n\n*******************************************************************");
    }

    private static void printField(String fieldName, String value) {
        System.out.println("******\t" + fieldName + ": " + value);
    }

    private static void setOption(Options options,
                                  String argument,
                                  boolean requiresValue,
                                  String helpText,
                                  String shortName) throws Exception {

        if (options.getOption(argument) != null) {
            throw new Exception("Option is already defined: " + argument);
        }

        //There does not appear to be a way to set argument name with the variants on addOption()
        options
                .addOption(argument, requiresValue, helpText)
                .getOption(argument)
                .setArgName(shortName);
    }

    /**
     * Main method of the configuration checking utility.
     * Use jar -? to find arguments
     * <p>
     * Examples:
     * Set file system root:  -a -wfqpn "c:\gobii-config-test\testconfig.xml" -gR "/mnt/lustre"
     * Set email options:  -a -wfqpn "c:\gobii-config-test\testconfig.xml" -stE -soH "foohost" -soN 25 -soU "foo userr" -soP "foo password"  -stT "fooetype" -stH "foohashtype"
     * Set crop web options:  -a -wfqpn "c:\gobii-config-test\testconfig.xml" -c "barcrop" -stW -soH "foohost" -soN 8080 -soU "foo userr" -soP "foo password" -soR "foo-web"
     * Set crop postgres options:  -a -wfqpn "c:\gobii-config-test\testconfig.xml" -c "barcrop" -stP -soH "foohost" -soN 5433 -soU "foo userr" -soP "foo password" -soR "foodb"
     *
     * @param args
     */
    public static void main(String[] args) {

        int exitCode = -1;

        // active?
        // file locations?

        try {

            // define commandline options
            Options options = new Options();
            setOption(options, TOMCAT_BASE_DIR, true, "Tomcat base directory (e.g., /usr/local/tomcat7)", "tomcat base");
            setOption(options, CONFIG_BASE_URL, true, "url of GOBII web server for configuration verification", "base url");
            setOption(options, CONFIG_MKDIRS, false, "make gobii directories from root in the specified properties file (requires " + PROP_FILE_FQPN + ")", "make directories");
            setOption(options, COPY_WARS, false, "create war files for active crops from the specified war file (requires " + PROP_FILE_FQPN + ")", "copy wars");
            setOption(options, PROP_FILE_FQPN, true, "fqpn of gobii configuration file", "config fqpn");
            setOption(options, PROP_FILE_PROPS_TO_XML, false, "Convert existing gobii-properties file to xml (requires " + PROP_FILE_FQPN + ")", "convert to xml");
            setOption(options, CONFIG_ADD_ITEM, false, "Adds or updates the configuration value specified by one of the standalone parameters ("
                    + CONFIG_GLOBAL_FILESYS_ROOT + ", " + CONFIG_GLOBAL_DEFAULT_CROP + ") or parameters that require server option parameters ("
                    + CONFIG_SVR_GLOBAL_EMAIL + ", " + CONFIG_CROP_ID + ")", "add config item");

            setOption(options, CONFIG_REMOVE_CROP, true, "Removes the specified crop and related server specifications", "crop ID");

            setOption(options, CONFIG_GLOBAL_DEFAULT_CROP, true, "Default crop (global)", "crop id");

            setOption(options, CONFIG_GLOBAL_FILESYS_ROOT, true, "Absolute path to the gobii file system root (global)", "gobii root fqpn");

            setOption(options, CONFIG_CROP_ID, true, "Identifier of crop to add or modify; must be accompanied by a server specifier and its options", "crop ID");


            setOption(options, CONFIG_SVR_GLOBAL_EMAIL, false, "Server type: Email (not crop specific)", "server: email"); // does not require -c
            setOption(options, CONFIG_SVR_GLOBAL_EMAIL_TYPE, true, "Email server type", "server type"); // does not require -c
            setOption(options, CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE, true, "Email server hash type", "hash type"); // does not require -c
            setOption(options, CONFIG_SVR_CROP_WEB, false, "Server type: Web (requires )", "server: web");
            setOption(options, CONFIG_SVR_CROP_POSTGRES, false, "Server type: postgres", "server: pgsql");
            setOption(options, CONFIG_SVR_CROP_MONET, false, "Server type: Monet DB", "server: monet");

            setOption(options, CONFIG_SVR_OPTIONS_HOST, true, "Server option: hostname", "hostname");
            setOption(options, CONFIG_SVR_OPTIONS_PORT, true, "Server option: port number", "port number");
            setOption(options, CONFIG_SVR_OPTIONS_CONTEXT_ROOT, true, "Server option: context root ("
                    + CONFIG_SVR_CROP_WEB
                    + ") or database name ("
                    + CONFIG_SVR_CROP_POSTGRES + " and " + "("
                    + CONFIG_SVR_CROP_MONET
                    + ")", "context path");
            setOption(options, CONFIG_SVR_OPTIONS_USER_NAME, true, "Server option: Username", "user name");
            setOption(options, CONFIG_SVR_OPTIONS_PASSWORD, true, "Server option: Password", "password");


            // parse our commandline
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            HelpFormatter helpFormatter = new HelpFormatter();

            if (commandLine.hasOption(TOMCAT_BASE_DIR)) {

                String tomcatBaseDirectory = commandLine.getOptionValue(TOMCAT_BASE_DIR);
                File tomcatDirectory = new File(tomcatBaseDirectory);

                if (tomcatDirectory.exists()) {

                    String configFileServerFqpn = tomcatBaseDirectory + "/conf/server.xml";

                    File configFileServer = new File(configFileServerFqpn);
                    if (configFileServer.exists()) {

                        GobiiConfig.printSeparator();
                        GobiiConfig.printField("Configuration Mode", "From tomcat server configuration");
                        GobiiConfig.printField("Tomcat file found", configFileServerFqpn);


                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document documentServer = documentBuilder.parse(new FileInputStream(configFileServer));


                        XPath xPathPropsLoc = XPathFactory.newInstance().newXPath();
                        NodeList nodesServer = (NodeList) xPathPropsLoc.evaluate("//Server/GlobalNamingResources/Environment[@name='gobiipropsloc']",
                                documentServer.getDocumentElement(), XPathConstants.NODESET);

                        Element locationElement = (Element) nodesServer.item(0);

                        if (null != locationElement) {

                            GobiiConfig.printField("Server configuration", "Proper node found");

                            String propertiesFileFqpn = locationElement.getAttribute("value");
                            File propertiesFile = new File(propertiesFileFqpn);
                            if (propertiesFile.exists()) {

                                GobiiConfig.printField("Local properties file", propertiesFileFqpn);

                                ConfigSettings configSettings = new ConfigSettings(propertiesFileFqpn);

                                String defaultCropType = configSettings.getDefaultGobiiCropType();
                                configSettings.setCurrentGobiiCropType(defaultCropType);

                                String configServerUrl = "http://"
                                        + configSettings.getCurrentCropConfig().getServiceDomain()
                                        + ":"
                                        + configSettings.getCurrentCropConfig().getServicePort().toString()
                                        + "/"
                                        + configSettings.getCurrentCropConfig().getServiceAppRoot();

                                String configFileContextFqpn = tomcatBaseDirectory + "/conf/context.xml";
                                File configFileContext = new File(configFileContextFqpn);
                                if (configFileContext.exists()) {

                                    GobiiConfig.printField("Tomcat file found", configFileContextFqpn);


                                    Document documentContext = documentBuilder.parse(new FileInputStream(configFileContext));
                                    XPath xPath = XPathFactory.newInstance().newXPath();
                                    NodeList nodesContext = (NodeList) xPath.evaluate("//Context/ResourceLink[@name='gobiipropsloc']",
                                            documentContext.getDocumentElement(), XPathConstants.NODESET);

                                    Element locationElementForLink = (Element) nodesContext.item(0);

                                    if (null != locationElementForLink) {
                                        GobiiConfig.printField("Context configuration", "Proper node found");
                                    } else {
                                        System.err.print("The configuration in server.xml does not define ResourceLink to the properties file: " + configFileServerFqpn);
                                    }

                                    ClientContext clientContext = configClientContext(configServerUrl);


                                    if (GobiiConfig.showServerInfo(clientContext)) {
                                        exitCode = 0;
                                    }


                                } else {
                                    System.err.println("Cannot find config file: : " + configFileContextFqpn);

                                }

                            } else {
                                System.err.println("The property file specified in "
                                        + configFileServerFqpn
                                        + " does not exist: "
                                        + propertiesFileFqpn);
                            }

                        } else {
                            System.err.println("The configuration does not define the properties file location: " + configFileServerFqpn);
                        }

                    } else {
                        System.err.println("Cannot find config file: : " + configFileServerFqpn);
                    }

                } else {
                    System.err.println("Specified tomcat base directory does not exist: " + tomcatBaseDirectory);
                }

            } else if (commandLine.hasOption(CONFIG_BASE_URL)) {

                String configUrl = commandLine.getOptionValue(CONFIG_BASE_URL);

                GobiiConfig.printSeparator();
                GobiiConfig.printField("Configuration Mode", "From url");

                ClientContext clientContext = configClientContext(configUrl);

                if (GobiiConfig.showServerInfo(clientContext)) {
                    exitCode = 0;
                }


            } else if (commandLine.hasOption(CONFIG_MKDIRS)
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);


                if (GobiiConfig.makeGobiiDirectories(propFileFqpn)) {
                    exitCode = 0;
                }


            } else if (commandLine.hasOption(COPY_WARS)
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);
                String warFileFqpn = commandLine.getOptionValue(COPY_WARS);


                if (GobiiConfig.copyWars(propFileFqpn, warFileFqpn)) {
                    exitCode = 0;
                }

            } else if (commandLine.hasOption(PROP_FILE_PROPS_TO_XML)
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);

                File propsFile = new File(propFileFqpn);
                if (propsFile.exists() && !propsFile.isDirectory()) {

                    exitCode = 0;

                } else {
                    System.err.println("Cannot find config file: : " + propFileFqpn);
                }

                ConfigSettings configSettings = new ConfigSettings(propFileFqpn);

            } else if (commandLine.hasOption(CONFIG_ADD_ITEM)) {


                String propFileFqpn = null;
                if (commandLine.hasOption(PROP_FILE_FQPN)
                        && (null != (propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN)))) {


                    if (setGobiiConfiguration(propFileFqpn, options, commandLine)) {
                        exitCode = 0;
                    }

                } else {
                    System.err.println("Value is required: " + options.getOption(PROP_FILE_FQPN).getDescription());
                }

            } else {
                helpFormatter.printHelp(NAME_COMMAND, options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.exit(exitCode);

    }// main()


    private static void writeConfigSettingsMessage(Options options,
                                                   String configFileFqpn,
                                                   List<String> configArgs,
                                                   List<String> configVals) throws Exception {

        if (configArgs.size() != configArgs.size()) {
            throw new Exception("The size of the options and values arrays do not match");
        }

        System.out.println("These options were written to the the config file " + configFileFqpn);
        for (int idx = 0; idx < configArgs.size(); idx++) {
            String currentArg = configArgs.get(idx);
            String currentOption = options.getOption(currentArg).getArgName();
            String curentDescription = options.getOption(currentArg).getDescription();
            String currentValue = configVals.get(idx);
            System.out.println("-" + currentArg + " <" + currentOption + ">:\t\t" + currentValue + " (" + curentDescription + ")");
        }
    }

    private static boolean setGobiiConfiguration(String propFileFqpn, Options options, CommandLine commandLine) {

        boolean returnVal = true;

        try {


            ConfigSettings configSettings;
            File file = new File(propFileFqpn);
            if (file.exists()) {

                configSettings = ConfigSettings.read(propFileFqpn);
            } else {
                configSettings = ConfigSettings.makeNew(propFileFqpn);
            }


            if (commandLine.hasOption(CONFIG_GLOBAL_DEFAULT_CROP)) {

                String defaultCrop = commandLine.getOptionValue(CONFIG_GLOBAL_DEFAULT_CROP);

                if (configSettings.getCropConfig(defaultCrop) == null) {
                    configSettings.setCrop(defaultCrop, true, null, null, null);
                }

                configSettings.setDefaultGobiiCropType(defaultCrop);

                configSettings.commit();

                writeConfigSettingsMessage(options,
                        propFileFqpn,
                        Arrays.asList(CONFIG_GLOBAL_DEFAULT_CROP),
                        Arrays.asList(defaultCrop));


            } else if (commandLine.hasOption(CONFIG_GLOBAL_FILESYS_ROOT)) {

                String fileSysRoot = commandLine.getOptionValue(CONFIG_GLOBAL_FILESYS_ROOT);

                configSettings.setFileSystemRoot(fileSysRoot);
                configSettings.commit();

                writeConfigSettingsMessage(options,
                        propFileFqpn,
                        Arrays.asList(CONFIG_GLOBAL_FILESYS_ROOT),
                        Arrays.asList(fileSysRoot));


            } else if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL) ||
                    commandLine.hasOption(CONFIG_CROP_ID)) {

                List<String> argsSet = new ArrayList<>();
                List<String> valsSet = new ArrayList<>();


                String svrHost = null;
                String svrUserName = null;
                String svrPassword = null;
                Integer svrPort = null;

                if (commandLine.hasOption(CONFIG_SVR_OPTIONS_HOST)) {
                    svrHost = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_HOST);
                    argsSet.add(CONFIG_SVR_OPTIONS_HOST);
                    valsSet.add(svrHost);
                }

                if (commandLine.hasOption(CONFIG_SVR_OPTIONS_USER_NAME)) {
                    svrUserName = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_USER_NAME);
                    argsSet.add(CONFIG_SVR_OPTIONS_USER_NAME);
                    valsSet.add(svrUserName);
                }

                if (commandLine.hasOption(CONFIG_SVR_OPTIONS_PASSWORD)) {
                    svrPassword = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_PASSWORD);
                    argsSet.add(CONFIG_SVR_OPTIONS_PASSWORD);
                    valsSet.add(svrPassword);
                }

                if (commandLine.hasOption(CONFIG_SVR_OPTIONS_PORT)) {

                    String svrPortStr = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_PORT);

                    if (NumberUtils.isNumber(svrPortStr)) {
                        svrPort = Integer.parseInt(svrPortStr);
                    } else {
                        throw new Exception("Option for port value (" + CONFIG_SVR_OPTIONS_PORT + ") is not an integer: " + svrPortStr);
                    }

                    argsSet.add(CONFIG_SVR_OPTIONS_PORT);
                    valsSet.add(svrPortStr);
                }


                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL)) {

                    configSettings.setEmailSvrDomain(svrHost);
                    configSettings.setEmailSvrUser(svrUserName);
                    configSettings.setEmailSvrPassword(svrPassword);
                    configSettings.setEmailSvrPort(svrPort);

                    if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL_TYPE)) {
                        String emailSvrType = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_EMAIL_TYPE);
                        configSettings.setEmailSvrType(emailSvrType);
                        argsSet.add(CONFIG_SVR_GLOBAL_EMAIL_TYPE);
                        valsSet.add(emailSvrType);
                    }

                    if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE)) {
                        String hashhType = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE);
                        configSettings.setEmailSvrHashType(hashhType);
                        argsSet.add(CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE);
                        valsSet.add(hashhType);
                    }


                } else if (commandLine.hasOption(CONFIG_CROP_ID)) {

                    String cropId = commandLine.getOptionValue(CONFIG_CROP_ID);

                    CropConfig cropConfig = configSettings.getCropConfig(cropId);
                    if (cropConfig == null) {
                        configSettings.setCrop(cropId, true, null, null, null);
                        cropConfig = configSettings.getCropConfig(cropId);
                    }

                    String contextRoot = null;
                    if (commandLine.hasOption(CONFIG_SVR_OPTIONS_CONTEXT_ROOT)) {
                        contextRoot = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_CONTEXT_ROOT);
                        argsSet.add(CONFIG_SVR_OPTIONS_CONTEXT_ROOT);
                        valsSet.add(contextRoot);
                    }

                    if (commandLine.hasOption(CONFIG_SVR_CROP_WEB)) {

                        cropConfig.setServiceDomain(svrHost);
                        cropConfig.setServicePort(svrPort);
                        cropConfig.setServiceAppRoot(contextRoot);

                    } else if (commandLine.hasOption(CONFIG_SVR_CROP_POSTGRES) ||
                            (commandLine.hasOption(CONFIG_SVR_CROP_MONET))) {

                        GobiiDbType gobiiDbType = GobiiDbType.UNKNOWN ;
                        if (commandLine.hasOption(CONFIG_SVR_CROP_POSTGRES)) {
                            gobiiDbType = GobiiDbType.POSTGRESQL;
                        } else if (commandLine.hasOption(CONFIG_SVR_CROP_MONET)) {
                            gobiiDbType = GobiiDbType.MONETDB;
                        }

                        cropConfig.setCropDbConfig(gobiiDbType,
                                svrHost,
                                contextRoot,
                                svrPort,
                                svrUserName,
                                svrPassword);

                    } else {

                    }


                }

                configSettings.commit();

                writeConfigSettingsMessage(options,
                        propFileFqpn,
                        argsSet,
                        valsSet);


            } else {

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnVal;

    }

    private static ClientContext configClientContext(String configServerUrl) throws Exception {
        System.out.println();
        System.out.println();
        GobiiConfig.printSeparator();
        GobiiConfig.printField("Config request server", configServerUrl);

        System.out.println();
        System.out.println();
        GobiiConfig.printSeparator();

        return ClientContext.getInstance(configServerUrl, true);

    }

    private static boolean showServerInfo(ClientContext clientContext) throws Exception {

        boolean returnVal = true;

        // The logging framework emits debugging messages before it knows not to emit them.
        // Until we solve this problem, we we'll visually set those messages aside
        List<String> gobiiCropTypes = clientContext.getInstance(null, false).getCropTypeTypes();
        GobiiConfig.printSeparator();

        GobiiConfig.printField("Default crop", ClientContext.getInstance(null, false).getDefaultCropType().toString());

        for (String currentCropType : gobiiCropTypes) {

            ClientContext.getInstance(null, false).setCurrentClientCrop(currentCropType);

            GobiiConfig.printSeparator();
            GobiiConfig.printField("Crop Type", currentCropType.toString());
            GobiiConfig.printField("Host", ClientContext.getInstance(null, false).getCurrentCropDomain());
            GobiiConfig.printField("Port", ClientContext.getInstance(null, false).getCurrentCropPort().toString());
            GobiiConfig.printField("Context root", ClientContext.getInstance(null, false).getCurrentCropContextRoot());

            GobiiConfig.printField("Loader instructions directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.LOADERINSTRUCTION_FILES));
            GobiiConfig.printField("User file upload directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.RAWUSER_FILES));
            GobiiConfig.printField("Digester output directory ", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.INTERMEDIATE_FILES));
            GobiiConfig.printField("Extractor instructions directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.EXTRACTORINSTRUCTION_FILES));

            //if(!LineUtils.isNullOrEmpty())

            SystemUsers systemUsers = new SystemUsers();
            SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());

            if (ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword())) {

                PingDTO pingDTORequest = new PingDTO();

                DtoRequestPing dtoRequestPing = new DtoRequestPing();
                PingDTO pingDTOResponse = dtoRequestPing.process(pingDTORequest);

                Integer responseNum = 1;
                if (pingDTOResponse.getStatus().isSucceeded()) {
                    for (String currentResponse : pingDTOResponse.getPingResponses()) {
                        GobiiConfig.printField("Ping response " + (responseNum++).toString(), currentResponse);
                    }
                } else {
                    for (HeaderStatusMessage currentHeader : pingDTOResponse.getStatus().getStatusMessages()) {
                        GobiiConfig.printField("Service error " + (responseNum++).toString(), currentHeader.getMessage());
                        returnVal = false;
                    }
                }
            } else {
                System.err.println("Authentication to server for crop failed: " + currentCropType.toString());
                returnVal = false;
            }
        }

        return returnVal;
    }

    private static boolean makeGobiiDirectories(String propFileFqpn) {

        boolean returnVal = true;

        try {

            ConfigSettings configSettings = new ConfigSettings(propFileFqpn);
            String gobiiRoot = configSettings.getFileSystemRoot();
            for (String currentCrop : configSettings.getActiveCropTypes()) {

                printSeparator();
                printField("Checking directories for crop", currentCrop.getGobiiCropType().toString());


                for (GobiiFileLocationType currentRelativeDirectory :GobiiFileLocationType.values()) {

                    String directoryToMake = configSettings.getProcessingPath(currentCrop,currentRelativeDirectory);


                    File currentFile = new File(directoryToMake);
                    if (!currentFile.exists()) {

                        printField("Creating new directory", directoryToMake);
                        if (!currentFile.mkdirs()) {
                            System.err.println("Unable to create directory " + directoryToMake);
                            returnVal = false;
                        }

                    } else {
                        printField("Checking permissions on existing directory", directoryToMake);

                    }

                    if (!currentFile.canRead() && !currentFile.setReadable(true, false)) {
                        System.err.println("Unable to set read permissions on directory " + directoryToMake);
                        returnVal = false;
                    }

                    if (!currentFile.canWrite() && !currentFile.setWritable(true, false)) {
                        System.err.println("Unable to set write permissions on directory " + directoryToMake);
                        returnVal = false;

                    }
                } // iterate directories
            } // iterate crops


        } catch (Exception e) {

            e.printStackTrace();
            returnVal = false;

        }

        return returnVal;
    }

    private static boolean copyWars(String configFileFqpn, String warFileFqpn) {

        boolean returnVal = true;

        try {


            File sourceFile = new File(warFileFqpn);
            if (sourceFile.exists()) {

                ConfigSettings configSettings = new ConfigSettings(configFileFqpn);
                String warDestinationRoot = configSettings.getFileSystemRoot() + WAR_FILES_DIR;

                File destinationRootPath = new File(warDestinationRoot);
                if (!destinationRootPath.exists()) {
                    if (!destinationRootPath.mkdirs()) {
                        System.err.println("Unable to create war directory: " + warDestinationRoot);
                        returnVal = false;

                    }
                }

                if (!destinationRootPath.canRead() && !destinationRootPath.setReadable(true, false)) {

                    System.err.println("Unable to set read permissions on war directory: " + warDestinationRoot);
                    returnVal = false;

                }

                if (!destinationRootPath.canWrite() && !destinationRootPath.setWritable(true, false)) {

                    System.err.println("Unable to set write permissions on war directory: " + warDestinationRoot);
                    returnVal = false;

                }

                if (returnVal) {
                    for (CropConfig currentCrop : configSettings.getActiveCropConfigs()) {

                        String warDestinationFqpn = warDestinationRoot + "gobii-" + currentCrop
                                .getGobiiCropType()
                                .toString()
                                .toLowerCase()
                                + ".war";

                        File destinationFile = new File(warDestinationFqpn);

                        if (destinationFile.exists()) {
                            destinationFile.delete();
                        }

                        Files.copy(sourceFile.toPath(), destinationFile.toPath());
                        printField("Created war", warDestinationFqpn);

                    }
                }

            } else {
                System.err.println("Source war file does not exist: " + warFileFqpn);
                returnVal = false;

            }

        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;

        }

        return returnVal;

    }
}
