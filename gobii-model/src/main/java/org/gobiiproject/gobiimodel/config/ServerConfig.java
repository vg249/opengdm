package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 6/10/2016.
 */
public class ServerConfig {

    public ServerConfig() {
    }

    public ServerConfig(CropConfig cropConfig,
                        String extractorInstructionsDir,
                        String loaderInstructionsDir,
                        String intermediateFilesDir,
                        String rawUserFilesDir) {

        this.port = cropConfig.getServicePort();
        this.domain = cropConfig.getServiceDomain();
        this.contextRoot = cropConfig.getServiceAppRoot();
        this.gobiiCropType = cropConfig.getGobiiCropType();

        fileLocations
                .put(GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS, extractorInstructionsDir);

        fileLocations
                .put(GobiiFileProcessDir.LOADER_INSTRUCTIONS, loaderInstructionsDir);

        fileLocations
                .put(GobiiFileProcessDir.INTERMEDIATE_FILES, intermediateFilesDir);

        fileLocations
                .put(GobiiFileProcessDir.RAW_USER_FILES, rawUserFilesDir);

    }

    @Element(required = false)
    private Integer port;

    @Element(required = false)
    private String domain;

    @Element(required = false)
    private String contextRoot;

    @Element(required = false)
    private String gobiiCropType;

    @Element
    private Map<GobiiFileProcessDir, String> fileLocations = new HashMap<>();

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public void setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }

    public Map<GobiiFileProcessDir, String> getFileLocations() {
        return fileLocations;
    }

    public void setFileLocations(Map<GobiiFileProcessDir, String> fileLocations) {
        this.fileLocations = fileLocations;
    }
}
