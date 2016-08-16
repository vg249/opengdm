package org.gobiiproject.gobiimodel.config;

import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.springframework.jndi.JndiTemplate;

import java.io.File;
import java.util.List;


/**
 * Created by Phil on 5/5/2016.
 */
class ConfigValuesFactory {

    public static ConfigValues make(String configFileWebPath) throws Exception {

        ConfigValues returnVal = null;

        String fqpn = configFileWebPath;

        if (LineUtils.isNullOrEmpty(fqpn)) {
            JndiTemplate jndi = new JndiTemplate();
            fqpn = (String) jndi.lookup("java:comp/env/gobiipropsloc");
            if (LineUtils.isNullOrEmpty(fqpn)) {
                throw new Exception("JNDI configuration does specify configuration file; see user manual for configuration instructions");
            }
        }

        ConfigFileReaderXml configFileReaderXml = new ConfigFileReaderXml();
        String extension = FilenameUtils.getExtension(fqpn);
        if (extension.equals("properties")) {

            String fileNameStem = FilenameUtils.getPath(fqpn)
                    + FilenameUtils.getBaseName(fqpn);

            String xmlFileEquivalent = fileNameStem + ".xml";
            File xmlFile = new File(xmlFileEquivalent);
            if (xmlFile.exists()) {
                returnVal = configFileReaderXml.read(xmlFileEquivalent);

            } else {

                File propsFile = new File(fqpn);
                if (propsFile.exists()) {

                    ConfigFileReaderProps configFileReaderProps = new ConfigFileReaderProps(fqpn);
                    ConfigValues configValues = configFileReaderProps.makeConfigValues();
                    configFileReaderXml.write(configValues, xmlFileEquivalent);
                    returnVal = configFileReaderXml.read(xmlFileEquivalent);

                } else {
                    throw (new Exception("File does not exist: " + fqpn));
                }

            }
        } else {

            returnVal = configFileReaderXml.read(fqpn);

        } // if else the file we got has ".properties" as extension


        return returnVal;

    } // make()

}
