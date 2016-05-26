package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 5/5/2016.
 */
public class CropConfig {


    private GobiiCropType gobiiCropType;
    private String serviceDomain;
    private String serviceAppRoot;
    private Integer servicePort;
    private String rawUserFilesDirectory;
    private String instructionFilesDirectory;
    private String intermediateFilesDirectory;
    private boolean isActive = false;
    private Map<GobiiDbType,CropDbConfig> dbConfigByDbType = new HashMap<>();

    public CropConfig(GobiiCropType gobiiCropType,
                      String serviceDomain,
                      String serviceAppRoot,
                      Integer servicePort,
                      String instructionFilesDirectory,
                      String rawUserFilesDirectory,
                      String intermediateFilesDirectory,
                      boolean isActive) {

        this.gobiiCropType = gobiiCropType;
        this.serviceDomain = serviceDomain;
        this.serviceAppRoot = serviceAppRoot;
        this.servicePort = servicePort;
        this.rawUserFilesDirectory = rawUserFilesDirectory;
        this.instructionFilesDirectory = instructionFilesDirectory;
        this.intermediateFilesDirectory = intermediateFilesDirectory;
        this.isActive = isActive;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public String getRawUserFilesDirectory() {
        return rawUserFilesDirectory;
    }

    public String getInstructionFilesDirectory() {
        return instructionFilesDirectory;
    }

    public String getIntermediateFilesDirectory() {
        return intermediateFilesDirectory;
    }

    public String getServiceDomain() {
        return serviceDomain;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getServiceAppRoot() {
        return serviceAppRoot;
    }

    public void setServiceAppRoot(String serviceAppRoot) {
        this.serviceAppRoot = serviceAppRoot;
    }

    public GobiiCropType getGobiiCropType() {
        return gobiiCropType;
    }

    public void setGobiiCropType(GobiiCropType gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }

    public void addCropDbConfig(GobiiDbType gobiiDbTypee, CropDbConfig cropDbConfig) {
        dbConfigByDbType.put(gobiiDbTypee,cropDbConfig);
    } // addCropDbConfig()

    public CropDbConfig getCropDbConfig(GobiiDbType gobiiDbType) {
        return dbConfigByDbType.get(gobiiDbType);
    } // getCropDbConfig()


}
