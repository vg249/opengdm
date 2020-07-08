package org.gobiiproject.gobiiweb.controllers.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobidomain.services.PropertiesService;
import org.gobiiproject.gobidomain.services.gdmv3.ProjectServiceImpl;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.ContactDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.gobiiproject.gobiiweb.controllers.GOBIIControllerV3;
import org.gobiiproject.gobiiweb.controllers.GlobalControllerExceptionHandler;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * This is an integration test class.  It must be run against a gobii test db instance.
 * @Transactional will not work here so make sure to delete anything we create 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    locations = { "classpath:/spring/application-config.xml" }
)
@WebAppConfiguration
@Slf4j
@Ignore  //ignoring this for now -- TODO: solve the DataSourceSelector problem
public class ProjectServiceIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ContactDao contactDao;


    private MockMvc mockMvc;

    Random random = new Random();

    @Before
    public void setup() throws Exception {
        log.info("Setting up Gobii V3 Controller test");

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        //assert this.projectsController.getProjectService() != null.
    }

    private Contact getRandomContact() {
        List<Contact> contacts = contactDao.getContacts(0, 1000, null);
        int size = contacts.size();
        Contact contact = contacts.get(random.nextInt(size));
        return contact;
    }

    @Test
    public void testCreateProjectWithNullProperties() throws Exception {
        GobiiProjectDTO request = new GobiiProjectDTO();

        request.setProjectName(RandomStringUtils.random(10));
        request.setProjectDescription(RandomStringUtils.random(20));

        //get contacts?
        Contact contact = this.getRandomContact();
        assertNotNull("Contact is null", contact);

        //get properties
        List<Cv> cvs = cvDao.getCvs(null, CvGroupTerm.CVGROUP_PROJECT_PROP.getCvGroupName(), GobiiCvGroupType.GROUP_TYPE_SYSTEM);
        assertTrue("No cvs available", cvs != null && cvs.size() > 0 );

        List<CvPropertyDTO> props = new ArrayList<>();
        cvs.forEach(cv -> {
            CvPropertyDTO cvPropertyDTO = new CvPropertyDTO();
            cvPropertyDTO.setPropertyId(cv.getCvId());
            cvPropertyDTO.setPropertyValue(null);
        });

        request.setProperties(props); //all null
        request.setPiContactId(contact.getContactId());

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(request);
        
        this.mockMvc.perform(post("/gobii/v3/projects")
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isCreated());

    }



    
}