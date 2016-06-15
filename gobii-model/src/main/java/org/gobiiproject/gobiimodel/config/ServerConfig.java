package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiFileLocationTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 6/10/2016.
 */
public class ServerConfig {

    public ServerConfig() {}

    public ServerConfig(Integer port,
                        String domain,
                        String contextRoot) {
        this.port = port;
        this.domain = domain;
        this.contextRoot = contextRoot;
    }

    private Integer port;
    private String domain;
    private String contextRoot;
    private Map<GobiiFileLocationTypes,String> fileLocations = new HashMap<>();

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

    public Map<GobiiFileLocationTypes, String> getFileLocations() {
        return fileLocations;
    }

    public void setFileLocations(Map<GobiiFileLocationTypes, String> fileLocations) {
        this.fileLocations = fileLocations;
    }
}
