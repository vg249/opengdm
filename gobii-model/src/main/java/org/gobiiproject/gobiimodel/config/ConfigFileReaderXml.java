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
        File result = new File(fileName);

        serializer.write(configValues, result);

//        StringWriter stringWriter = new StringWriter();
//        JAXBContext carContext = JAXBContext.newInstance(ConfigValuesProps.class);
//        Marshaller marshaller = carContext.createMarshaller();
//        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
//        marshaller.marshal(configValues, stringWriter);
//        String xmlAsString = stringWriter.toString();
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
//        bufferedWriter.write(xmlAsString);
//        bufferedWriter.flush();
//        bufferedWriter.close();
    } // ConfigFileReaderProps
}
