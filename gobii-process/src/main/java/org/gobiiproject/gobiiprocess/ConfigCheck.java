package org.gobiiproject.gobiiprocess;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.codehaus.jackson.map.DeserializerFactory;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestPing;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
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
import java.util.List;

/**
 * Created by Phil on 6/24/2016.
 */
public class ConfigCheck {

    private static String NAME_COMMAND = "ConfigCheck";
    private static String TOMCAT_BASE_DIR = "tbase";
    private static String CONFIG_BASE_URL = "burl";


    private static void printSeparator() {
        System.out.println("\n\n*******************************************************************");
    }

    private static void printField(String fieldName, String value) {
        System.out.println("******\t" + fieldName + ": " + value);
    }

    public static void main(String[] args) {

        try {

            // define commandline options
            Options options = new Options();
            options.addOption(TOMCAT_BASE_DIR, true, "Tomcat base directory (e.g., /usr/local/tomcat7)");
            options.addOption(CONFIG_BASE_URL, true, "url of server from which to get initial config settings");

            // parse our commandline
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            HelpFormatter formatter = new HelpFormatter();

            if (commandLine.hasOption(TOMCAT_BASE_DIR)) {

                String tomcatBaseDirectory = commandLine.getOptionValue(TOMCAT_BASE_DIR);
                File tomcatDirectory = new File(tomcatBaseDirectory);

                if (tomcatDirectory.exists()) {

                    String configFileServerFqpn = tomcatBaseDirectory + "/conf/server.xml";

                    File configFileServer = new File(configFileServerFqpn);
                    if (configFileServer.exists()) {

                        ConfigCheck.printSeparator();
                        ConfigCheck.printField("Configuration Mode", "From tomcat server configuration");
                        ConfigCheck.printField("Tomcat file found", configFileServerFqpn);


                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document documentServer = documentBuilder.parse(new FileInputStream(configFileServer));


                        XPath xPathPropsLoc = XPathFactory.newInstance().newXPath();
                        NodeList nodesServer = (NodeList) xPathPropsLoc.evaluate("//Server/GlobalNamingResources/Environment[@name='gobiipropsloc']",
                                documentServer.getDocumentElement(), XPathConstants.NODESET);

                        Element locationElement = (Element) nodesServer.item(0);

                        if (null != locationElement) {

                            ConfigCheck.printField("Server configuration","Proper node found");

                            String propertiesFileFqpn = locationElement.getAttribute("value");
                            File propertiesFile = new File(propertiesFileFqpn);
                            if (propertiesFile.exists()) {

                                ConfigSettings configSettings = new ConfigSettings(propertiesFileFqpn);

                                GobiiCropType defaultCropType = configSettings.getDefaultGobiiCropType();
                                configSettings.setCurrentGobiiCropType(defaultCropType);

                                String configServerUrl = "http://"
                                        + configSettings.getCurrentCropConfig().getServiceDomain()
                                        + ":"
                                        + configSettings.getCurrentCropConfig().getServicePort().toString()
                                        + "/"
                                        + configSettings.getCurrentCropConfig().getServiceAppRoot();

                                String configFileContextFqpn = tomcatBaseDirectory + "/conf/context.xml";
                                File configFileContext = new File(configFileContextFqpn);
                                if( configFileContext.exists()) {

                                    ConfigCheck.printField("Tomcat file found", configFileContextFqpn);


                                    Document documentContext = documentBuilder.parse(new FileInputStream(configFileContext));
                                    XPath xPath = XPathFactory.newInstance().newXPath();
                                    NodeList nodesContext = (NodeList) xPath.evaluate("//Context/ResourceLink[@name='gobiipropsloc']",
                                            documentContext.getDocumentElement(), XPathConstants.NODESET);

                                    Element locationElementForLink = (Element) nodesContext.item(0);

                                    if( null != locationElementForLink ) {
                                        ConfigCheck.printField("Context configuration","Proper node found");
                                    } else {
                                        System.err.print("The configuration in server.xml does not define ResourceLink to the properties file: " + configFileServerFqpn);
                                    }


                                    ConfigCheck.showServerInfo(configServerUrl);


                                } else {
                                    System.err.println("Cannot find config file: : " + configFileContextFqpn);

                                }



                            } else {
                                System.err.print("The property file specified in "
                                        + configFileServerFqpn
                                        + " does not exist: "
                                        + propertiesFileFqpn);
                            }

                        } else {
                            System.err.print("The configuration does not define the properties file location: " + configFileServerFqpn);
                        }

                    } else {
                        System.err.print("Cannot find config file: : " + configFileServerFqpn);
                    }

                } else {
                    System.err.print("Specified tomcat base directory does not exist: " + tomcatBaseDirectory);
                }

            } else if (commandLine.hasOption(CONFIG_BASE_URL)) {

                String configUrl = commandLine.getOptionValue(CONFIG_BASE_URL);

                ConfigCheck.printSeparator();
                ConfigCheck.printField("Configuration Mode", "From url");

                ConfigCheck.showServerInfo(configUrl);

            } else {
                formatter.printHelp(NAME_COMMAND, options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }// main()

    private static void showServerInfo(String configServerUrl) throws Exception {

        // The logging framework emits debugging messages before it knows not to emit them.
        // Until we solve this problem, we we'll visually set those messages aside
        System.out.println();
        System.out.println();
        ConfigCheck.printSeparator();
        ConfigCheck.printField("Config request server", configServerUrl);

        System.out.println();
        System.out.println();
        ConfigCheck.printSeparator();


        List<GobiiCropType> gobiiCropTypes = ClientContext.getInstance(configServerUrl, true).getCropTypeTypes();
        ConfigCheck.printSeparator();

        ConfigCheck.printField("Default crop", ClientContext.getInstance(null, false).getDefaultCropType().toString());

        for (GobiiCropType currentCropType : gobiiCropTypes) {

            ClientContext.getInstance(null, false).setCurrentClientCrop(currentCropType);

            ConfigCheck.printSeparator();
            ConfigCheck.printField("Crop Type", currentCropType.toString());
            ConfigCheck.printField("Host", ClientContext.getInstance(null, false).getCurrentCropDomain());
            ConfigCheck.printField("Port", ClientContext.getInstance(null, false).getCurrentCropPort().toString());
            ConfigCheck.printField("Context root", ClientContext.getInstance(null, false).getCurrentCropContextRoot());

            ConfigCheck.printField("Loader instructions directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.LOADERINSTRUCTION_FILES));
            ConfigCheck.printField("User file upload directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.RAWUSER_FILES));
            ConfigCheck.printField("Digester output directory ", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.INTERMEDIATE_FILES));
            ConfigCheck.printField("Extractor instructions directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.EXTRACTORINSTRUCTION_FILES));


            SystemUsers systemUsers = new SystemUsers();
            SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());

            if (ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword())) {

                PingDTO pingDTORequest = new PingDTO();
                pingDTORequest.setControllerType(ControllerType.LOADER);

                DtoRequestPing dtoRequestPing = new DtoRequestPing();
                PingDTO pingDTOResponse = dtoRequestPing.process(pingDTORequest);

                Integer responseNum = 1;
                if( pingDTOResponse.getDtoHeaderResponse().isSucceeded()) {
                    for (String currentResponse : pingDTOResponse.getPingResponses()) {
                        ConfigCheck.printField("Ping response " + (responseNum++).toString(), currentResponse);
                    }
                } else {
                    for( HeaderStatusMessage currentHeader :  pingDTOResponse.getDtoHeaderResponse().getStatusMessages()) {
                        ConfigCheck.printField("Service error " + (responseNum++).toString(), currentHeader.getMessage());
                    }
                }
            } else {
                System.err.println("Authentication to server for crop failed: " + currentCropType.toString());
            }
        }
    }
}
