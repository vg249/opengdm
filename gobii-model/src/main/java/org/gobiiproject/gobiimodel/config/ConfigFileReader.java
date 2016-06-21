package org.gobiiproject.gobiimodel.config;

import org.apache.commons.lang.SystemUtils;
import org.gobiiproject.gobiimodel.utils.LineUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Phil on 4/12/2016.
 */
public class ConfigFileReader {


    private final String PROP_FILE_NAME = "/gobii.properties";
    private Properties webProperties = null;
    private final String FILE_LOC_PROP_PATH_WINDOWS = "configfileloc.windows";
    private final String FILE_LOC_PROP_PATH_LINUX = "configfileloc.linux";

    private Properties getWebProperties() throws Exception {

        if (null == webProperties) {

            InputStream locationPropFileStream = getClass().getResourceAsStream(PROP_FILE_NAME);

            if (null != locationPropFileStream) {
                Properties locationProperites = new Properties();
                locationProperites.load(locationPropFileStream);

                String configFileWebPath = SystemUtils.IS_OS_WINDOWS ?
                        locationProperites.getProperty(FILE_LOC_PROP_PATH_WINDOWS) :
                        locationProperites.getProperty(FILE_LOC_PROP_PATH_WINDOWS);

                File configFileWeb = new File(configFileWebPath);

                if (!LineUtils.isNullOrEmpty(configFileWebPath) || (null == configFileWeb) ) {
                    InputStream configFileWebStream = new FileInputStream(configFileWebPath);
                    if (null != configFileWebStream) {
                        webProperties = new Properties();
                        webProperties.load(configFileWebStream);
                    } else {
                        throw new Exception("Unable to create input stream for config file: " + configFileWebPath);
                    }

                } else {
                    throw new Exception("Config file for prp file location does not specify property: " + PROP_FILE_NAME);
                }

            } else {
                throw new Exception("Config file for prp file location not found: " + PROP_FILE_NAME);
            }
        }

        return webProperties;

    } // getWebProperties()

    public String getPropValue(String propName) throws Exception {

        String returnVal = getWebProperties().getProperty(propName);
        if (null == returnVal) {
            returnVal = ""; //prevent NPEs
        }

        return returnVal;

    } // getPropValue()

} // ConfigFileReader
