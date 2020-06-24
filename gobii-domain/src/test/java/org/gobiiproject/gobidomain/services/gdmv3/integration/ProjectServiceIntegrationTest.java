package org.gobiiproject.gobidomain.services.gdmv3.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

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
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * This is an integration test class.  It must be run against a gobii test db instance.
 */
@WebAppConfiguration
@Transactional
public class ProjectServiceIntegrationTest {

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private CvDao cvDao;

    private ProjectServiceImpl projectServiceImpl;

    Random random = new Random();

    @Before
    public void init() {
        projectServiceImpl = new ProjectServiceImpl();
    }

    private Contact getRandomContact() {
        List<Contact> contacts = contactDao.getContacts(0, 1000, null);
        int size = contacts.size();
        return contacts.get(random.nextInt(size));
    }

    private List<Cv> getProjectProperties() {
        List<Cv> cvs = cvDao.getCvs(null, CvGroupTerm.CVGROUP_PROJECT_PROP.getCvGroupName(), GobiiCvGroupType.GROUP_TYPE_SYSTEM);
        return cvs;
    }

    @Test
    public void testCreateProjectWithNullValuedProperties() throws Exception {
        GobiiProjectRequestDTO request = new GobiiProjectRequestDTO();

        request.setProjectName(RandomStringUtils.random(20));
        request.setProjectDescription(RandomStringUtils.random(30));

        Contact contact = getRandomContact();
        assertNotNull("No contacts", contact);

        request.setPiContactId(contact.getContactId().toString());

        List<Cv> cvs = getProjectProperties();
        assertTrue("No project properties in db", cvs.size() > 0);

        //all null
        List<CvPropertyDTO> props = new ArrayList<>();
        cvs.forEach( cv -> {
            CvPropertyDTO cvPropertyDTO = new CvPropertyDTO();
            cvPropertyDTO.setPropertyId(cv.getCvId());
            cvPropertyDTO.setPropertyValue(null);
            props.add(cvPropertyDTO);
        });
        request.setProperties(props);

        GobiiProjectDTO result = projectServiceImpl.createProject(request, contact.getUsername());

        assertTrue("Properties should be empty", result.getProperties() == null || result.getProperties().size() == 0);
    }
    



    
}