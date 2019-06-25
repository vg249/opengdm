package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobidomain.security.UserContextLoader;
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
        UserContextLoader userContextLoader = new UserContextLoader();
        userContextLoader.loadUser("USER_READER");
    }

    @Test
    public void createProject() {
        assertTrue( 1 > 0);
    }


}
