package org.gobiiproject.gobiiweb.controllers;

import org.apache.commons.configuration2.ConfigurationUtils;
import org.gobiiproject.gobidomain.security.UserContextLoader;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/application-config.xml"})
public class SampleTrackingControllerTest {


    private MockMvc mockMvc;

    @BeforeClass
    public static void setupClass() {

        String CONFIG_FILE_LOCATION_PROP = "cfgFqpn";

        String configFileLocation = System.getProperty(CONFIG_FILE_LOCATION_PROP);

        ConfigSettings configSettings = new ConfigSettings(configFileLocation);
        TestExecConfig testExecConfig = configSettings.getTestExecConfig();
        UserContextLoader userContextLoader = new UserContextLoader();
        userContextLoader.loadUser(testExecConfig.getLdapUserForUnitTest());
    }

    @Test
    public void createProject() {
        assertTrue( 1 > 0);
    }


}
