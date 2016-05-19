package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiDbType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 5/5/2016.
 */
public class CropConfig {


    private String serviceDomain;
    private Integer servicePort;
    private String rawUserFilesDirectory;
    private String instructionFilesDirectory;
    private String intermediateFilesDirectory;
    private Map<GobiiDbType,CropDbConfig> dbConfigByDbType = new HashMap<>();

    public CropConfig(String serviceDomain,
                      Integer servicePort,
                      String instructionFilesDirectory,
                      String rawUserFilesDirectory,
                      String intermediateFilesDirectory) {

        this.serviceDomain = serviceDomain;
        this.servicePort = servicePort;
        this.rawUserFilesDirectory = rawUserFilesDirectory;
        this.instructionFilesDirectory = instructionFilesDirectory;
        this.intermediateFilesDirectory = intermediateFilesDirectory;
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


}
