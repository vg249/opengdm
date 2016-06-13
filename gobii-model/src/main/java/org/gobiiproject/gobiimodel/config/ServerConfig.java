package org.gobiiproject.gobiimodel.config;

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
}
