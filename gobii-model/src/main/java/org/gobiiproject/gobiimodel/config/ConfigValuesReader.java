package org.gobiiproject.gobiimodel.config;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.LoggerFactory;
import org.springframework.jndi.JndiTemplate;

import java.io.File;


/**
 * This class is responsible for reading and writing the GOBII configuration file. It knows
 * how to retrieve the name of the configuration file from the web context. It also knows
 * how to convert a legacy .properties file to a new XML format file.
 */
class ConfigValuesReader {

    private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ConfigValuesReader.class);



    /**
     * Creates a new and empty configuration file. If {@code fqpn} is null, fully-qualified pathname of the configuation
     * file will be read from the Tomcat configuration.
     *
     * @param userFqpn The fully-qualfiied pathname of the file to be created.
     * @return a new ConfigValues instance with empty collections and null property values.
     * @throws Exception
     */
    public static ConfigValues makeNew(String userFqpn) throws Exception {

        ConfigValues returnVal = null;


        String fqpn = userFqpn;
        if (LineUtils.isNullOrEmpty(fqpn)) {
            fqpn = getFqpnFromTomcat();
        }

        if (!LineUtils.isNullOrEmpty(fqpn)) {

            if (FilenameUtils.getExtension(fqpn).toLowerCase().equals("xml")) {

//                File existingFile = new File(fqpn);
//                if (existingFile.exists()) {

                String fileParentDirectory = FilenameUtils.getFullPath(fqpn);
                File directory = new File(fileParentDirectory);

                if (LineUtils.isNullOrEmpty(fileParentDirectory) || directory.exists()) {

                    if (FilenameUtils.getExtension(fqpn).toLowerCase().equals("xml")) {

                        returnVal = new ConfigValues();
                        ConfigFileReaderXml configFileReaderXml = new ConfigFileReaderXml();
                        configFileReaderXml.write(returnVal, fqpn);

                    } else {
                        throw new Exception("The specified file is not of type xml: " + fqpn);
                    }

                } else {
                    throw new Exception("The specified directory does not exist: " + fileParentDirectory);
                }

//                } else {
//                    throw new Exception("Configuration file already exists: " + fqpn);
//                }

            } else {
                throw new Exception("The input file name does not have the xml extension: " + "xml");
            }

        } else {
            throw new Exception("A fully-qualified path was not supplied and none exists in the Tomcat configuration");
        }

        return returnVal;

    }

    /**
     * Retrieves the fully-qualified pathname of the configuration file from the Tomcat configuration
     *
     * @return The full-qualified pathname of the configuration file.
     * @throws Exception
     */
    public static String getFqpnFromTomcat() throws Exception {

        String returnVal = null;

        JndiTemplate jndi = new JndiTemplate();
        returnVal = (String) jndi.lookup("java:comp/env/gobiipropsloc");


        return returnVal;
    } //


    /**
     * Reads the GOBII configuration file from disk and renders it in a ConfigValues instance.
     * If the fully-qualified pathmame of the file is not supplied, it is read from the Tomcat
     * configuration. If the configuration file is in the deprecated .properties format, it will
     * be converted to the xml version and renamed to .unused, making the new xml configuration file
     * the configuration repository of record for the system.
     *
     * @param userFqpn The fully-qualified pathmame of the file to be read. May be null if the Tomcat
     *                 configuration specifies the file.
     * @return
     * @throws Exception
     */
    public static ConfigValues read(String userFqpn) throws Exception {

        ConfigValues returnVal = null;

        try {

            String fqpn = userFqpn;

            if (LineUtils.isNullOrEmpty(fqpn)) {

                fqpn = getFqpnFromTomcat();
                if (LineUtils.isNullOrEmpty(fqpn)) {
                    throw new Exception("Tomcat's JNDI configuration does not specify a configuration file");
                }
            }

            returnVal = new ConfigFileReaderXml().read(fqpn);

        } catch (Exception e) {
            LOGGER.error("Error creating configuration POJO", e);
            throw (e);
        }


        return returnVal;

    } // make()

    public static void commitConfigValues(ConfigValues configValues, String fqpn) throws GobiiException {

        if(LineUtils.isNullOrEmpty(fqpn)) {
            throw new GobiiException("Cannot write config file without fqpn");
        }

        try {
            ConfigFileReaderXml configFileReaderXml = new ConfigFileReaderXml();
            configFileReaderXml.write(configValues, fqpn);

        } catch (Exception e) {

            throw new GobiiException("Error writing config file: " +
                    e.getMessage()
                    + ": " + e.getStackTrace());
        } // try/catch

    } //commitConfigValues()

}
