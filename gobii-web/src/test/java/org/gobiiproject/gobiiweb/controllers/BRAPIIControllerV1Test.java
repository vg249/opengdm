package org.gobiiproject.gobiiweb.controllers;


import org.gobiiproject.gobidomain.security.UserContextLoader;
import org.gobiiproject.gobidomain.services.DnaRunService;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/application-config.xml"})
@WebAppConfiguration
public class BRAPIIControllerV1Test {

    //@Autowired
    //private WebApplicationContext wac;

    @Mock
    private DnaRunService dnaRunService;


    @InjectMocks
    private BRAPIIControllerV1 brapiiControllerV1;

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

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(brapiiControllerV1).build();

    }

    @Test
    public void getCallsets() throws Exception {

        List<DnaRunDTO> callsets = new ArrayList<>();

        DnaRunDTO callset1 = new DnaRunDTO();

        callset1.setCallSetDbId(34);
        callset1.setCallSetName("test");
        callset1.setDnaRunCode("test");
        callset1.setGermplasmDbId(4);

        callsets.add(callset1);

        when(
                dnaRunService.getDnaRuns(
                        any(Integer.TYPE), any(Integer.TYPE), any(DnaRunDTO.class))
        ).thenReturn(callsets);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/callsets"
                ).contextPath("/gobii-dev")).andDo(print()
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.metaData.pagination.pageSize").value(equals(1)));
    }


}
