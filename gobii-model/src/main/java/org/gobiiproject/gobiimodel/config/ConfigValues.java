package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import java.util.EnumMap;
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

    private final char PATH_TERMINATOR = '/';

    @Element
    private TestExecConfig testExecConfig = new TestExecConfig();

    @ElementMap(required = false)
    private Map<String, CropConfig> cropConfigs = new LinkedHashMap<>();

    @ElementMap(required = false)
    private Map<GobiiFileProcessDir, String> relativePaths = new EnumMap<GobiiFileProcessDir, String>(GobiiFileProcessDir.class) {{
        put(GobiiFileProcessDir.RAW_USER_FILES, "files/");
        put(GobiiFileProcessDir.LOADER_INSTRUCTIONS, "loader/instructions/");
        put(GobiiFileProcessDir.INTERMEDIATE_FILES, "loader/digest//");
        put(GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS, "extractor/instructions/");
        put(GobiiFileProcessDir.EXTRACTOR_OUTPUT, "extractor/output/");
        put(GobiiFileProcessDir.QC_NOTIFICATIONS, "qcnotifications/");

    }};

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
    private String fileSysCropsParent = "crops/";

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


    public String getProcessingPath(String cropType, GobiiFileProcessDir gobiiFileProcessDir) throws Exception {

        String returnVal;

        if (!cropConfigs.containsKey(cropType)) {
            throw new Exception("Unknown crop type: " + cropType);
        }

//        String root = LineUtils.terminateDirectoryPath(this.fileSystemRoot, PATH_TERMINATOR);
//        String parent = LineUtils.terminateDirectoryPath(this.fileSysCropsParent, PATH_TERMINATOR);

        String cropRoot = this.getFileSysCropsParent();
        String crop = LineUtils.terminateDirectoryPath(cropType, PATH_TERMINATOR);
        String relativePath = LineUtils.terminateDirectoryPath(relativePaths.get(gobiiFileProcessDir), PATH_TERMINATOR);

        returnVal = cropRoot + crop + relativePath;

        return returnVal;
    } //

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
                        boolean isActive,
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
                .setActive(isActive)
                .setServiceDomain(serviceDomain)
                .setServiceAppRoot(serviceAppRoot)
                .setServicePort(servicePort);
    }

    public void removeCrop(String cropId) throws Exception {

        if (!cropConfigs.containsKey(cropId)) {
            throw new Exception("The specified crop cannot be removed because it does not exist: " + cropId);
        }

        if ((!LineUtils.isNullOrEmpty(getDefaultGobiiCropType()))
                && getDefaultGobiiCropType().equals(cropId)) {

            throw new Exception("Unable to remove crop " + cropId + " because it is the default crop in this configuration");
        }

        if ((!LineUtils.isNullOrEmpty(getTestExecConfig().getTestCrop())) &&
                getTestExecConfig().getTestCrop().equals(cropId)) {

            throw new Exception("Unable to remove crop " + cropId + " because it is the crop used for testing");
        }

        cropConfigs.remove(cropId);

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

    public String getFileSysCropsParent() {

        String returnVal = LineUtils.terminateDirectoryPath(this.fileSystemRoot, PATH_TERMINATOR);
        returnVal  += LineUtils.terminateDirectoryPath(this.fileSysCropsParent, PATH_TERMINATOR);
        return  returnVal;
    }

    public void setFileSysCropsParent(String fileSysCropsParent) {
        this.fileSysCropsParent = fileSysCropsParent;
    }
}
