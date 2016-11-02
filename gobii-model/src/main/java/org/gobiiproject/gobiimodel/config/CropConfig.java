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
    private String rawUserFilesDirectory;

    @Element(required = false)
    private String loaderInstructionFilesDirectory;

    @Element(required = false)
    private String extractorInstructionFilesDirectory;

    @Element(required = false)
    private String extractorInstructionFilesOutputDirectory;

    @Element(required = false)
    private String intermediateFilesDirectory;

    @Element
    private boolean isActive = false;

    @ElementMap(required = false)
    private Map<GobiiDbType, CropDbConfig> cropDbConfigsByDbType = new HashMap<>();

    @ElementList(required = false)
    private List<CropDbConfig> cropDbConfigForSerialization = new ArrayList<>();

    public CropConfig() {
//        this.cropDbConfigsByDbType.put(GobiiDbType.POSTGRESQL,new CropDbConfig(GobiiDbType.POSTGRESQL,null,null,null,null,null));
//        this.cropDbConfigsByDbType.put(GobiiDbType.MONETDB,new CropDbConfig(GobiiDbType.MONETDB,null,null,null,null,null));
//
//        this.cropDbConfigForSerialization.add(this.cropDbConfigsByDbType.get(GobiiDbType.POSTGRESQL));
//        this.cropDbConfigForSerialization.add(this.cropDbConfigsByDbType.get(GobiiDbType.MONETDB));

    }

    public CropConfig(String gobiiCropType,
                      String serviceDomain,
                      String serviceAppRoot,
                      Integer servicePort,
                      String loaderInstructionFilesDirectory,
                      String extractorInstructionFilesDirectory,
                      String extractorInstructionFilesOutputDirectory,
                      String rawUserFilesDirectory,
                      String intermediateFilesDirectory,
                      boolean isActive) {

        this.gobiiCropType = gobiiCropType;
        this.serviceDomain = serviceDomain;
        this.serviceAppRoot = serviceAppRoot;
        this.servicePort = servicePort;
        this.rawUserFilesDirectory = rawUserFilesDirectory;
        this.loaderInstructionFilesDirectory = loaderInstructionFilesDirectory;
        this.extractorInstructionFilesDirectory = extractorInstructionFilesDirectory;
        this.extractorInstructionFilesOutputDirectory = extractorInstructionFilesOutputDirectory;
        this.intermediateFilesDirectory = intermediateFilesDirectory;
        this.isActive = isActive;

//        this.cropDbConfigsByDbType.put(GobiiDbType.POSTGRESQL,new CropDbConfig());
//        this.cropDbConfigsByDbType.put(GobiiDbType.MONETDB,new CropDbConfig());
//
//        this.cropDbConfigForSerialization.add(this.cropDbConfigsByDbType.get(GobiiDbType.POSTGRESQL));
//        this.cropDbConfigForSerialization.add(this.cropDbConfigsByDbType.get(GobiiDbType.MONETDB));

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
            this.cropDbConfigForSerialization.add(cropDbConfig);

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

    public CropConfig setRawUserFilesDirectory(String rawUserFilesDirectory) {
        this.rawUserFilesDirectory = rawUserFilesDirectory;
        return this;
    }

    public CropConfig setLoaderInstructionFilesDirectory(String loaderInstructionFilesDirectory) {
        this.loaderInstructionFilesDirectory = loaderInstructionFilesDirectory;
        return this;
    }

    public CropConfig setExtractorInstructionFilesDirectory(String extractorInstructionFilesDirectory) {
        this.extractorInstructionFilesDirectory = extractorInstructionFilesDirectory;
        return this;
    }

    public CropConfig setExtractorInstructionFilesOutputDirectory(String extractorInstructionFilesOutputDirectory) {
        this.extractorInstructionFilesOutputDirectory = extractorInstructionFilesOutputDirectory;
        return this;
    }

    public CropConfig setIntermediateFilesDirectory(String intermediateFilesDirectory) {
        this.intermediateFilesDirectory = intermediateFilesDirectory;
        return this;
    }

    public CropConfig setCropDbConfigsByDbType(Map<GobiiDbType, CropDbConfig> cropDbConfigsByDbType) {
        this.cropDbConfigsByDbType = cropDbConfigsByDbType;
        return this;
    }

    public CropConfig setCropDbConfigForSerialization(List<CropDbConfig> cropDbConfigForSerialization) {
        this.cropDbConfigForSerialization = cropDbConfigForSerialization;
        return this;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public String getRawUserFilesDirectory() {
        return rawUserFilesDirectory;
    }

    public String getLoaderInstructionFilesDirectory() {
        return loaderInstructionFilesDirectory;
    }

    public String getExtractorInstructionFilesDirectory() {
        return extractorInstructionFilesDirectory;
    }

    public String getExtractorInstructionFilesOutputDirectory() {
        return extractorInstructionFilesOutputDirectory;
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
        cropDbConfigForSerialization.add(cropDbConfig);
    } // addCropDbConfig()

    public CropDbConfig getCropDbConfig(GobiiDbType gobiiDbType) {
        return cropDbConfigForSerialization
                .stream()
                .filter(d -> d.getGobiiDbType().equals(gobiiDbType))
                .collect(Collectors.toList())
                .get(0);
    } // getCropDbConfig()

}
