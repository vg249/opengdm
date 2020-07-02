package org.gobiiproject.gobidomain.services.gdmv3;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.CvGroup;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class CvServiceImplTest {
    @Mock
    private CvDao cvDao;

    @InjectMocks
    private CvServiceImpl cvServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCvNoProperties() throws Exception {
        CvDTO cvDTORequest = new CvDTO();
        cvDTORequest.setCvName("test-name");
        cvDTORequest.setCvDescription("test-desc");
        cvDTORequest.setCvGroupId(12);

        CvGroup mockCvGroup = new CvGroup();
        mockCvGroup.setCvGroupId(12);
        mockCvGroup.setCvGroupType(2);
        mockCvGroup.setCvGroupName("test-group");

        when(
            cvDao.getCvGroupById(12)
        ).thenReturn(mockCvGroup);

        Cv mockNewStatus = new Cv();
        mockNewStatus.setTerm("new");
        mockNewStatus.setCvId(57);

        when(
            cvDao.getNewStatus()
        ).thenReturn(mockNewStatus);


        when(
            cvDao.getCvListByCvGroup("cv_prop", null)
        ).thenReturn(
            new ArrayList<Cv>()
        );

        when(
            cvDao.createCv(any(Cv.class))
        ).thenReturn(new Cv());

        ArgumentCaptor<Cv> arg = ArgumentCaptor.forClass(Cv.class);
        
    
        CvDTO dto = cvServiceImpl.createCv(cvDTORequest);

        verify(cvDao).createCv(arg.capture());

        Cv result = arg.getValue();

        assertTrue("Result cv name incorrect", result.getTerm().equals("test-name"));
        assertTrue("Result cv description incorrect", result.getDefinition().equals("test-desc"));
        assertTrue("Result cv status incorrect", result.getStatus() == 57);
        assertTrue("Result cvGroup name incorrect", result.getCvGroup().getCvGroupName().equals("test-group"));
        assertTrue("Result cvGroup Type incorrect", result.getCvGroup().getCvGroupType() == 2);
        assertTrue("Result cvGroup Id incorrect", result.getCvGroup().getCvGroupId() == 12);
        assertTrue("Result properties not empty", result.getProperties() == null || result.getProperties().size() == 0);
    }

    @Test
    public void testCreateWithProperties() throws Exception {
        CvDTO cvDTORequest = new CvDTO();
        cvDTORequest.setCvName("test-name");
        cvDTORequest.setCvDescription("test-desc");
        cvDTORequest.setCvGroupId(12);

        List<CvPropertyDTO> properties = new ArrayList<>();
        CvPropertyDTO mockProp = new CvPropertyDTO();
        mockProp.setPropertyId(888);
        mockProp.setPropertyValue("SomeValue");
        properties.add(mockProp);
        cvDTORequest.setProperties(properties);

        CvGroup mockCvGroup = new CvGroup();
        mockCvGroup.setCvGroupId(12);
        mockCvGroup.setCvGroupType(2);
        mockCvGroup.setCvGroupName("test-group");

        Cv mockPropCv = new Cv();
        mockPropCv.setCvId(888);
        CvGroup mockPropCvGroup = new CvGroup();
        mockPropCvGroup.setCvGroupName(CvGroupTerm.CVGROUP_CV_PROP.getCvGroupName());
        mockPropCv.setCvGroup(mockPropCvGroup);

        when(cvDao.getCvByCvId(888)).thenReturn(mockPropCv);

        when(
            cvDao.getCvGroupById(12)
        ).thenReturn(mockCvGroup);

        Cv mockNewStatus = new Cv();
        mockNewStatus.setTerm("new");
        mockNewStatus.setCvId(57);

        when(
            cvDao.getNewStatus()
        ).thenReturn(mockNewStatus);


        when(
            cvDao.getCvListByCvGroup("cv_prop", null)
        ).thenReturn(
            new ArrayList<Cv>()
        );

        when(
            cvDao.createCv(any(Cv.class))
        ).thenReturn(new Cv());

        ArgumentCaptor<Cv> arg = ArgumentCaptor.forClass(Cv.class);
        
    
        CvDTO dto = cvServiceImpl.createCv(cvDTORequest);

        verify(cvDao).createCv(arg.capture());

        Cv result = arg.getValue();
        
        assertTrue("Result cv name incorrect", result.getTerm().equals("test-name"));
        assertTrue("Result cv description incorrect", result.getDefinition().equals("test-desc"));
        assertTrue("Result cv status incorrect", result.getStatus() == 57);
        assertTrue("Result cvGroup name incorrect", result.getCvGroup().getCvGroupName().equals("test-group"));
        assertTrue("Result cvGroup Type incorrect", result.getCvGroup().getCvGroupType() == 2);
        assertTrue("Result cvGroup Id incorrect", result.getCvGroup().getCvGroupId() == 12);
        assertTrue("Result properties size incorrect", result.getProperties() != null  && result.getProperties().size() == 1);
    }

    @Test
    public void testUpdateSimple() throws Exception {
        CvDTO cvDTORequest = new CvDTO();
        cvDTORequest.setCvName("test-name");

        Cv mockCv = new Cv();
        mockCv.setCvId(123);
        mockCv.setTerm("before-test");
        mockCv.setDefinition("before-test-definition");

        CvGroup mockCvGroup = new CvGroup();
        mockCvGroup.setCvGroupId(12);
        mockCvGroup.setCvGroupName("test-group");
        mockCvGroup.setCvGroupType(1);

        mockCv.setCvGroup(mockCvGroup);

        Cv mockCv2 = new Cv();
        mockCv2.setCvId(123);
        mockCv2.setTerm("test-name");
        mockCv2.setDefinition("before-test-definition");
        mockCv2.setCvGroup(mockCvGroup);
        

        when(cvDao.getCvByCvId(123)).thenReturn(mockCv);


        when(cvDao.updateCv(any(Cv.class))).thenReturn(mockCv2);

        Cv mockModStatus = new Cv();
        mockModStatus.setTerm("modified");
        mockModStatus.setCvId(57);

        when(cvDao.getModifiedStatus()).thenReturn(
            mockModStatus
        );

        ArgumentCaptor<Cv> arg = ArgumentCaptor.forClass(Cv.class);

        CvDTO updatedCvDTO = cvServiceImpl.updateCv(123, cvDTORequest);

        verify(cvDao).updateCv(arg.capture());

        Cv cvToUpdate = arg.getValue();

        assertTrue("test update failed", cvToUpdate.getTerm().equals("test-name")); //TODO: integ test better
    
    }

}