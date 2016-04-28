package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.junit.Assert;
import org.junit.Test;

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
}
