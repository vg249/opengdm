package org.gobiiproject.gobiiweb.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.gobiiproject.gobiidomain.services.gdmv3.ProjectService;
import org.gobiiproject.gobiidomain.services.gdmv3.ProtocolService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.controllers.gdm.v3.AuthUtils;
import org.gobiiproject.gobiiweb.controllers.gdm.v3.ProjectsController;
import org.gobiiproject.gobiiweb.controllers.gdm.v3.ProtocolsController;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("projectsController-test")
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PrepareForTest({CropRequestAnalyzer.class, AuthUtils.class})
@ContextConfiguration(classes = GOBIIControllerV3TestConfiguration.class
// locations = { "classpath:/spring/application-config.xml" }
)
@WebAppConfiguration
@Slf4j
public class ProtocolsControllerTest {
    @Mock
    private ProtocolService protocolService;

    private ProtocolsController protocolsController;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        log.info("Setting up Gobii V3 Projects Controller test");
        MockitoAnnotations.initMocks(this);

        this.protocolsController = new ProtocolsController( protocolService );

        this.mockMvc = MockMvcBuilders.standaloneSetup(protocolsController)
                .setControllerAdvice(new GlobalControllerExceptionHandler()).build();

        // assert this.projectsController.getProjectService() != null.

    }

    //TODO: missing protocol controller endpoint tests
}
