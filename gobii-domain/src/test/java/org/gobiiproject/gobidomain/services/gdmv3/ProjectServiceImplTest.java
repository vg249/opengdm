/**
 * V3ProjectServiceImplTest.java
 * 
 * Unit test for V3ProjectServiceImpl
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-07
 */
package org.gobiiproject.gobidomain.services.gdmv3;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectPatchDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class ProjectServiceImplTest {

    @Mock
    private ProjectDao projectDao;

    @Mock
    private CvDao cvDao;

    @Mock
    private ContactDao contactDao;

    @InjectMocks
    private ProjectServiceImpl v3ProjectServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSimple() {

        assert projectDao != null ;

        //Mock Cvs
        List<Cv> mockCvList = new java.util.ArrayList<>();
        when(cvDao.getCvListByCvGroup(CvGroupTerm.CVGROUP_PROJECT_PROP.getCvGroupName(), null))
        .thenReturn(
            mockCvList
        );

        List<Project> daoReturn = new java.util.ArrayList<>();
        Project mockEntity = new Project();
        mockEntity.setProjectName("PName");
        daoReturn.add(mockEntity);
        when(projectDao.getProjects(0,1000, null))
        .thenReturn(
            daoReturn
        );

        PagedResult<GobiiProjectDTO> payload = v3ProjectServiceImpl.getProjects(0,  1000, null);
        assert payload.getResult().size() == 1 ;
        assert payload.getCurrentPageNum() == 0;
        assert payload.getCurrentPageSize() == 1;
    }


    @Test
    public void testCreateWithNullProperties() throws Exception {
        //Setup test GobiiProject
        GobiiProjectRequestDTO request = new GobiiProjectRequestDTO();
        String projectName = RandomStringUtils.random(10);
        request.setProjectName(projectName);
        request.setProjectDescription(RandomStringUtils.random(20));
        request.setPiContactId("111");

        List<CvPropertyDTO> props = new ArrayList<>();
        CvPropertyDTO nullValued = new CvPropertyDTO();
        nullValued.setPropertyId(10);
        nullValued.setPropertyValue(null);
        props.add(nullValued);
        request.setProperties(props);
        
        
        Contact mockContact = new Contact();
        mockContact.setContactId(111);

        when(contactDao.getContact(111)).thenReturn(
            mockContact
        );

        Cv mockNewStat = new Cv();

        when(cvDao.getNewStatus()).thenReturn(mockNewStat);

        Contact mockCreator = new Contact();
        mockCreator.setContactId(123);

        when (contactDao.getContactByUsername("test-user")).thenReturn(mockCreator);

        ArgumentCaptor<Project> arg = ArgumentCaptor.forClass(Project.class);

        v3ProjectServiceImpl.createProject(request, "test-user");
        verify(projectDao).createProject(arg.capture());

        assertTrue(arg.getValue().getProperties().size() == 0);
        assertTrue(arg.getValue().getProjectName().equals(projectName));
    }

    @Test
    public void testCreateWithNonNullProperties() throws Exception {
        //Setup test GobiiProject
        GobiiProjectRequestDTO request = new GobiiProjectRequestDTO();
        String projectName = RandomStringUtils.random(10);
        request.setProjectName(projectName);
        request.setProjectDescription(RandomStringUtils.random(20));
        request.setPiContactId("111");

        List<CvPropertyDTO> props = new ArrayList<>();
        CvPropertyDTO valued = new CvPropertyDTO();
        valued.setPropertyId(10);
        valued.setPropertyValue("test");
        props.add(valued);
        request.setProperties(props);
        
        
        Contact mockContact = new Contact();
        mockContact.setContactId(111);

        when(contactDao.getContact(111)).thenReturn(
            mockContact
        );

        Cv mockNewStat = new Cv();

        when(cvDao.getNewStatus()).thenReturn(mockNewStat);

        Contact mockCreator = new Contact();
        mockCreator.setContactId(123);

        when (contactDao.getContactByUsername("test-user")).thenReturn(mockCreator);
        when (cvDao.getCvListByCvGroup(CvGroupTerm.CVGROUP_PROJECT_PROP.getCvGroupName(), null)).thenReturn(
            new ArrayList<Cv>()
        ); //this is just to avoid errors since we are only interested in the createProject args
        ArgumentCaptor<Project> arg = ArgumentCaptor.forClass(Project.class);

        v3ProjectServiceImpl.createProject(request, "test-user");
        verify(projectDao).createProject(arg.capture());

        assertTrue(arg.getValue().getProperties().size() == 1);

    }
   

    @Test
    public void testPatchProjectOk() throws Exception {
        Project mockProject = new Project();
        mockProject.setProjectId(123);
        mockProject.setProjectName("test-project");
        mockProject.setProjectDescription("test-description");
        

        Contact mockContact = new Contact();
        mockContact.setContactId(333);
        mockContact.setUsername("test-user");

        mockProject.setContact(mockContact);

        Contact mockNewContact  = new Contact();
        mockNewContact.setContactId(222);
        mockNewContact.setUsername("new-user");

        Contact mockEditor = new Contact();
        mockEditor.setContactId(444);
        mockEditor.setUsername("test-editor");

        Cv mockModifiedStatus = new Cv();
        mockModifiedStatus.setCvId(12);
        mockModifiedStatus.setTerm("modified");
  

        GobiiProjectPatchDTO request = new GobiiProjectPatchDTO();
        request.setPiContactId("222");
        request.setProjectName("new-project-name");
        request.setProjectDescription("new-project-description");

        when(projectDao.getProject(123)).thenReturn(mockProject);
        when(contactDao.getContact(222)).thenReturn(mockNewContact);
        when(contactDao.getContactByUsername("test-editor")).thenReturn(mockEditor);
        when(cvDao.getModifiedStatus()).thenReturn(mockModifiedStatus);

        ArgumentCaptor<Project> arg = ArgumentCaptor.forClass(Project.class);

        v3ProjectServiceImpl.patchProject(123, request, "test-editor");
        verify(projectDao).patchProject(arg.capture());

        Project param = arg.getValue();
        assertTrue(param.getProjectName().equals("new-project-name"));
        assertTrue(param.getProjectDescription().equals("new-project-description"));
        assertTrue(param.getContact().getContactId() == 222);

    }

    @Test
    public void testUpdateProperties() throws Exception {
        Project mockProject = new Project();
        mockProject.setProjectId(123);
        mockProject.setProjectName("test-project");
        mockProject.setProjectDescription("test-description");
        mockProject.setProperties(new HashMap<String, String>());

        Contact mockContact = new Contact();
        mockContact.setContactId(333);
        mockContact.setUsername("test-user");

        mockProject.setContact(mockContact);

        
        GobiiProjectPatchDTO request = new GobiiProjectPatchDTO();
        List<CvPropertyDTO> testProps = new ArrayList<>();
        CvPropertyDTO mockProp = new CvPropertyDTO();
        mockProp.setPropertyId(444);
        mockProp.setPropertyValue("test-value");
        testProps.add(mockProp);
        request.setProperties(testProps);

        Contact mockEditor = new Contact();
        mockEditor.setContactId(444);
        mockEditor.setUsername("test-editor");

        Cv mockModifiedStatus = new Cv();
        mockModifiedStatus.setCvId(12);
        mockModifiedStatus.setTerm("modified");

        when(projectDao.getProject(123)).thenReturn(mockProject);
        when(contactDao.getContactByUsername("test-editor")).thenReturn(mockEditor);
        when(cvDao.getModifiedStatus()).thenReturn(mockModifiedStatus);

        ArgumentCaptor<Project> arg = ArgumentCaptor.forClass(Project.class);

        v3ProjectServiceImpl.patchProject(123, request, "test-editor");
        verify(projectDao).patchProject(arg.capture());

        Project param = arg.getValue();
        assertTrue(param.getProperties().size() == 1);


        
    }
}