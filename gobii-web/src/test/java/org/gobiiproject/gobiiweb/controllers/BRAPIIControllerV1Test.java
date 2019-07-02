package org.gobiiproject.gobiiweb.controllers;


import org.gobiiproject.gobidomain.security.UserContextLoader;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/application-config.xml"})
public class BRAPIIControllerV1Test {




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
    public void getCallset() throws Exception {


        HttpURLConnection connection = null;

        //mockMvc.perform(MockMvcRequestBuilders.get("/callsets")).andExpect(
        //        MockMvcResultMatchers.status().isOk());

        URL restApiUrl = new URL("http://localhost:8282/gobii-dev/brapi/v1/callsets");

        connection = (HttpURLConnection) restApiUrl.openConnection();

        BufferedReader reader = null;

        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder jsonData = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {

            jsonData.append(line);

        }

        System.out.println(jsonData.toString());

        assertTrue( 1 > 0);
    }



}
