package org.gobiiproject.gobiimodel.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Phil on 4/12/2016.
 */
public class ConfigFileReader {


    private final String PROP_FILE_NAME = "/gobii.properties";
    private Properties properties = null;

    private Properties getProperties() throws IOException {

        if (null == properties) {

            InputStream inputStream = getClass().getResourceAsStream(PROP_FILE_NAME);
            properties = new Properties();
            properties.load(inputStream);

        }

        return properties;

    } // getProperties()

    public String getPropValue(String propName) throws IOException {

        return getProperties().getProperty(propName);

    } // getPropValue()

} // ConfigFileReader
