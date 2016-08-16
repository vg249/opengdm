package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiCropType;

import java.util.List;


/**
 * Created by Phil on 5/5/2016.
 */
public class ConfigSettings {

    public ConfigSettings() throws Exception {
        configValues = ConfigValuesFactory.make(null);
    }

    ConfigValues configValues = null;

    public ConfigSettings(String configFileWebPath) throws Exception {

        configValues = ConfigValuesFactory.make(configFileWebPath);
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

}
