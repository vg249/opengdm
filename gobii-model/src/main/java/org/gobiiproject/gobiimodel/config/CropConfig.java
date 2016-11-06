package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/5/2016.
 */
@Root
public class CropConfig {


    @Element(required = false)
    private String gobiiCropType;

    @Element(required = false)
    private String serviceDomain;

    @Element(required = false)
    private String serviceAppRoot;

    @Element(required = false)
    private Integer servicePort;

    @Element(required = false)
    private boolean isActive;

    @ElementMap(required = false)
    private Map<GobiiDbType, CropDbConfig> cropDbConfigsByDbType = new HashMap<>();

    public CropConfig() {
    }

    public CropConfig(String gobiiCropType,
                      String serviceDomain,
                      String serviceAppRoot,
                      Integer servicePort,
//                      String loaderInstructionFilesDirectory,
//                      String extractorInstructionFilesDirectory,
//                      String extractorInstructionFilesOutputDirectory,
//                      String rawUserFilesDirectory,
//                      String intermediateFilesDirectory,
                      boolean isActive) {

        this.gobiiCropType = gobiiCropType;
        this.serviceDomain = serviceDomain;
        this.serviceAppRoot = serviceAppRoot;
        this.servicePort = servicePort;
//        this.rawUserFilesDirectory = rawUserFilesDirectory;
//        this.loaderInstructionFilesDirectory = loaderInstructionFilesDirectory;
//        this.extractorInstructionFilesDirectory = extractorInstructionFilesDirectory;
//        this.extractorInstructionFilesOutputDirectory = extractorInstructionFilesOutputDirectory;
//        this.intermediateFilesDirectory = intermediateFilesDirectory;
        this.isActive = isActive;

    }

    public void setCropDbConfig(GobiiDbType gobiiDbType,
                                String host,
                                String dbName,
                                Integer port,
                                String userName,
                                String password) {

        CropDbConfig cropDbConfig = this.cropDbConfigsByDbType.get(gobiiDbType);
        if (cropDbConfig == null) {

            cropDbConfig = new CropDbConfig();
            this.cropDbConfigsByDbType.put(gobiiDbType,cropDbConfig);

        }

        cropDbConfig
                .setGobiiDbType(gobiiDbType)
                .setHost(host)
                .setDbName(dbName)
                .setPort(port)
                .setUserName(userName)
                .setPassword(password);
    }

    public CropConfig setServiceDomain(String serviceDomain) {
        this.serviceDomain = serviceDomain;
        return this;
    }

    public CropConfig setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
        return this;
    }

//    public CropConfig setRawUserFilesDirectory(String rawUserFilesDirectory) {
//        this.rawUserFilesDirectory = rawUserFilesDirectory;
//        return this;
//    }
//
//    public CropConfig setLoaderInstructionFilesDirectory(String loaderInstructionFilesDirectory) {
//        this.loaderInstructionFilesDirectory = loaderInstructionFilesDirectory;
//        return this;
//    }
//
//    public CropConfig setExtractorInstructionFilesDirectory(String extractorInstructionFilesDirectory) {
//        this.extractorInstructionFilesDirectory = extractorInstructionFilesDirectory;
//        return this;
//    }
//
//    public CropConfig setExtractorInstructionFilesOutputDirectory(String extractorInstructionFilesOutputDirectory) {
//        this.extractorInstructionFilesOutputDirectory = extractorInstructionFilesOutputDirectory;
//        return this;
//    }
//
//    public CropConfig setIntermediateFilesDirectory(String intermediateFilesDirectory) {
//        this.intermediateFilesDirectory = intermediateFilesDirectory;
//        return this;
//    }

    public CropConfig setCropDbConfigsByDbType(Map<GobiiDbType, CropDbConfig> cropDbConfigsByDbType) {
        this.cropDbConfigsByDbType = cropDbConfigsByDbType;
        return this;
    }

    public Integer getServicePort() {
        return servicePort;
    }


    public String getServiceDomain() {
        return serviceDomain;
    }

    public boolean isActive() {
        return isActive;
    }

    public CropConfig setActive(boolean active) {
        isActive = active;
        return this;
    }

    public String getServiceAppRoot() {
        return serviceAppRoot;
    }

    public CropConfig setServiceAppRoot(String serviceAppRoot) {
        this.serviceAppRoot = serviceAppRoot;
        return this;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public CropConfig setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
        return this;
    }

    public void addCropDbConfig(GobiiDbType gobiiDbTypee, CropDbConfig cropDbConfig) {
        cropDbConfigsByDbType.put(gobiiDbTypee, cropDbConfig);

    } // addCropDbConfig()

    public CropDbConfig getCropDbConfig(GobiiDbType gobiiDbType) {
        CropDbConfig returnVal = this.cropDbConfigsByDbType.get(gobiiDbType);
        return returnVal;
    } // getCropDbConfig()

}
