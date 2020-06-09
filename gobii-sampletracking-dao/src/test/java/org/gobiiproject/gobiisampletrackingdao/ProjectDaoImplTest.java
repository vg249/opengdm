/**
 * ProjectDaoImplTest.java
 * 
 * This test requires a test db.
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.modelmapper.CvMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/test-config.xml" })
@Slf4j
@Ignore
public class ProjectDaoImplTest {

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private ProjectDao v3ProjectDao;

    @Before
    public void init() {
        // TODO: insert mock data
    }

    private Project createProject(Integer contactId, String name, String description, List<CvPropertyDTO> props, String editor) throws Exception {
        Project project = new Project();
        
        Contact contact = contactDao.getContact(contactId);
        project.setContact(contact);
        project.setProjectName(name);
        project.setProjectDescription(description);

        //Project properties
        java.util.Map<String, String> prMap = CvMapper.mapCvIdToCvTerms(props);
        project.setProperties(prMap);

         //set new status
         List<Cv> cvList = cvDao.getCvs("modified", CvGroupTerm.CVGROUP_STATUS.getCvGroupName(), GobiiCvGroupType.GROUP_TYPE_SYSTEM);

        Cv cv = null;
        if (!cvList.isEmpty()) {
            cv = cvList.get(0);
        }
        project.setStatus(cv);

        Contact editorContact = contactDao.getContactByUsername(editor);
        project.setCreatedBy(editorContact.getContactId());
        project.setCreatedDate(new java.util.Date());

        return project;

    }

    @Test
    @Transactional
    public void testSimpleQuery() {
        assert v3ProjectDao != null;
        assert v3ProjectDao.getProjects(0, 1000, null) != null;
        // assert v3ProjectDao.getProjects(0, 1000).size() > 0;
    }

    @Test
    @Transactional
    public void testWithContactIdQuery() {
        assert v3ProjectDao != null;
        assert v3ProjectDao.getProjects(0, 1000, 4) != null;
        // assert v3ProjectDao.getProjects(0, 1000).size() > 0;
    }

    @Test
    @Transactional
    public void testCreateProject() throws Exception {
        List<Cv> cvList = cvDao.getCvs("new", CvGroupTerm.CVGROUP_STATUS.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM);
        assert cvList.size() > 0;

        Project project = v3ProjectDao.createProject(this.createProject(4, "test", "", null, "gadm"));
        assert project != null;
        assert project.getProjectId() > 0;
        assert project.getStatus().getCvId() == cvList.get(0).getCvId();
    }

    @Test
    @Transactional
    public void testCreateProjectWithProperties() throws Exception {
        java.util.List<CvPropertyDTO> cvProps = new java.util.ArrayList<>();

        CvPropertyDTO prop1 = new CvPropertyDTO();
        prop1.setPropertyId(4);
        prop1.setPropertyValue("test-value");
        cvProps.add(prop1);

        Project project = v3ProjectDao.createProject(this.createProject(4, "test", "test", cvProps, "gadm"));
        assert project != null;
        assert project.getProjectId() > 0;
        assert project.getProperties().size() == 1;
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    @Transactional
    @SuppressWarnings("unused")
    public void testDoubleCreateProject() throws Exception {
        Project project = v3ProjectDao.createProject(this.createProject(4, "test", "", null, "gadm"));
        Project project2 = v3ProjectDao.createProject(this.createProject(4, "test", "", null, "gadm"));
    }

    @Test
    @Transactional
    public void testGetProjectProperties() {
        List<Cv> cvs = v3ProjectDao.getCvProperties(0, 1000);
        assert cvs.size() > 0;
    }

    @Test
    @Transactional
    public void testPatchProject() throws Exception {
        List<CvPropertyDTO> props = new ArrayList<>();
        CvPropertyDTO dto =  new CvPropertyDTO();
        dto.setPropertyId(4);
        dto.setPropertyValue("test-value");
        props.add(dto);
        
        Project project = v3ProjectDao.createProject(this.createProject(4, "test", "", props, "gadm"));
        assert project.getProjectId() > 0;
        assert project.getProperties().size() > 0;
    
        

        //Get test
        project = v3ProjectDao.getProject(project.getProjectId());
        assert project != null;
        assert project.getProperties().size() > 0;
        log.debug("Props " + project.getProperties().get("4"));
        
        
        //create patch
        project.setContact(contactDao.getContact(5));
        project.setProjectDescription("test");
        
        project = v3ProjectDao.patchProject(project);
        log.debug("Project description: " + project.getProjectDescription());
        log.debug("Props " + project.getProperties());
        assert project.getProjectDescription().equals("test");
        assert project.getContact().getContactId() == 5;
        assert project.getProperties().get("4").equals("test-value");
        assert project.getProperties().size() > 0;

        //edit-test
        project.getProperties().put("4", "new-test-value");
        project = v3ProjectDao.patchProject(project); 
        assert project.getProperties().size() > 0;
        log.info(project.getProperties().get("4"));
        assert project.getProperties().get("4").equals("new-test-value");

        //delete test
        project.getProperties().remove("4");
        project = v3ProjectDao.patchProject(project); 
        assert project.getProperties().size() == 0; //property shoud be deleted

    }

}



