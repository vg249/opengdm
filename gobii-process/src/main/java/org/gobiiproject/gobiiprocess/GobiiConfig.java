package org.gobiiproject.gobiiprocess;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.KeycloakConfig;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.config.ServerConfigItem;
import org.gobiiproject.gobiimodel.types.GobiiAuthenticationType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
    private static String VALIDATE_CONFIGURATION = "validate";

    private static String CONFIG_ADD_ITEM = "a";
    private static String CONFIG_MARK_CROP_ACTIVE = "cA";
    private static String CONFIG_MARK_CROP_NOTACTIVE = "cD";
    private static String CONFIG_REMOVE_CROP = "cR";
    private static String CONFIG_GLOBAL_FILESYS_ROOT = "gR";
    private static String CONFIG_GLOBAL_FILESYS_HDF5 = "gH5";
    private static String CONFIG_GLOBAL_FILESYS_LOG = "gL";
    private static String CONFIG_GLOBAL_PROVIDES_BACKEND = "gB";

    private static String CONFIG_SVR_GLOBAL_EMAIL = "stE"; // does not require -c
    private static String CONFIG_SVR_GLOBAL_EMAIL_TYPE = "stT"; // does not require -c
    private static String CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE = "stH"; // does not require -c
    private static String CONFIG_SVR_GLOBAL_EMAIL_FROM = "stFr"; // does not require -c
    private static String CONFIG_SVR_GLOBAL_EMAIL_PASSWORD_TYPE = "stPt"; // does not require -c
    private static String CONFIG_SVR_GLOBAL_MAX_UPLOAD_MBYTES = "stMUM";
    private static String CONFIG_SVR_CROP_WEB = "stW";
    private static String CONFIG_SVR_CROP_POSTGRES = "stP";
    private static String CONFIG_SVR_CROP_COMPUTE = "stC";

    private static String CONFIG_SVR_GLOBAL_AUTH_TYPE = "auT";
    private static String CONFIG_SVR_GLOBAL_LDAP_UDN = "ldUDN";
    private static String CONFIG_SVR_GLOBAL_LDAP_URL = "ldURL";
    private static String CONFIG_SVR_GLOBAL_LDAP_BUSR = "ldBUSR";
    private static String CONFIG_SVR_GLOBAL_LDAP_BPAS = "ldBPAS";
    private static String CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER = "ldraUSR";
    private static String CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD = "ldraPAS";
    private static String CONFIG_SVR_GLOBAL_LDAP_AUTHENTICATE_BRAPI = "ldBR";

    private static String CONFIG_SVR_GLOBAL_LDAP_DECRYPT = "e";

    private static String CONFIG_TST_GLOBAL = "gt";
    private static String CONFIG_TST_GLOBAL_INTIAL_URL = "gtiu";
    private static String CONFIG_TST_GLOBAL_SSH_INTIAL_URL = "gtsu";
    private static String CONFIG_TST_GLOBAL_SSH_HOST = "gtsh";
    private static String CONFIG_TST_GLOBAL_SSH_PORT = "gtsp";
    private static String CONFIG_TST_GLOBAL_SSH_FLAG = "gtsf";
    private static String CONFIG_TST_GLOBAL_CONFIG_DIR_TEST = "gtcd";
    private static String CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM = "gtcs";
    private static String CONFIG_TST_GLOBAL_CONFIG_CROP_ID = "gtcr";
    private static String CONFIG_TST_GLOBAL_LDAP_USER = "gtldu";
    private static String CONFIG_TST_GLOBAL_LDAP_PASSWORD = "gtldp";
    private static String CONFIG_TST_GLOBAL_DOWNLOAD_DIR = "dldr";
    private static String CONFIG_TST_GLOBAL_ASYNCH_OP_TIMEOUT = "gtat";

    private static String CONFIG_CROP_ID = "c";

    private static String CONFIG_SVR_OPTIONS_HOST = "soH";
    private static String CONFIG_SVR_OPTIONS_PORT = "soN";
    private static String CONFIG_SVR_OPTIONS_CONTEXT_PATH = "soR";
    private static String CONFIG_SVR_OPTIONS_USER_NAME = "soU";
    private static String CONFIG_SVR_OPTIONS_PASSWORD = "soP";
    private static String CONFIG_SVR_OPTIONS_ACTIVE = "soA";

    private static String SVR_KDC = "ksvr";
    private static String SVO_OWNC = "ownc";
    private static String SVR_OWNC_ERR = "ocERR";
    private static String SVR_KDC_RESOURCE_START = "krscSTA";
    private static String SVR_KDC_RESOURCE_STATUS = "krscSTT";
    private static String SVR_KDC_RESOURCE_DOWNLOAD = "krscDLD";
    private static String SVR_KDC_RESOURCE_PURGE = "krscPRG";
    private static String SVR_KDC_STATUS_CHECK_INTERVAL_SECS = "kstTRS";
    private static String SVR_KDC_STATUS_CHECK_MAX_TIME_MINS = "kstTRM";

    private static String SVR_KC = "kcsvr";
    private static String KEYCLOAK_REALM = "kcRealm";
    private static String KEYCLOAK_RESOURCE = "kcResource";
    private static String KEYCLOAK_AUTH_SERVER_URL = "kcURL";
    private static String KEYCLOAK_ADMIN_USERNAME = "kcAdminuser";
    private static String KEYCLOAK_ADMIN_PASSWORD = "kcAdminpass";
    private static String KEYCLOAK_EXTRACTOR_UI_CLIENT = "kcExtractor";


    // we don't actually use the default crop any more, but for now we need to
    // leave the option in the commands so we don't break deploy scripts
    // if the value is not there at all
    private static String CONFIG_GLOBAL_DEFAULT_CROP = "gD";


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
     * Main method of the configuration utility. This utility has a number of functions, all of which can be
     * seen in the help listing -- run the utility without options to get a help listing.
     * This utility and this utility alone should be used for creating configuration files for deployment
     * purposes. The -validate option should be used on files before attempting a deployment: it will
     * point out missing and, to some extent, invalid values in the file (it does _not_ test whether server
     * configurations are valid). The gobii-client project contains a unit test that demonstrates many (but
     * not all) of the configuration options. Note that some of the commandline items are global to a deployment
     * whilst others are specific to a crop.
     * <p>
     * The following example sequence of commands illustrates how to create and validate a complete configuration file.
     * <p>
     * # Set root gobii directory (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -gR "/shared_data/gobii/"
     * <p>
     * # Configure email server (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -stE  -soH smtp.gmail.com -soN 465 -soU user@gmail.com -soP password -stT SMTP -stH na
     * <p>
     * # Configure web server for crop DEV
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  DEV  -stW  -soH localhost -soN 8282 -soR /gobii-dev/
     * <p>
     * # Configure web server for crop TEST
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  TEST  -stW  -soH localhost -soN 8383 -soR /gobii-test/
     * <p>
     * # Configure PostGRES server for crop DEV
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  DEV  -stP
     * -soH localhost -soN 5432 -soU appuser -soP password -soR gobii_dev
     * <p>
     * # Configure Compute Node server for crop DEV
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  DEV -stC  -soH localhost -soN 5000 -soU appuser -soP appuser -soR gobii_dev
     * <p>
     * # Configure PostGRES server for crop TEST
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  TEST  -stP  -soH localhost -soN 5432 -soU appuser -soP appuser -soR gobii_test
     * <p>
     * # Configure Compute Node server for crop DEV
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  TEST -stC  -soH localhost -soN 5000 -soU appuser -soP appuser -soR gobii_test
     * <p>
     * # Configure integration testing (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -gt  -gtcd /gobii-config-test -gtcq /gobii-config-test/gobii-web.xml -gtcr  DEV  -gtcs  "java -jar gobiiconfig.jar"  -gtiu http://localhost:8282/gobii-dev -gtsf false -gtsh localhost -gtsp 8080 -gtsu http://localhost:8080/gobii-dev
     * <p>
     * # Set default crop to DEV (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -gD   DEV
     * <p>
     * # Set log file directory (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -gL  /shared_data/gobii
     * <p>
     * # Mark crop TEST inactive
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  TEST  -cD
     * <p>
     * # Validate the file
     * java -jar gobiiconfig.jar -validate -wfqpn /gobii-config-test/gobii-web.xml
     *
     * @param args
     */
    public static void main(String[] args) {

        int exitCode = -1;

        // active?
        // remove?

        try {

            // define commandline options
            Options options = new Options();
            setOption(options, TOMCAT_BASE_DIR, true, "Tomcat base directory (e.g., /usr/local/tomcat7)", "tomcat base");
            setOption(options, CONFIG_BASE_URL, true, "url of GOBII web server for configuration verification", "base url");
            setOption(options, CONFIG_MKDIRS, false, "make gobii directories from root in the specified properties file (requires " + PROP_FILE_FQPN + ")", "make directories");
            setOption(options, COPY_WARS, false, "create war files for active crops from the specified war file (requires " + PROP_FILE_FQPN + ")", "copy wars");
            setOption(options, PROP_FILE_FQPN, true, "fqpn of gobii configuration file", "config fqpn");
            setOption(options, PROP_FILE_PROPS_TO_XML, false, "Convert existing gobii-properties file to xml (requires " + PROP_FILE_FQPN + ")", "convert to xml");
            setOption(options, CONFIG_ADD_ITEM, false, "Adds or updates the configuration value specified by one of the infrastructure parameters ("
                    + CONFIG_GLOBAL_FILESYS_ROOT + ") or parameters that require server option parameters ("
                    + CONFIG_SVR_GLOBAL_EMAIL + ", " + CONFIG_CROP_ID + ")", "add config item");

            setOption(options, CONFIG_REMOVE_CROP, true, "Removes the specified crop and related server specifications", "crop ID");
            setOption(options, CONFIG_MARK_CROP_ACTIVE, false, "Marks the specified crop active", "crop ID");
            setOption(options, CONFIG_MARK_CROP_NOTACTIVE, false, "Marks the specified crop inactive", "crop ID");

            setOption(options, CONFIG_GLOBAL_FILESYS_ROOT, true, "Absolute path to the gobii file system root (global)", "gobii root fqpn");
            setOption(options, CONFIG_GLOBAL_FILESYS_HDF5, true, "Absolute path to the gobii file system hdf5 binary directory (global)", "gobii hdf5 fqpn");
            setOption(options, CONFIG_GLOBAL_FILESYS_LOG, true, "Log file directory (global)", "log directory");
            setOption(options, CONFIG_GLOBAL_PROVIDES_BACKEND, true, "Specifies whether or not (true|false) MDE functionality is supported", "backend provided");

            setOption(options, CONFIG_CROP_ID, true, "Identifier of crop to add or modify; must be accompanied by a server specifier and its options", "crop ID");


            setOption(options, CONFIG_SVR_GLOBAL_EMAIL, false, "Server type: Email (not crop specific)", "server: email"); // does not require -c
            setOption(options, CONFIG_SVR_GLOBAL_EMAIL_TYPE, true, "Email server type", "server type"); // does not require -c
            setOption(options, CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE, true, "Email server hash type", "hash type"); // does not require -c
            setOption(options, CONFIG_SVR_GLOBAL_EMAIL_FROM, true, "Email server From Address", "server from"); // does not require -c
            setOption(options, CONFIG_SVR_GLOBAL_EMAIL_PASSWORD_TYPE, true, "Email server Password Type", "password type"); // does not require -c
            setOption(options, CONFIG_SVR_GLOBAL_MAX_UPLOAD_MBYTES, true, "Max file upload size (mbytes)", "max upload size"); // does not require -c
            setOption(options, CONFIG_SVR_CROP_WEB, false, "Server type: Web", "server: web");
            setOption(options, CONFIG_SVR_CROP_POSTGRES, false, "Server type: postgres", "server: pgsql");
            setOption(options, CONFIG_SVR_CROP_COMPUTE, false, "Server type: Compute Node", "server: compute");


            setOption(options, CONFIG_SVR_GLOBAL_AUTH_TYPE, true, "Authentication type (LDAP | LDAP_CONNECT_WITH_MANAGER | ACTIVE_DIRECTORY | ACTIVE_DIRECTORY_CONNECT_WITH_MANAGER | TEST)", "authentication type");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_UDN, true, "LDAP User DN pattern (e.g., uid={0},ou=people) ", "User DN Pattern");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_URL, true, "Fully-qualified LDAP URL", "LDAP URL");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_BUSR, true, "User for authenticated LDAP search", "LDAP user");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_BPAS, true, "Password for authenticated LDAP search", "LDAP password");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER, true, "LDAP user as which background processes will run", "Background LDAP user");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD, true, "LDAP password with which background processes authenticate", "Background LDAP password");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_AUTHENTICATE_BRAPI, true, "Whether or not BRAPI calls require authentication", "BRAPI Authentication");

            setOption(options, CONFIG_SVR_GLOBAL_LDAP_DECRYPT, true, "Whether or not to decrypt ALL userids and passwords (true | false)", "decryption flag");

            setOption(options, CONFIG_SVR_OPTIONS_HOST, true, "Server option: hostname", "hostname");
            setOption(options, CONFIG_SVR_OPTIONS_PORT, true, "Server option: port number", "port number");
            setOption(options, CONFIG_SVR_OPTIONS_CONTEXT_PATH, true, "Server option: context root ("
                    + CONFIG_SVR_CROP_WEB
                    + ") or database name ("
                    + CONFIG_SVR_CROP_POSTGRES + " and " + "("
                    + CONFIG_SVR_CROP_COMPUTE
                    + ")", "context path");
            setOption(options, CONFIG_SVR_OPTIONS_USER_NAME, true, "Server option: Username", "user name");
            setOption(options, CONFIG_SVR_OPTIONS_PASSWORD, true, "Server option: Password", "password");
            setOption(options, CONFIG_SVR_OPTIONS_ACTIVE, true, "Marker server inactive 'false'", "Server active");


            setOption(options, CONFIG_TST_GLOBAL, false, "Configure test options", "test options");
            setOption(options, CONFIG_TST_GLOBAL_INTIAL_URL, true, "test option: intial server URL", "test url");
            setOption(options, CONFIG_TST_GLOBAL_SSH_INTIAL_URL, true, "test option: intial server URL for ssh", "ssh url");
            setOption(options, CONFIG_TST_GLOBAL_SSH_HOST, true, "test option: host for ssh", "ssh host");
            setOption(options, CONFIG_TST_GLOBAL_SSH_PORT, true, "test option: port for ssh", "ssh port");
            setOption(options, CONFIG_TST_GLOBAL_SSH_FLAG, true, "test option: flag to test SSH", "ssh flag");
            setOption(options, CONFIG_TST_GLOBAL_CONFIG_DIR_TEST, true, "directory for creating test configuration files", "test directory");
            setOption(options, CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM, true, "configuration utility command to which command args are appended", "config cmd");
            setOption(options, CONFIG_TST_GLOBAL_CONFIG_CROP_ID, true, "Crop to use for automated testing", "crop id");
            setOption(options, CONFIG_TST_GLOBAL_LDAP_USER, true, "LDAP user as which unit tests authenticate (if Authentication requires LDAP)", "LDAP test user");
            setOption(options, CONFIG_TST_GLOBAL_LDAP_PASSWORD, true, "LDAP password with which LDAP unit test user authenticates (if Authentication requires LDAP)", "LDAP test user password");
            setOption(options, CONFIG_TST_GLOBAL_DOWNLOAD_DIR, true, "Destination directory for downloaded files)", "Download Directory");
            setOption(options, CONFIG_TST_GLOBAL_ASYNCH_OP_TIMEOUT, true, "Timeout value, in minutes, for asynch ops such as digest/extract", "Asynch Timeout Minutes");
            setOption(options, VALIDATE_CONFIGURATION, false, "Verify that the specified configuration has all the values necessary for the system to function (does not test that the servers exist); requires " + PROP_FILE_FQPN, "validate");

            setOption(options, SVR_KDC, false, "KDC server to add or modify; must be accompanied by a server options and KDC options", "KDC Server options");
            setOption(options, SVO_OWNC, false, "ownCloud configuration to add or modify a server options", "ownCloud");
            setOption(options, SVR_OWNC_ERR,true,"Error log context path for OwnCloud server.","owncloud error");
            setOption(options, SVR_KDC_RESOURCE_START, true, "KDC qcStart resource path", "qcStart resource");
            setOption(options, SVR_KDC_RESOURCE_STATUS, true, "KDC qcStatus resource path", "qcStatus resource");
            setOption(options, SVR_KDC_RESOURCE_DOWNLOAD, true, "KDC qcDownload resource path", "qcDownload resource");
            setOption(options, SVR_KDC_RESOURCE_PURGE, true, "KDC qcPurge resource path", "qcPurge resource");
            setOption(options, SVR_KDC_STATUS_CHECK_INTERVAL_SECS, true, "Status check interval for KDC jobs in seconds", "KDC status check interval");
            setOption(options, SVR_KDC_STATUS_CHECK_MAX_TIME_MINS, true, "Total time to wait for KDC job completion in minutes", "KDC job wait threshold");

            setOption(options, SVR_KC, false, "Keycloak server to add or modify; must be accompanied by a server options and KC options", "Keycloak Server options");
            setOption(options, KEYCLOAK_REALM, false, "Realm name within Keycloak", "Keycloak realm");
            setOption(options, KEYCLOAK_RESOURCE, false, "IDK what this is", "Keycloak resource?");
            setOption(options, KEYCLOAK_AUTH_SERVER_URL, false, "URL to the Keycloak auth server", "Keycloak auth server URL");
            setOption(options, KEYCLOAK_ADMIN_USERNAME, false, "Username for the Keycloak admin user", "Keycloak admin username");
            setOption(options, KEYCLOAK_ADMIN_PASSWORD, false, "Password for the Keycloak admin user", "Keycloak admin password");
            setOption(options, KEYCLOAK_EXTRACTOR_UI_CLIENT, false, "Extractor UI client for Keycloak connection", "Extractor UI client");

            // we don't actually use this value any more; it shold be removed when there is time to
            // change the deploy scripts
            setOption(options, CONFIG_GLOBAL_DEFAULT_CROP, true, "Default crop (global)", "crop id");

            // parse our commandline
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption(TOMCAT_BASE_DIR)) {

                String tomcatBaseDirectory = commandLine.getOptionValue(TOMCAT_BASE_DIR);
                File tomcatDirectory = new File(tomcatBaseDirectory);

                if (!tomcatDirectory.exists()) {
                    System.err.println("Specified tomcat base directory does not exist: " + tomcatBaseDirectory);
                    throw new Exception();
                }

                String configFileServerFqpn = tomcatBaseDirectory + "/conf/server.xml";

                File configFileServer = new File(configFileServerFqpn);

                if (!configFileServer.exists()) {
                    System.err.println("Cannot find config file: : " + configFileServerFqpn);
                    throw new Exception();
                }

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

                if (null == locationElement) {
                    System.err.println("The configuration does not define the properties file location: " + configFileServerFqpn);
                    throw new Exception();
                }

                GobiiConfig.printField("Server configuration", "Proper node found");

                String propertiesFileFqpn = locationElement.getAttribute("value");
                File propertiesFile = new File(propertiesFileFqpn);
                if (!propertiesFile.exists()) {
                    System.err.println("The property file specified in "
                            + configFileServerFqpn
                            + " does not exist: "
                            + propertiesFileFqpn);
                    throw new Exception();
                }

                GobiiConfig.printField("Local properties file", propertiesFileFqpn);

                ConfigSettings configSettings = new ConfigSettings(propertiesFileFqpn);

                String configServerUrl = "http://"
                        + configSettings.getCurrentCropConfig().getServer(ServerType.GOBII_WEB).getHost()
                        + ":"
                        + configSettings.getCurrentCropConfig().getServer(ServerType.GOBII_WEB).getPort().toString()
                        + "/"
                        + configSettings.getCurrentCropConfig().getServer(ServerType.GOBII_WEB).getContextPath();

                String configFileContextFqpn = tomcatBaseDirectory + "/conf/context.xml";
                File configFileContext = new File(configFileContextFqpn);

                if (!configFileContext.exists()) {
                    System.err.println("Cannot find config file: : " + configFileContextFqpn);
                    throw new Exception();
                }

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

                if (GobiiConfig.showServerInfo(configClientContext(configServerUrl))) {
                    exitCode = 0;
                }

            } else if (commandLine.hasOption(CONFIG_BASE_URL)) {
                GobiiConfig.printSeparator();
                GobiiConfig.printField("Configuration Mode", "From url");

                if (GobiiConfig.showServerInfo(configClientContext(commandLine.getOptionValue(CONFIG_BASE_URL)))) {
                    exitCode = 0;
                }

            } else if (commandLine.hasOption(CONFIG_MKDIRS) && commandLine.hasOption(PROP_FILE_FQPN)) {
                if (GobiiConfig.makeGobiiDirectories(commandLine.getOptionValue(PROP_FILE_FQPN))) {
                    exitCode = 0;
                }

            } else if (commandLine.hasOption(COPY_WARS) && commandLine.hasOption(PROP_FILE_FQPN)) {
                if (GobiiConfig.copyWars(commandLine.getOptionValue(PROP_FILE_FQPN), commandLine.getOptionValue(COPY_WARS))) {
                    exitCode = 0;
                }

            } else if (commandLine.hasOption(PROP_FILE_PROPS_TO_XML) && commandLine.hasOption(PROP_FILE_FQPN)) {

                String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);

                File propsFile = new File(propFileFqpn);
                if (propsFile.exists() && !propsFile.isDirectory()) {
                    exitCode = 0;
                } else {
                    System.err.println("Cannot find config file: : " + propFileFqpn);
                }

                // this should do the conversion automatically
                ConfigSettings configSettings = new ConfigSettings(propFileFqpn);
                configSettings.commit();

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

            } else if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_DECRYPT) && commandLine.hasOption(PROP_FILE_FQPN)) {
                if (setDecryptionFlag(options, commandLine.getOptionValue(PROP_FILE_FQPN), commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_DECRYPT))) {
                    exitCode = 0;
                }

            } else if (commandLine.hasOption(VALIDATE_CONFIGURATION)) {
                String propFileFqpn = null;
                if (commandLine.hasOption(PROP_FILE_FQPN) &&
                        (null != (propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN)))) {
                    if (validateGobiiConfiguration(propFileFqpn, options, commandLine)) {
                        System.out.println("File " + propFileFqpn + " is valid.");
                        exitCode = 0;
                    }
                } else {
                    System.err.println("Value is required: " + options.getOption(PROP_FILE_FQPN).getDescription());
                }

            } else if ((commandLine.hasOption(SVR_KDC) || commandLine.hasOption(SVO_OWNC))
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                ServerType serverType = ServerType.UNKNOWN;
                if (commandLine.hasOption(SVR_KDC)) {
                    serverType = ServerType.KDC;
                } else if (commandLine.hasOption(SVO_OWNC)) {
                    serverType = ServerType.OWN_CLOUD;
                }

                if (setGlobalServerOptions(serverType, options, commandLine)) {
                    exitCode = 0;
                }

            } else if (commandLine.hasOption(PROP_FILE_FQPN) && commandLine.hasOption(SVR_KC)) {
                if (setKeyCloakOptions(options, commandLine)) {
                    exitCode = 0;
                }
            } else {
                new HelpFormatter().printHelp(NAME_COMMAND, options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.exit(exitCode);

    }// main()


    private static boolean setKeyCloakOptions(Options options, CommandLine commandLine) {
        String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);
        ConfigSettings configSettings = getConfigSettings(propFileFqpn);
        String value;
        List<String> argsSet = new ArrayList<>();
        List<String> valsSet = new ArrayList<>();


        try {
            KeycloakConfig config = configSettings.getKeycloakConfig();

            if (commandLine.hasOption(KEYCLOAK_REALM)) {
                value = commandLine.getOptionValue(KEYCLOAK_REALM);
                config.setRealm(value);
                argsSet.add(KEYCLOAK_REALM);
                valsSet.add(value);
            }

            if (commandLine.hasOption(KEYCLOAK_RESOURCE)) {
                value = commandLine.getOptionValue(KEYCLOAK_RESOURCE);
                config.setResource(value);
                argsSet.add(KEYCLOAK_RESOURCE);
                valsSet.add(value);
            }

            if (commandLine.hasOption(KEYCLOAK_AUTH_SERVER_URL)) {
                value = commandLine.getOptionValue(KEYCLOAK_AUTH_SERVER_URL);
                config.setAuthServerUrl(value);
                argsSet.add(KEYCLOAK_AUTH_SERVER_URL);
                valsSet.add(value);
            }

            if (commandLine.hasOption(KEYCLOAK_ADMIN_USERNAME)) {
                value = commandLine.getOptionValue(KEYCLOAK_ADMIN_USERNAME);
                config.setAdminUsername(value);
                argsSet.add(KEYCLOAK_ADMIN_USERNAME);
                valsSet.add(value);
            }

            if (commandLine.hasOption(KEYCLOAK_ADMIN_PASSWORD)) {
                value = commandLine.getOptionValue(KEYCLOAK_ADMIN_PASSWORD);
                config.setAdminPassword(value);
                argsSet.add(KEYCLOAK_ADMIN_PASSWORD);
                valsSet.add(value);
            }

            if (commandLine.hasOption(KEYCLOAK_EXTRACTOR_UI_CLIENT)) {
                value = commandLine.getOptionValue(KEYCLOAK_EXTRACTOR_UI_CLIENT);
                config.setExtractorUIClient(value);
                argsSet.add(KEYCLOAK_EXTRACTOR_UI_CLIENT);
                valsSet.add(value);
            }

            writeConfigSettingsMessage(
                options,
                ServerType.KEY_CLOAK,
                propFileFqpn,
                argsSet,
                valsSet,
                null
            );

            configSettings.setKeycloakConfig(config);
            configSettings.commit();
        } catch (Exception e) {
            return false;
        }

        return true;
    }


    private static void writeConfigSettingsMessage(Options options,
                                                   ServerType serverType,
                                                   String configFileFqpn,
                                                   List<String> configArgs,
                                                   List<String> configVals,
                                                   String cropId) throws Exception {

        if (configArgs.size() != configVals.size()) {
            throw new Exception("The size of the options and values arrays do not match");
        }

        String contextMessage = "The following "
                + ((serverType != null && serverType != ServerType.UNKNOWN) ? " " + serverType.toString() + " " : "")
                + (LineUtils.isNullOrEmpty(cropId) ? "global " : "")
                + "options "
                + (LineUtils.isNullOrEmpty(cropId) ? "" : "for the " + cropId + " crop ")
                + "are being written to the the config file "
                + configFileFqpn
                + ": ";

        System.out.println();
        System.out.println("__________________________________________________");
        System.out.println(contextMessage);
        for (int idx = 0; idx < configArgs.size(); idx++) {
            String currentArg = configArgs.get(idx);
            String currentOption = options.getOption(currentArg).getArgName();
            String curentDescription = options.getOption(currentArg).getDescription();
            String currentValue = configVals.get(idx);
            System.out.println("-" + currentArg + " <" + currentOption + ">:\t\t" + currentValue + " (" + curentDescription + ")");
        }
    }


    private static ConfigSettings getConfigSettings(String propFileFqpn) {

        ConfigSettings returnVal = null;

        try {
            File file = new File(propFileFqpn);
            if (file.exists()) {
                returnVal = ConfigSettings.read(propFileFqpn);
            } else {
                returnVal = ConfigSettings.makeNew(propFileFqpn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnVal;

    }


    private static boolean setDecryptionFlag(Options options, String propFileFqpn, String flagAsString) {

        boolean returnVal = false;

        try {

            ConfigSettings configSettings = getConfigSettings(propFileFqpn);

            boolean decrypt = false;
            if (flagAsString.toLowerCase().equals("true")) {
                decrypt = true;
            }

            configSettings.setIsDecrypt(decrypt);
            configSettings.commit();
            returnVal = true;

            writeConfigSettingsMessage(options,
                    ServerType.UNKNOWN,
                    propFileFqpn,
                    Arrays.asList(CONFIG_SVR_GLOBAL_LDAP_DECRYPT),
                    Arrays.asList(decrypt ? "true" : "false"),
                    null);

        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;
        }

        return returnVal;
    }

    private static boolean setGlobalServerOptions(ServerType serverType,
                                                  Options options,
                                                  CommandLine commandLine) {

        boolean returnVal = false;

        try {

            String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);

            ConfigSettings configSettings = getConfigSettings(propFileFqpn);


            List<String> argsSet = new ArrayList<>();
            List<String> valsSet = new ArrayList<>();


            String svrHost;
            Integer port;
            String contextPath;
            //String errorContextPath;
            String userName;
            String password;
            String resourceQCStart;
            String resourceQCStatus;
            String resourceQCDownload;
            String resourceQCPurge;
            Integer statusCheckIntervalSecs;
            Integer statusWaitThresholdMinutes;
            boolean active;


            if (commandLine.hasOption(CONFIG_SVR_OPTIONS_HOST)) {
                svrHost = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_HOST);
                argsSet.add(CONFIG_SVR_OPTIONS_HOST);
                valsSet.add(svrHost);
                configSettings.getGlobalServer(serverType).setHost(svrHost);
            }

            if (commandLine.hasOption(CONFIG_SVR_OPTIONS_PORT)) {
                port = Integer.parseInt(commandLine.getOptionValue(CONFIG_SVR_OPTIONS_PORT));
                argsSet.add(CONFIG_SVR_OPTIONS_PORT);
                valsSet.add(port.toString());
                configSettings.getGlobalServer(serverType).setPort(port);
            }

            if (commandLine.hasOption(CONFIG_SVR_OPTIONS_CONTEXT_PATH)) {
                contextPath = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_CONTEXT_PATH);
                argsSet.add(CONFIG_SVR_OPTIONS_CONTEXT_PATH);
                valsSet.add(contextPath);
                configSettings.getGlobalServer(serverType).setContextPath(contextPath);
            }

            if (commandLine.hasOption(CONFIG_SVR_OPTIONS_USER_NAME)) {
                userName = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_USER_NAME);
                argsSet.add(CONFIG_SVR_OPTIONS_USER_NAME);
                valsSet.add(userName);
                configSettings.getGlobalServer(serverType).setUserName(userName);
            }

            if (commandLine.hasOption(CONFIG_SVR_OPTIONS_PASSWORD)) {
                password = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_PASSWORD);
                argsSet.add(CONFIG_SVR_OPTIONS_PASSWORD);
                valsSet.add(password);
                configSettings.getGlobalServer(serverType).setPassword(password);
            }

            if (commandLine.hasOption(CONFIG_SVR_OPTIONS_ACTIVE)) {
                String activeStr = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_ACTIVE);
                if (activeStr.toLowerCase().equals("false")) {
                    active = false;
                } else {
                    active = true;
                }
            } else {
                active = true;
            }
            argsSet.add(CONFIG_SVR_OPTIONS_ACTIVE);
            valsSet.add(active ? "true" : "false");
            configSettings.getGlobalServer(serverType).setActive(active);

            if (serverType.equals(ServerType.KDC)) {
                if (commandLine.hasOption(SVR_KDC_RESOURCE_START)) {
                    resourceQCStart = commandLine.getOptionValue(SVR_KDC_RESOURCE_START);
                    argsSet.add(SVR_KDC_RESOURCE_START);
                    valsSet.add(resourceQCStart);
                    configSettings.getGlobalServer(ServerType.KDC).setCallResourcePath(RestResourceId.KDC_START, resourceQCStart);
                }

                if (commandLine.hasOption(SVR_KDC_RESOURCE_STATUS)) {
                    resourceQCStatus = commandLine.getOptionValue(SVR_KDC_RESOURCE_STATUS);
                    argsSet.add(SVR_KDC_RESOURCE_STATUS);
                    valsSet.add(resourceQCStatus);
                    configSettings.getGlobalServer(ServerType.KDC).setCallResourcePath(RestResourceId.KDC_STATUS, resourceQCStatus);
                }

                if (commandLine.hasOption(SVR_KDC_RESOURCE_DOWNLOAD)) {
                    resourceQCDownload = commandLine.getOptionValue(SVR_KDC_RESOURCE_DOWNLOAD);
                    argsSet.add(SVR_KDC_RESOURCE_DOWNLOAD);
                    valsSet.add(resourceQCDownload);
                    configSettings.getGlobalServer(ServerType.KDC).setCallResourcePath(RestResourceId.KDC_DOWNLOAD, resourceQCDownload);
                }

                if (commandLine.hasOption(SVR_KDC_RESOURCE_PURGE)) {
                    resourceQCPurge = commandLine.getOptionValue(SVR_KDC_RESOURCE_PURGE);
                    argsSet.add(SVR_KDC_RESOURCE_PURGE);
                    valsSet.add(resourceQCPurge);
                    configSettings.getGlobalServer(ServerType.KDC).setCallResourcePath(RestResourceId.KDC_PURGE, resourceQCPurge);
                }

                if (commandLine.hasOption(SVR_KDC_STATUS_CHECK_INTERVAL_SECS)) {
                    statusCheckIntervalSecs = Integer.parseInt(commandLine.getOptionValue(SVR_KDC_STATUS_CHECK_INTERVAL_SECS));
                    argsSet.add(SVR_KDC_STATUS_CHECK_INTERVAL_SECS);
                    valsSet.add(statusCheckIntervalSecs.toString());
                    configSettings.getGlobalServer(ServerType.KDC).setStatusCheckIntervalSecs(statusCheckIntervalSecs);
                }

                if (commandLine.hasOption(SVR_KDC_STATUS_CHECK_MAX_TIME_MINS)) {
                    statusWaitThresholdMinutes = Integer.parseInt(commandLine.getOptionValue(SVR_KDC_STATUS_CHECK_MAX_TIME_MINS));
                    argsSet.add(SVR_KDC_STATUS_CHECK_MAX_TIME_MINS);
                    valsSet.add(statusWaitThresholdMinutes.toString());
                    configSettings.getGlobalServer(ServerType.KDC).setMaxStatusCheckMins(statusWaitThresholdMinutes);
                }

            }
            if(serverType.equals(ServerType.OWN_CLOUD)){
                if(commandLine.hasOption(SVR_OWNC_ERR)){
                    String errorPath = commandLine.getOptionValue(SVR_OWNC_ERR);
                    argsSet.add(SVR_OWNC_ERR);
                    valsSet.add(errorPath);
                    configSettings.getGlobalServer(ServerType.OWN_CLOUD).setErrorContextPath(errorPath);
                }
            }


            writeConfigSettingsMessage(options,
                    serverType,
                    propFileFqpn,
                    argsSet,
                    valsSet,
                    null);

            configSettings.commit();
            returnVal = true;


        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;
        }

        return returnVal;
    }

    private static boolean setGobiiConfiguration(String propFileFqpn, Options options, CommandLine commandLine) {

        boolean returnVal = true;

        try {


            ConfigSettings configSettings = getConfigSettings(propFileFqpn);

            if (commandLine.hasOption(CONFIG_GLOBAL_FILESYS_ROOT)) {

                String fileSysRoot = commandLine.getOptionValue(CONFIG_GLOBAL_FILESYS_ROOT);

                configSettings.setFileSystemRoot(fileSysRoot);
                configSettings.commit();

                writeConfigSettingsMessage(options,
                        ServerType.UNKNOWN,
                        propFileFqpn,
                        Arrays.asList(CONFIG_GLOBAL_FILESYS_ROOT),
                        Arrays.asList(fileSysRoot),
                        null);


            }
            else if (commandLine.hasOption(CONFIG_GLOBAL_FILESYS_HDF5)) {
                String fileSysHDF5 = commandLine.getOptionValue(CONFIG_GLOBAL_FILESYS_HDF5);
                configSettings.setFileSystemHDF5(fileSysHDF5);
                configSettings.commit();

                writeConfigSettingsMessage(options,
                        ServerType.UNKNOWN,
                        propFileFqpn,
                        Arrays.asList(CONFIG_GLOBAL_FILESYS_HDF5),
                        Arrays.asList(fileSysHDF5),
                        null);

            }
            else if (commandLine.hasOption(CONFIG_GLOBAL_FILESYS_LOG)) {
                String fileSysLog = commandLine.getOptionValue(CONFIG_GLOBAL_FILESYS_LOG);
                configSettings.setFileSystemLog(fileSysLog);
                configSettings.commit();

                writeConfigSettingsMessage(options,
                        ServerType.UNKNOWN,
                        propFileFqpn,
                        Arrays.asList(CONFIG_GLOBAL_FILESYS_LOG),
                        Arrays.asList(fileSysLog),
                        null);

            } else if (commandLine.hasOption(CONFIG_GLOBAL_PROVIDES_BACKEND)) {
                String flagAsString = commandLine.getOptionValue(CONFIG_GLOBAL_PROVIDES_BACKEND);
                boolean flag = flagAsString.equalsIgnoreCase("true");

                configSettings.setProvidesBackend(flag);
                configSettings.commit();

                writeConfigSettingsMessage(options,
                        ServerType.UNKNOWN,
                        propFileFqpn,
                        Arrays.asList(CONFIG_GLOBAL_PROVIDES_BACKEND),
                        Arrays.asList(flag ? "true" : "false"),
                        null);

            } else if (commandLine.hasOption(CONFIG_MARK_CROP_ACTIVE) &&
                    commandLine.hasOption(CONFIG_CROP_ID)) {

                String cropId = commandLine.getOptionValue(CONFIG_CROP_ID);
                if (configSettings.isCropDefined(cropId)) {
                    GobiiCropConfig gobiiCropConfig = configSettings.getCropConfig(cropId);
                    gobiiCropConfig.setActive(true);
                } else {
                    returnVal = false;
                    System.err.println("The specified crop does not exist: " + cropId);
                }

                configSettings.commit();

            } else if (commandLine.hasOption(CONFIG_MARK_CROP_NOTACTIVE) &&
                    commandLine.hasOption(CONFIG_CROP_ID)) {

                String cropId = commandLine.getOptionValue(CONFIG_CROP_ID);
                if (configSettings.isCropDefined(cropId)) {
                    GobiiCropConfig gobiiCropConfig = configSettings.getCropConfig(cropId);
                    gobiiCropConfig.setActive(false);
                } else {
                    returnVal = false;
                    System.err.println("The specified crop does not exist: " + cropId);

                }

                configSettings.commit();


                //CONFIG_SVR_GLOBAL_MAX_UPLOAD_MBYTES
            } else if (commandLine.hasOption(CONFIG_SVR_GLOBAL_MAX_UPLOAD_MBYTES)) {

                String maxUploadMbytes = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_MAX_UPLOAD_MBYTES);

                if (NumberUtils.isNumber(maxUploadMbytes)) {
                    Integer maxUPloadMbytesInt = Integer.parseInt(maxUploadMbytes);
                    configSettings.setMaxUploadSizeMbytes(maxUPloadMbytesInt);

                } else {
                    returnVal = false;
                    System.err.println("The max upload size is not numeric: " + maxUploadMbytes);

                }

                configSettings.commit();


                //CONFIG_SVR_GLOBAL_MAX_UPLOAD_MBYTES
            } else if (commandLine.hasOption(CONFIG_TST_GLOBAL)) {

                List<String> argsSet = new ArrayList<>();
                List<String> valsSet = new ArrayList<>();

                //String configFileFqpn = null;
                String configFileTestDirectory = null;
                String configUtilCommandlineStem = null;
                String initialConfigUrl = null;
                String initialConfigUrlForSshOverride = null;
                String sshOverrideHost = null;
                Integer sshOverridePort = null;
                String testCrop = null;
                String ldapTestUser = null;
                String ldapTestPassword = null;
                String testDownloadDirectory = null;
                boolean isTestSsh = false;

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_INTIAL_URL)) {
                    initialConfigUrl = commandLine.getOptionValue(CONFIG_TST_GLOBAL_INTIAL_URL);
                    argsSet.add(CONFIG_TST_GLOBAL_INTIAL_URL);
                    valsSet.add(initialConfigUrl);
                    configSettings.getTestExecConfig().setInitialConfigUrl(initialConfigUrl);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_SSH_INTIAL_URL)) {
                    initialConfigUrlForSshOverride = commandLine.getOptionValue(CONFIG_TST_GLOBAL_SSH_INTIAL_URL);
                    argsSet.add(CONFIG_TST_GLOBAL_SSH_INTIAL_URL);
                    valsSet.add(initialConfigUrlForSshOverride);
                    configSettings.getTestExecConfig().setInitialConfigUrlForSshOverride(initialConfigUrlForSshOverride);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_SSH_HOST)) {
                    sshOverrideHost = commandLine.getOptionValue(CONFIG_TST_GLOBAL_SSH_HOST);
                    argsSet.add(CONFIG_TST_GLOBAL_SSH_HOST);
                    valsSet.add(sshOverrideHost);
                    configSettings.getTestExecConfig().setSshOverrideHost(sshOverrideHost);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_SSH_PORT)) {
                    String portString = commandLine.getOptionValue(CONFIG_TST_GLOBAL_SSH_PORT);
                    if (!NumberUtils.isNumber(portString)) {
                        throw new Exception("Specified value for "
                                + CONFIG_TST_GLOBAL_SSH_PORT
                                + "("
                                + options.getOption(CONFIG_TST_GLOBAL_SSH_PORT).getDescription()
                                + ") is not a valid integer: "
                                + portString);
                    }
                    sshOverridePort = Integer.parseInt(portString);
                    argsSet.add(CONFIG_TST_GLOBAL_SSH_PORT);
                    valsSet.add(sshOverridePort.toString());
                    configSettings.getTestExecConfig().setSshOverridePort(sshOverridePort);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_SSH_FLAG)) {
                    isTestSsh = commandLine.getOptionValue(CONFIG_TST_GLOBAL_SSH_FLAG).toLowerCase().equals("true") ? true : false;
                    argsSet.add(CONFIG_TST_GLOBAL_SSH_FLAG);
                    valsSet.add(isTestSsh ? "true" : "false");
                    configSettings.getTestExecConfig().setTestSsh(isTestSsh);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_CONFIG_DIR_TEST)) {
                    configFileTestDirectory = commandLine.getOptionValue(CONFIG_TST_GLOBAL_CONFIG_DIR_TEST);
                    argsSet.add(CONFIG_TST_GLOBAL_CONFIG_DIR_TEST);
                    valsSet.add(configFileTestDirectory);
                    configSettings.getTestExecConfig().setConfigFileTestDirectory(configFileTestDirectory);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM)) {
                    configUtilCommandlineStem = commandLine.getOptionValue(CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM);
                    argsSet.add(CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM);
                    valsSet.add(configUtilCommandlineStem);
                    configSettings.getTestExecConfig().setConfigUtilCommandlineStem(configUtilCommandlineStem);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_CONFIG_CROP_ID)) {
                    testCrop = commandLine.getOptionValue(CONFIG_TST_GLOBAL_CONFIG_CROP_ID);
                    argsSet.add(CONFIG_TST_GLOBAL_CONFIG_CROP_ID);
                    valsSet.add(testCrop);
                    configSettings.getTestExecConfig().setTestCrop(testCrop);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_LDAP_USER)) {
                    ldapTestUser = commandLine.getOptionValue(CONFIG_TST_GLOBAL_LDAP_USER);
                    argsSet.add(CONFIG_TST_GLOBAL_LDAP_USER);
                    valsSet.add(ldapTestUser);
                    configSettings.getTestExecConfig().setLdapUserForUnitTest(ldapTestUser);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_LDAP_PASSWORD)) {
                    ldapTestPassword = commandLine.getOptionValue(CONFIG_TST_GLOBAL_LDAP_PASSWORD);
                    argsSet.add(CONFIG_TST_GLOBAL_LDAP_PASSWORD);
                    valsSet.add(ldapTestPassword);
                    configSettings.getTestExecConfig().setLdapPasswordForUnitTest(ldapTestPassword);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_DOWNLOAD_DIR)) {
                    testDownloadDirectory = commandLine.getOptionValue(CONFIG_TST_GLOBAL_DOWNLOAD_DIR);
                    argsSet.add(CONFIG_TST_GLOBAL_DOWNLOAD_DIR);
                    valsSet.add(testDownloadDirectory);
                    configSettings.getTestExecConfig().setTestFileDownloadDirectory(testDownloadDirectory);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_ASYNCH_OP_TIMEOUT)) {
                    String asycnTimeOutValAsString = commandLine.getOptionValue(CONFIG_TST_GLOBAL_ASYNCH_OP_TIMEOUT);
                    if (!StringUtils.isNumeric(asycnTimeOutValAsString)) {
                        throw new Exception("Value provided for option " + CONFIG_TST_GLOBAL_ASYNCH_OP_TIMEOUT + "is not a number");
                    }
                    Integer asycnTimeout = Integer.parseInt(asycnTimeOutValAsString);
                    argsSet.add(CONFIG_TST_GLOBAL_ASYNCH_OP_TIMEOUT);
                    valsSet.add(asycnTimeout.toString());
                    configSettings.getTestExecConfig().setAsynchOpTimeoutMinutes(asycnTimeout);
                }

                configSettings.commit();

                writeConfigSettingsMessage(options,
                        ServerType.UNKNOWN,
                        propFileFqpn,
                        argsSet,
                        valsSet,
                        null);

            } else if (commandLine.hasOption(CONFIG_SVR_GLOBAL_AUTH_TYPE)) {

                List<String> argsSet = new ArrayList<>();
                List<String> valsSet = new ArrayList<>();

                String gobiiAuthenticationTypeRaw = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_AUTH_TYPE);
                GobiiAuthenticationType gobiiAuthenticationType = GobiiAuthenticationType.valueOf(gobiiAuthenticationTypeRaw);
                argsSet.add(CONFIG_SVR_GLOBAL_AUTH_TYPE);
                valsSet.add(gobiiAuthenticationTypeRaw);
                configSettings.setGobiiAuthenticationType(gobiiAuthenticationType);

                String ldapUserDnPattern = null;
                String ldapUrl = null;
                String ldapBindUser = null;
                String ldapBindPassword = null;
                String ldapRunAsUser = null;
                String ldapRunAsPassword = null;
                boolean ldapAuthenticateBrapi = true;

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_UDN)) {
                    ldapUserDnPattern = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_UDN);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_UDN);
                    valsSet.add(ldapUserDnPattern);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_URL)) {
                    ldapUrl = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_URL);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_URL);
                    valsSet.add(ldapUrl);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_BUSR)) {
                    ldapBindUser = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_BUSR);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_BUSR);
                    valsSet.add(ldapBindUser);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_BPAS)) {
                    ldapBindPassword = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_BPAS);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_BPAS);
                    valsSet.add(ldapBindPassword);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER)) {
                    ldapRunAsUser = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER);
                    valsSet.add(ldapRunAsUser);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD)) {
                    ldapRunAsPassword = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD);
                    valsSet.add(ldapRunAsPassword);
                }


                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_AUTHENTICATE_BRAPI)) {
                    ldapAuthenticateBrapi = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_AUTHENTICATE_BRAPI)
                            .toLowerCase()
                            .equals("true");
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_AUTHENTICATE_BRAPI);
                    valsSet.add(ldapAuthenticateBrapi ? "true" : "false");
                }

                configSettings.setLdapUrl(ldapUrl);
                configSettings.setLdapUserDnPattern(ldapUserDnPattern);
                configSettings.setLdapBindUser(ldapBindUser);
                configSettings.setLdapBindPassword(ldapBindPassword);
                configSettings.setLdapUserForBackendProcs(ldapRunAsUser);
                configSettings.setLdapPasswordForBackendProcs(ldapRunAsPassword);
                configSettings.setAuthenticateBrapi(ldapAuthenticateBrapi);
                configSettings.commit();

                writeConfigSettingsMessage(options,
                        ServerType.LDAP,
                        propFileFqpn,
                        argsSet,
                        valsSet,
                        null);


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

                    if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL_FROM)) {
                        String emailSvrFrom = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_EMAIL_FROM);
                        configSettings.setEmailSvrFrom(emailSvrFrom);
                        argsSet.add(CONFIG_SVR_GLOBAL_EMAIL_FROM);
                        valsSet.add(emailSvrFrom);
                    }

                    if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL_PASSWORD_TYPE)) {
                        String emailSvrPasswdType = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_EMAIL_PASSWORD_TYPE);
                        configSettings.setEmailSvrPassword(emailSvrPasswdType);
                        argsSet.add(CONFIG_SVR_GLOBAL_EMAIL_PASSWORD_TYPE);
                        valsSet.add(emailSvrPasswdType);
                    }

                    if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE)) {
                        String hashhType = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE);
                        configSettings.setEmailSvrHashType(hashhType);
                        argsSet.add(CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE);
                        valsSet.add(hashhType);
                    }


                } else if (commandLine.hasOption(CONFIG_CROP_ID)) {

                    ServerType serverType = ServerType.UNKNOWN;

                    String cropId = commandLine.getOptionValue(CONFIG_CROP_ID);


                    if (!configSettings.isCropDefined(cropId)) {
                        configSettings.setCrop(cropId, true, null, null, null);
                    }

                    GobiiCropConfig gobiiCropConfig = configSettings.getCropConfig(cropId);

                    String contextRoot = null;
                    if (commandLine.hasOption(CONFIG_SVR_OPTIONS_CONTEXT_PATH)) {
                        contextRoot = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_CONTEXT_PATH);
                        argsSet.add(CONFIG_SVR_OPTIONS_CONTEXT_PATH);
                        valsSet.add(contextRoot);
                    }

                    if (commandLine.hasOption(CONFIG_SVR_CROP_WEB)) {

                        argsSet.add(CONFIG_SVR_CROP_WEB);
                        valsSet.add("");

                        serverType = ServerType.GOBII_WEB;
                        gobiiCropConfig.getServer(serverType).setHost(svrHost);
                        gobiiCropConfig.getServer(serverType).setPort(svrPort);
                        gobiiCropConfig.getServer(serverType).setContextPath(contextRoot);

                    } else if (commandLine.hasOption(CONFIG_SVR_CROP_POSTGRES) ||
                            (commandLine.hasOption(CONFIG_SVR_CROP_COMPUTE))) {

                        if (commandLine.hasOption(CONFIG_SVR_CROP_POSTGRES)) {
                            serverType = ServerType.GOBII_PGSQL;
                            argsSet.add(CONFIG_SVR_CROP_POSTGRES);
                            valsSet.add("");
                        } else if (commandLine.hasOption(CONFIG_SVR_CROP_COMPUTE)) {
                            serverType = ServerType.GOBII_COMPUTE;
                            argsSet.add(CONFIG_SVR_CROP_COMPUTE);
                            valsSet.add("");
                        }

                        gobiiCropConfig.addServer(serverType,
                                svrHost,
                                contextRoot,
                                svrPort,
                                svrUserName,
                                svrPassword,
                                false,
                                null);

                    } else {
                        // do nothing: allow control to fall through to print help
                    }

                    writeConfigSettingsMessage(options,
                            serverType,
                            propFileFqpn,
                            argsSet,
                            valsSet,
                            cropId);

                }

                configSettings.commit();


            } else if (commandLine.hasOption(CONFIG_REMOVE_CROP)) {

                String cropId = commandLine.getOptionValue(CONFIG_REMOVE_CROP);

                if (configSettings.isCropDefined(cropId)) {

                    configSettings.removeCrop(cropId);
                    configSettings.commit();
                } else {
                    System.err.println("The following crop does not exist: " + cropId);
                    returnVal = false;
                }

            } else {
                // do nothing: allow control to fall through to print help

            }

        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;
        }

        return returnVal;

    }

    private static boolean validateGobiiConfiguration(String propFileFqpn, Options options, CommandLine commandLine) {

        boolean returnVal = true;

        try {

            if (commandLine.hasOption(VALIDATE_CONFIGURATION) && commandLine.hasOption(PROP_FILE_FQPN)) {

                List<String> messages = new ArrayList<>();
                String configFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);
                ConfigSettings configSettings = new ConfigSettings(configFileFqpn);

                if (LineUtils.isNullOrEmpty(configSettings.getEmailSvrDomain())) {
                    messages.add("An email server host is not defined");
                    returnVal = false;
                }

                if (configSettings.getEmailServerPort() == null) {
                    messages.add("An email port is not defined");
                    returnVal = false;
                }

                if (LineUtils.isNullOrEmpty(configSettings.getEmailSvrUser())) {
                    messages.add("An email server user id is not defined");
                    returnVal = false;
                }

                if (LineUtils.isNullOrEmpty(configSettings.getEmailSvrUser())) {
                    messages.add("An email server password is not defined");
                    returnVal = false;
                }

                if (LineUtils.isNullOrEmpty(configSettings.getFileSystemRoot())) {
                    messages.add("A file system root is not defined");
                    returnVal = false;
                } else {
                    File directoryToTest = new File(configSettings.getFileSystemRoot());
                    if (!directoryToTest.exists() || !directoryToTest.isDirectory()) {
                        messages.add("The specified file system root does not exist or is not a directory: " + configSettings.getFileSystemRoot());
                        returnVal = false;
                    }
                }

                if (configSettings.getGobiiAuthenticationType() == null) {
                    messages.add("An authentication type is not specified");
                    returnVal = false;
                }

                // for TEST authentication we use internal, in-memory users
                if (!configSettings.getGobiiAuthenticationType().equals(GobiiAuthenticationType.TEST)) {

                    if (LineUtils.isNullOrEmpty(configSettings.getLdapUserDnPattern())) {
                        messages.add("The authentication type is "
                                + configSettings.getGobiiAuthenticationType().toString()
                                + " but a user dn pattern is not specified");
                        returnVal = false;
                    }

                    if (LineUtils.isNullOrEmpty(configSettings.getLdapUrl())) {
                        messages.add("The authentication type is "
                                + configSettings.getGobiiAuthenticationType().toString()
                                + " but an ldap url is not specified");
                        returnVal = false;
                    }

                    if (configSettings.getGobiiAuthenticationType().equals(GobiiAuthenticationType.LDAP_CONNECT_WITH_MANAGER) ||
                            configSettings.getGobiiAuthenticationType().equals(GobiiAuthenticationType.ACTIVE_DIRECTORY_CONNECT_WITH_MANAGER)) {

                        if (LineUtils.isNullOrEmpty(configSettings.getLdapBindUser())) {
                            messages.add("The authentication type is "
                                    + configSettings.getGobiiAuthenticationType().toString()
                                    + " but an ldap bind user is not specified");
                            returnVal = false;
                        }

                        if (LineUtils.isNullOrEmpty(configSettings.getLdapBindPassword())) {
                            messages.add("The authentication type is "
                                    + configSettings.getGobiiAuthenticationType().toString()
                                    + " but an ldap bind password is not specified");
                            returnVal = false;
                        }

                    } // if the authentication type requires connection credentails

                } // if the authentication type requires url and user dn pattern


                if (LineUtils.isNullOrEmpty(configSettings.getFileSystemLog())) {
                    messages.add("A file system log directory is not defined");
                    returnVal = false;
                } else {
                    File directoryToTest = new File(configSettings.getFileSystemLog());
                    if (!directoryToTest.exists() || !directoryToTest.isDirectory()) {
                        messages.add("The specified file system log does not exist or is not a directory: " + configSettings.getFileSystemLog());
                        returnVal = false;
                    }
                }


                if (LineUtils.isNullOrEmpty(configSettings.getFileSysCropsParent())) {
                    messages.add("A file system crop parent directory is not defined");
                    returnVal = false;
                } else {
                    File directoryToTest = new File(configSettings.getFileSysCropsParent());
                    if (!directoryToTest.exists() || !directoryToTest.isDirectory()) {
                        messages.add("The specified file crop parent directory does not exist or is not a directory: " + configSettings.getFileSysCropsParent());
                        returnVal = false;
                    }
                }


                if (configSettings.getTestExecConfig() == null) {
                    messages.add("No test exec configuration is defined");
                    returnVal = false;
                }

                if (LineUtils.isNullOrEmpty(configSettings.getTestExecConfig().getTestCrop())) {
                    messages.add("A test crop id is not defined");
                    returnVal = false;
                } else {

                    String cropId = configSettings.getTestExecConfig().getTestCrop();
                    if (!configSettings.isCropDefined(cropId)) {
                        messages.add("The test crop is not defined in the crop configurations: " + cropId);
                        returnVal = false;
                    }

                    if (configSettings
                            .getActiveCropConfigs()
                            .stream()
                            .filter(cropConfig -> cropConfig.getGobiiCropType().equals(cropId))
                            .count() != 1) {
                        messages.add("The specified test crop config is not an active crop: " + cropId);
                        returnVal = false;
                    }
                }


                if (LineUtils.isNullOrEmpty(configSettings.getTestExecConfig().getInitialConfigUrl())) {
                    messages.add("An initial configuration url for testing is not defined");
                    returnVal = false;
                }


                if (LineUtils.isNullOrEmpty(configSettings.getTestExecConfig().getConfigFileTestDirectory())) {
                    messages.add("A a directory for test files is not defined");
                    returnVal = false;
                } else {
                    String testDirectoryPath = configSettings.getTestExecConfig().getConfigFileTestDirectory();
                    File testFilePath = new File(configSettings.getTestExecConfig().getConfigFileTestDirectory());
                    if (!testFilePath.exists()) {
                        messages.add("The specified test file path does not exist: "
                                + testDirectoryPath);
                        returnVal = false;
                    }
                }


                if (LineUtils.isNullOrEmpty(configSettings.getTestExecConfig().getConfigUtilCommandlineStem())) {
                    messages.add("The commandline stem of this utility for testing purposes is not defined");
                    returnVal = false;
                }


                if (configSettings.getActiveCropConfigs().size() < 1) {
                    messages.add("No active crops are defined");
                    returnVal = false;
                }

                List<String> contextPathList = new ArrayList<>();
                for (GobiiCropConfig currentGobiiCropConfig : configSettings.getAllCropConfigs()) {


                    // we don't need to test for unique cropNames because they are stored  in a map keyed
                    // by crop name
                    if (LineUtils.isNullOrEmpty(currentGobiiCropConfig.getGobiiCropType())) {
                        messages.add("The crop type for the crop is not defined");
                        returnVal = false;
                    }


                    if (LineUtils.isNullOrEmpty(currentGobiiCropConfig.getServer(ServerType.GOBII_WEB).getHost())) {
                        messages.add("The web server host for the crop (" + currentGobiiCropConfig.getGobiiCropType() + ") is not defined");
                        returnVal = false;

                    }


                    if (LineUtils.isNullOrEmpty(currentGobiiCropConfig.getServer(ServerType.GOBII_WEB).getContextPath())) {
                        messages.add("The web server context path for the crop (" + currentGobiiCropConfig.getGobiiCropType() + ") is not defined");
                        returnVal = false;
                    } else {
                        if (!contextPathList.contains(currentGobiiCropConfig.getServer(ServerType.GOBII_WEB).getContextPath())) {
                            contextPathList.add(currentGobiiCropConfig.getServer(ServerType.GOBII_WEB).getContextPath());
                        } else {
                            messages.add("The context path for the crop occurs more than once -- context paths must be unique:" + currentGobiiCropConfig.getServer(ServerType.GOBII_WEB).getContextPath());
                            returnVal = false;
                        }
                    }


                    if (currentGobiiCropConfig.getServer(ServerType.GOBII_WEB).getPort() == null) {
                        messages.add("The web server port for the crop (" + currentGobiiCropConfig.getGobiiCropType() + ") is not defined");
                        returnVal = false;

                    }

                    ServerConfig postGresConfig = currentGobiiCropConfig.getServer(ServerType.GOBII_PGSQL);
                    if (postGresConfig == null) {
                        messages.add("The postgresdb for the crop (" + currentGobiiCropConfig.getGobiiCropType() + ") is not defined");
                        returnVal = false;
                    } else {
                        returnVal = returnVal && verifyDbConfig(postGresConfig);
                    }

//                    ServerConfig computeNodeConfig = currentGobiiCropConfig.getServer(ServerType.GOBII_COMPUTE);
//                    if (computeNodeConfig == null) {
//                        messages.add("The compute node for the crop (" + currentGobiiCropConfig.getGobiiCropType() + ") is not defined");
//                        returnVal = false;
//                    } else {
//                        returnVal = returnVal && verifyDbConfig(computeNodeConfig);
//                    }
                }


                if (returnVal == false && messages.size() > 0) {
                    System.err.println("\n\n****************CONFIGURATION VALIDATION FAILED****************");
                    messages.forEach(m -> {
                        System.err.println(m);
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;
        }

        return returnVal;

    } //


    private static boolean verifyDbConfig(ServerConfig gobiiServerConfig) {

        boolean returnVal = true;

        if (LineUtils.isNullOrEmpty(gobiiServerConfig.getHost())) {
            System.err.println("The server  config for " + gobiiServerConfig.getServerType().toString() + " does not define a host");
            returnVal = false;
        }


        if (gobiiServerConfig.getPort() == null) {
            System.err.println("The server config for " + gobiiServerConfig.getServerType().toString() + " does not define a port");
            returnVal = false;
        }


        if (gobiiServerConfig.getServerType().equals(ServerType.GOBII_PGSQL)) {
            if (LineUtils.isNullOrEmpty(gobiiServerConfig.getUserName())) {
                System.err.println("The db config for " + gobiiServerConfig.getServerType().toString() + " does not define a user name");
                returnVal = false;
            }

            if (LineUtils.isNullOrEmpty(gobiiServerConfig.getPassword())) {
                System.err.println("The db config for " + gobiiServerConfig.getServerType().toString() + " does not define a password");
                returnVal = false;
            }

            if (LineUtils.isNullOrEmpty(gobiiServerConfig.getContextPath())) {
                System.err.println("The db config for " + gobiiServerConfig.getServerType().toString() + " does not define a database name");
                returnVal = false;
            }
        }

        return returnVal;
    }

    private static GobiiClientContext configClientContext(String configServerUrl) throws Exception {
        System.out.println();
        System.out.println();
        GobiiConfig.printSeparator();
        GobiiConfig.printField("Config request server", configServerUrl);

        System.out.println();
        System.out.println();
        GobiiConfig.printSeparator();

        return GobiiClientContext.getInstance(configServerUrl, true);

    }

    private static boolean showServerInfo(GobiiClientContext gobiiClientContext) throws Exception {

        boolean returnVal = true;

        // The logging framework emits debugging messages before it knows not to emit them.
        // Until we solve this problem, we we'll visually set those messages aside
        List<String> gobiiCropTypes = GobiiClientContext.getInstance(null, false).getCropTypeTypes();
        GobiiConfig.printSeparator();

        for (String currentCropId : gobiiCropTypes) {


            ServerConfigItem currentServerConfigItem = GobiiClientContext.getInstance(null, false).getServerConfig(currentCropId);

            GobiiConfig.printSeparator();
            GobiiConfig.printField("Crop Type", currentCropId.toString());
            GobiiConfig.printField("Host", currentServerConfigItem.getDomain());
            GobiiConfig.printField("Port", currentServerConfigItem.getPort().toString());
            GobiiConfig.printField("Context root", currentServerConfigItem.getContextRoot());

            GobiiConfig.printField("Loader instructions directory", currentServerConfigItem
                    .getFileLocations()
                    .get(GobiiFileProcessDir.LOADER_INSTRUCTIONS));

            GobiiConfig.printField("User file upload directory", currentServerConfigItem
                    .getFileLocations()
                    .get(GobiiFileProcessDir.RAW_USER_FILES));

            GobiiConfig.printField("Digester output directory ", currentServerConfigItem
                    .getFileLocations()
                    .get(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES));

            GobiiConfig.printField("Extractor instructions directory", currentServerConfigItem
                    .getFileLocations()
                    .get(GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS));

            //if(!LineUtils.isNullOrEmpty())

            // This ping thing gives out too much internal details about the server. Removing this for now.
//            SystemUsers systemUsers = new SystemUsers();
//            SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
//
//            if (GobiiClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword())) {
//
//                PingDTO pingDTORequest = new PingDTO();
//
//
//                //DtoRequestPing dtoRequestPing = new DtoRequestPing();
//                GobiiEnvelopeRestResource<PingDTO> gobiiEnvelopeRestResourcePingDTO = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
//                        .getUriFactory()
//                        .resourceColl(RestResourceId.GOBII_PING));
//
//                PayloadEnvelope<PingDTO> resultEnvelopePing = gobiiEnvelopeRestResourcePingDTO.post(PingDTO.class,
//                        new PayloadEnvelope<>(pingDTORequest, GobiiProcessType.CREATE));
//                //PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(new PayloadEnvelope<>(newContactDto, GobiiProcessType.CREATE));
//
//
//                Integer responseNum = 1;
//                if (resultEnvelopePing.getHeader().getStatus().isSucceeded()) {
//                    PingDTO pingDTOResponse = resultEnvelopePing.getPayload().getData().get(0);
//                    for (String currentResponse : pingDTOResponse.getPingResponses()) {
//                        GobiiConfig.printField("Ping response " + (responseNum++).toString(), currentResponse);
//                    }
//                } else {
//                    for (HeaderStatusMessage currentHeader : resultEnvelopePing.getHeader().getStatus().getStatusMessages()) {
//                        GobiiConfig.printField("Service error " + (responseNum++).toString(), currentHeader.messages());
//                        returnVal = false;
//                    }
//                }
//            } else {
//                System.err.println("Authentication to server for crop failed: " + currentCropType.toString());
//                returnVal = false;
//            }
        }

        return returnVal;
    }

    private static boolean makeGobiiDirectories(String propFileFqpn) {

        boolean returnVal = true;

        try {

            ConfigSettings configSettings = new ConfigSettings(propFileFqpn);
            if (configSettings.getActiveCropTypes().size() > 0) {
                for (String currentCrop : configSettings.getActiveCropTypes()) {

                    printSeparator();
                    printField("Checking directories for crop", currentCrop);


                    for (GobiiFileProcessDir currentRelativeDirectory : EnumSet.allOf(GobiiFileProcessDir.class)) {

                        String directoryToMake = configSettings.getProcessingPath(currentCrop, currentRelativeDirectory);


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
            } else {
                System.err.println("No directories were created because there are no active crops defined");
                returnVal = false;
            }


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
                    for (GobiiCropConfig currentCrop : configSettings.getActiveCropConfigs()) {

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
