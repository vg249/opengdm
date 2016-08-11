package org.gobiiproject.gobiimodel.config;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;



import java.io.File;

/**
 * Created by Phil on 4/12/2016.
 */
public class ConfigFileReaderXml {

    public void write(ConfigValues configValues, String fileName) throws Exception {

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);

        xmlMapper.writeValue(new File(fileName), configValues);
    }


} // ConfigFileReaderProps
