package org.gobiiproject.gobiimodel.config;



import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/5/2016.
 */
public class ConfigSettings {

    private static Logger LOGGER = LoggerFactory.getLogger(ConfigSettings.class);

    public ConfigSettings() {
        try {
            configValues = ConfigValuesFactory.make(null);
        } catch( Exception e ) {
            LOGGER.error("Error instancing ConfigValues with null fqpn",e);
        }
    }

    ConfigValues configValues = null;

    public ConfigSettings(String configFileWebPath){

        try {
            configValues = ConfigValuesFactory.make(configFileWebPath);
        } catch(Exception e ) {
            LOGGER.error("Error instancing ConfigValues with fqpn: " + configFileWebPath,e);

        }
    } // ctor


    public CropConfig getCropConfig(String gobiiCropType) {

        return (this.configValues.getCropConfig(gobiiCropType));
    }

    public List<CropConfig> getActiveCropConfigs() {

        return (this.configValues.getActiveCropConfigs());

    }

    public List<String> getActiveCropTypes() {
        return this
                .configValues
                .getActiveCropConfigs()
                .stream()
                .filter(c -> c.isActive() == true )
                .map(CropConfig::getGobiiCropType)
                .collect(Collectors.toList());
    }

    public CropConfig getCurrentCropConfig() {

        return this.configValues.getCurrentCropConfig();
    }


    public void setCurrentGobiiCropType(String currentGobiiCropType) {
        this.configValues.setCurrentGobiiCropType(currentGobiiCropType);

    }

    public String getCurrentGobiiCropType() {
        return this.configValues.getCurrentGobiiCropType();
    }

    public String getDefaultGobiiCropType() {
        return this.configValues.getDefaultGobiiCropType();
    }


    public void setDefaultGobiiCropType(String defaultGobiiCropType) {
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
        this.configValues.setEmailSvrPort(emailServerPort);
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

    public String getFileSystemLog() {
        return this.configValues.getFileSystemLog();
    }

    public void setFileSystemLog(String fileSystemLog) {
        this.configValues.setFileSystemLog(fileSystemLog);
    }


}
