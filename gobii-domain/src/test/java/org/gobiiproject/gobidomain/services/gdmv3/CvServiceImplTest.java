package org.gobiiproject.gobidomain.services.gdmv3;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.gobiiproject.gobiimodel.dto.gdmv3.CvDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.CvGroup;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.junit.Before;
import org.junit.Test;
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

        CvDTO result = cvServiceImpl.createCv(cvDTORequest);

        assertTrue("Result cv name incorrect", result.getCvName().equals("test-name"));
        assertTrue("Result cv description incorrect", result.getCvDescription().equals("test-desc"));
        assertTrue("Result cv status incorrect", result.getCvStatus().equals("new"));
        assertTrue("Result cvGroup name incorrect", result.getCvGroupName().equals("test-group"));
        assertTrue("Result cvGroup Type incorrect", result.getCvGroupType().equals(CvDTO.PROPERTY_TYPE_CUSTOM));
        assertTrue("Result cvGroup Id incorrect", result.getCvGroupId() == 12);
        assertTrue("Result properties not empty", result.getProperties() == null || result.getProperties().size() == 0);
    }

}