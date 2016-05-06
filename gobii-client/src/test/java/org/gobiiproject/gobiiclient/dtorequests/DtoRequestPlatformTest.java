package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Phil on 4/27/2016.
 */
public class DtoRequestPlatformTest {

    @Test
    public void testGetPlatformDetails() throws Exception {
        DtoRequestPlatform dtoRequestPlatform = new DtoRequestPlatform();
        PlatformDTO PlatformDTORequest = new PlatformDTO();
        PlatformDTORequest.setPlatformId(1);
        PlatformDTO PlatformDTOResponse = dtoRequestPlatform.process(PlatformDTORequest);

        Assert.assertNotEquals(null, PlatformDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(PlatformDTOResponse));
        Assert.assertFalse(PlatformDTOResponse.getPlatformName().isEmpty());
        Assert.assertTrue(PlatformDTOResponse.getPlatformId() == 1);
        
    }


    @Ignore
    public void testCreatePlatform() throws Exception {

        DtoRequestPlatform dtoRequestPlatform = new DtoRequestPlatform();
        PlatformDTO platformDTORequest = new PlatformDTO(DtoMetaData.ProcessType.CREATE);

        // set the plain properties
        platformDTORequest.setStatus(1);
        platformDTORequest.setModifiedBy(1);
        platformDTORequest.setModifiedDate(new Date());
        platformDTORequest.setCreatedBy(1);
        platformDTORequest.setCreatedDate(new Date());
        platformDTORequest.setPlatformCode("dummy code");
        platformDTORequest.setPlatformDescription("dummy description");
        platformDTORequest.setPlatformName("New Platform");
        platformDTORequest.setPlatformVendor(1);
        platformDTORequest.setTypeId(1);

        PlatformDTO platformDTOResponse = dtoRequestPlatform.process(platformDTORequest);
        Assert.assertNotEquals(null, platformDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(platformDTOResponse));
        Assert.assertTrue(platformDTOResponse.getPlatformId() > 0);

    }
}
