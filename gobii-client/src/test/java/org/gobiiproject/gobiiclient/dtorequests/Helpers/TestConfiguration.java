package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class encapsulates the retrieval of configuration values necessary for
 * the test framework. It is _the_ only class in gobii-client that is allowed ot
 * read the configuration file directly. We require the rest of the framework
 * to get configuration data through the configuration web service: aside from
 * the test environment, we never ever want to create a direct dependency of
 * a client on a configuration file because we need configuration to remain
 * dynamic through central control in the server.
 */
public class TestConfiguration {

    private static Logger LOGGER = LoggerFactory.getLogger(TestConfiguration.class);

    private TestExecConfig testExecConfig = null;

    public TestExecConfig getTestExecConfig() {
        return testExecConfig;
    }

    private static String CONFIG_FILE_LOCATION_PROP = "cfgFqpn";

    public TestConfiguration() {

        String configFileLocation = System.getProperty(CONFIG_FILE_LOCATION_PROP);

        if (configFileLocation != null) {
            LOGGER.error("FQPN of configuration file read from environment variable " + CONFIG_FILE_LOCATION_PROP + ": " + configFileLocation);
        } else {
            LOGGER.error("The the environment does not define the FQPN of configuration in environment variable: " + CONFIG_FILE_LOCATION_PROP);
        }

        ConfigSettings configSettings = new ConfigSettings(configFileLocation);
        this.testExecConfig = configSettings.getTestExecConfig();
    }
}
