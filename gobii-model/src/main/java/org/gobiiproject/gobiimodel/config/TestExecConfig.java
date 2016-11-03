package org.gobiiproject.gobiimodel.config;

import org.simpleframework.xml.Element;

/**
 * Created by Phil on 11/2/2016.
 */
public class TestExecConfig {

    @Element(required = false)
    private String testCrop = "DEV";

    @Element(required = false)
    private String initialConfigUrl = "http://localhost:8282/gobii-dev";

    @Element(required = false)
    private String initialConfigUrlForSshOverride = "http://localhost:8080/gobii-dev";

    @Element(required = false)
    private String sshOverrideHost = "localhost";
    
    @Element(required = false)
    private Integer sshOverridePort = 8080;

    @Element(required = false)
    private boolean isTestSsh = false;

    @Element(required = false)
    private String configFileFqpn = "/gobii-config/gobii-web.properties";

    @Element(required = false)
    private String configFileTestDirectory = "/gobii-config-test";

    public String getTestCrop() {
        return testCrop;
    }

    public void setTestCrop(String testCrop) {
        this.testCrop = testCrop;
    }

    public String getInitialConfigUrl() {
        return initialConfigUrl;
    }

    public TestExecConfig setInitialConfigUrl(String initialConfigUrl) {
        this.initialConfigUrl = initialConfigUrl;
        return this;
    }

    public String getInitialConfigUrlForSshOverride() {
        return initialConfigUrlForSshOverride;
    }

    public TestExecConfig setInitialConfigUrlForSshOverride(String initialConfigUrlForSshOverride) {
        this.initialConfigUrlForSshOverride = initialConfigUrlForSshOverride;
        return this;
    }

    public String getSshOverrideHost() {
        return sshOverrideHost;
    }

    public TestExecConfig setSshOverrideHost(String sshOverrideHost) {
        this.sshOverrideHost = sshOverrideHost;
        return this;
    }

    public Integer getSshOverridePort() {
        return sshOverridePort;
    }

    public TestExecConfig setSshOverridePort(Integer sshOverridePort) {
        this.sshOverridePort = sshOverridePort;
        return this;
    }

    public boolean isTestSsh() {
        return isTestSsh;
    }

    public TestExecConfig setTestSsh(boolean testSsh) {
        isTestSsh = testSsh;
        return this;
    }

    public String getConfigFileFqpn() {
        return configFileFqpn;
    }

    public TestExecConfig setConfigFileFqpn(String configFileFqpn) {
        this.configFileFqpn = configFileFqpn;
        return this;
    }

    public String getConfigFileTestDirectory() {
        return configFileTestDirectory;
    }

    public TestExecConfig setConfigFileTestDirectory(String configFileTestDirectory) {
        this.configFileTestDirectory = configFileTestDirectory;
        return this;
    }
}
