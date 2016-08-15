package org.gobiiproject.gobiimodel.config;

import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.springframework.jndi.JndiTemplate;

import java.io.File;
import java.util.List;


/**
 * Created by Phil on 5/5/2016.
 */
public class ConfigSettings {

    private ConfigFileReaderProps fileReaderProps = null;

    public ConfigSettings() throws Exception {
        this(null);
    }

    ConfigValues configValues = null;

    public ConfigSettings(String configFileWebPath) throws Exception {

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

            String fileNameStem =  FilenameUtils.getPath(fqpn)
                    +  FilenameUtils.getBaseName(fqpn);

            String xmlFileEquivalent = fileNameStem + ".xml";
            File xmlFile = new File(xmlFileEquivalent);
            if (xmlFile.exists()) {

            } else {

                File propsFile = new File(fqpn);
                if (propsFile.exists()) {

                    ConfigValuesProps configValuesProps = new ConfigValuesProps(fqpn);

                    configFileReaderXml.write(configValuesProps, xmlFileEquivalent);

                    configValues = configFileReaderXml.read(xmlFileEquivalent);

                } else {
                    throw (new Exception("File does not exist: " + fqpn));
                }

            }
        } else {

        }

    } // ctor


    public CropConfig getCropConfig(GobiiCropType gobiiCropType) {

        return (this.configValues.getCropConfig(gobiiCropType));
    }

    public List<CropConfig> getActiveCropConfigs() {

        return (this.configValues.getActiveCropConfigs());

    }

    public CropConfig getCurrentCropConfig() {

        return this.configValues.getCurrentCropConfig();
    }


    public void setCurrentGobiiCropType(GobiiCropType currentGobiiCropType) {
        this.configValues.setCurrentGobiiCropType(currentGobiiCropType);

    }

    public GobiiCropType getCurrentGobiiCropType() {
        return this.configValues.getCurrentGobiiCropType();
    }

    public GobiiCropType getDefaultGobiiCropType() {
        return this.configValues.getDefaultGobiiCropType();
    }


    public void setDefaultGobiiCropType(GobiiCropType defaultGobiiCropType) {
        this.configValues.setDefaultGobiiCropType(defaultGobiiCropType);
    }

    public String getEmailSvrPassword() {

        return this.configValues.getEmailSvrPassword();
    }

    public String getEmailSvrHashType() {
        return this.configValues.getEmailSvrHashType();
    }

    public String getEmailSvrUser() {

        return this.configValues.getEmailSvrUser();
    }

    public String getEmailSvrDomain() {

        return this.configValues.getEmailSvrDomain();
    }

    public String getEmailSvrType() {

        return this.configValues.getEmailSvrType();
    }

    public Integer getEmailServerPort() {

        return this.configValues.getEmailServerPort();
    }

    public void setEmailServerPort(Integer emailServerPort) {
        this.configValues.setEmailServerPort(emailServerPort);
    }

    public boolean isIflIntegrityCheck() {
        return this.configValues.isIflIntegrityCheck();
    }

    public void setIflIntegrityCheck(boolean iflIntegrityCheck) {
        this.configValues.setIflIntegrityCheck(iflIntegrityCheck);
    }

    public String getFileSystemRoot() {

        return this.configValues.getFileSystemRoot();
    }

}
