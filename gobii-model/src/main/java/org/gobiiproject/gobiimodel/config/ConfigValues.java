package org.gobiiproject.gobiimodel.config;

import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Phil on 5/5/2016.
 */
class ConfigValues {

//    @ElementList(required = false)
//    List<CropConfig> cropConfigsToSerialize = new ArrayList<>();

    @Element
    private TestExecConfig testExecConfig = new TestExecConfig();

    @ElementMap(required = false)
    private Map<String, CropConfig> cropConfigs = new LinkedHashMap<>();

    private String currentGobiiCropType;

    @Element(required = false)
    private String defaultGobiiCropType;

    @Element(required = false)
    private String emailSvrType;

    @Element(required = false)
    private String emailSvrDomain;

    @Element(required = false)
    private String emailSvrUser;

    @Element(required = false)
    private String emailSvrHashType;

    @Element(required = false)
    private String emailSvrPassword;

    @Element(required = false)
    private Integer emailServerPort = 0;

    @Element(required = false)
    private boolean iflIntegrityCheck = false;

    @Element(required = false)
    private String fileSystemRoot;

    @Element(required = false)
    private String fileSystemLog;

    public TestExecConfig getTestExecConfig() {
        return testExecConfig;
    }

    public void setTestExecConfig(TestExecConfig testExecConfig) {
        this.testExecConfig = testExecConfig;
    }


    public CropConfig getCropConfig(String gobiiCropType) {

        CropConfig returnVal = null;
        returnVal = getCropConfigs().get(gobiiCropType);

        return returnVal;
    }

    public List<CropConfig> getActiveCropConfigs() throws Exception {


        return getCropConfigs()
                .values()
                .stream()
                .filter(c -> c.isActive())
                .collect(Collectors.toList());
    }

    public CropConfig getCurrentCropConfig() {
        return getCropConfig(getCurrentGobiiCropType());
    }


    public void setCurrentGobiiCropType(String currentGobiiCropType) {
        this.currentGobiiCropType = currentGobiiCropType;

    }

    public String getCurrentGobiiCropType() {
        return currentGobiiCropType;
    }

    public String getDefaultGobiiCropType() {
        return defaultGobiiCropType;
    }


    public void setDefaultGobiiCropType(String defaultGobiiCropType) throws Exception {

        this.defaultGobiiCropType = defaultGobiiCropType;
    }

//    public List<CropConfig> getCropConfigsToSerialize() {
//        return cropConfigsToSerialize;
//    }

//    public void setCropConfigsToSerialize(List<CropConfig> cropConfigsToSerialize) {
//        this.cropConfigsToSerialize = cropConfigsToSerialize;
//    }

    public Map<String, CropConfig> getCropConfigs() {


        return this.cropConfigs;

//        if (0 == cropConfigs.size()) {
//            for (CropConfig currentCropConfig : cropConfigsToSerialize) {
//                cropConfigs.put(currentCropConfig.getGobiiCropType(), currentCropConfig);
//            }
//        }

        //return cropConfigs;
    }

    public void setCropConfigs(Map<String, CropConfig> cropConfigs) {
        this.cropConfigs = cropConfigs;
    }

    public void setCrop(String gobiiCropType,
                        String serviceDomain,
                        String serviceAppRoot,
                        Integer servicePort) {

        CropConfig cropConfig = this.getCropConfig(gobiiCropType);

        if (null == cropConfig) {
            cropConfig = new CropConfig();
            this.cropConfigs.put(gobiiCropType, cropConfig);
        }

        cropConfig
                .setGobiiCropType(gobiiCropType)
                .setServiceDomain(serviceDomain)
                .setServiceAppRoot(serviceAppRoot)
                .setServicePort(servicePort);
    }


    public String getEmailSvrType() {
        return emailSvrType;
    }

    public void setEmailSvrType(String emailSvrType) {
        this.emailSvrType = emailSvrType;
    }

    public String getEmailSvrDomain() {
        return emailSvrDomain;
    }

    public void setEmailSvrDomain(String emailSvrDomain) {
        this.emailSvrDomain = emailSvrDomain;
    }

    public String getEmailSvrUser() {
        return emailSvrUser;
    }

    public void setEmailSvrUser(String emailSvrUser) {
        this.emailSvrUser = emailSvrUser;
    }

    public String getEmailSvrHashType() {
        return emailSvrHashType;
    }

    public void setEmailSvrHashType(String emailSvrHashType) {
        this.emailSvrHashType = emailSvrHashType;
    }

    public String getEmailSvrPassword() {
        return emailSvrPassword;
    }

    public void setEmailSvrPassword(String emailSvrPassword) {
        this.emailSvrPassword = emailSvrPassword;
    }

    public Integer getEmailServerPort() {
        return emailServerPort;
    }

    public void setEmailSvrPort(Integer emailServerPort) {
        this.emailServerPort = emailServerPort;
    }

    public boolean isIflIntegrityCheck() {
        return iflIntegrityCheck;
    }

    public void setIflIntegrityCheck(boolean iflIntegrityCheck) {
        this.iflIntegrityCheck = iflIntegrityCheck;
    }

    public String getFileSystemRoot() {
        return fileSystemRoot;
    }

    public void setFileSystemRoot(String fileSystemRoot) {
        this.fileSystemRoot = fileSystemRoot;
    }

    public String getFileSystemLog() {
        return fileSystemLog;
    }

    public void setFileSystemLog(String fileSystemLog) {
        this.fileSystemLog = fileSystemLog;
    }
}
