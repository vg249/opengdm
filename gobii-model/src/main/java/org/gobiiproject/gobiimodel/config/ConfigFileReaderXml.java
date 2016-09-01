package org.gobiiproject.gobiimodel.config;


import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

/**
 * Created by Phil on 4/12/2016.
 */
public class ConfigFileReaderXml {

    public void write(ConfigValues configValues, String fileName) throws Exception {

        Serializer serializer = new Persister();
        File file = new File(fileName);

        serializer.write(configValues, file);

    } // ConfigFileReaderProps

    public ConfigValues read(String fileName) throws Exception {

        ConfigValues returnVal = null;

        Serializer serializer = new Persister();
        File file = new File(fileName);

        returnVal = serializer.read(ConfigValues.class, file);

        return returnVal;


    }
}
