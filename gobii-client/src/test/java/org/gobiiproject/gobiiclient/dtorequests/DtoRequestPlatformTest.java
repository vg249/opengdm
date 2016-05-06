package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.*;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

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


    @Test
    public void testCreatePlatform() throws Exception {

        DtoRequestPlatform dtoRequestPlatform = new DtoRequestPlatform();

        PlatformDTO newPlatformDto = TestDtoFactory
                .makePopulatedPlatformDTO(DtoMetaData.ProcessType.CREATE, 1);

        PlatformDTO platformDTOResponse = dtoRequestPlatform.process(newPlatformDto);
        Assert.assertNotEquals(null, platformDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(platformDTOResponse));
        Assert.assertTrue(platformDTOResponse.getPlatformId() > 0);
    }


    @Test
    public void testUpdatePlatform() throws Exception {

        DtoRequestPlatform dtoRequestPlatform = new DtoRequestPlatform();

        // create a new platform for our test
        PlatformDTO newPlatformDto = TestDtoFactory
                .makePopulatedPlatformDTO(DtoMetaData.ProcessType.CREATE, 1);
        PlatformDTO newPlatformDTOResponse = dtoRequestPlatform.process(newPlatformDto);


        // re-retrieve the platform we just created so we start with a fresh READ mode dto
        PlatformDTO PlatformDTORequest = new PlatformDTO();
        PlatformDTORequest.setPlatformId(newPlatformDTOResponse.getPlatformId());
        PlatformDTO platformDTOReceived = dtoRequestPlatform.process(PlatformDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(platformDTOReceived));


        // so this would be the typical workflow for the client app
        platformDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newName = UUID.randomUUID().toString();
        platformDTOReceived.setPlatformName(newName);

        PlatformDTO PlatformDTOResponse = dtoRequestPlatform.process(platformDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(PlatformDTOResponse));

        PlatformDTO dtoRequestPlatformReRetrieved =
                dtoRequestPlatform.process(PlatformDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestPlatformReRetrieved));

        Assert.assertTrue(dtoRequestPlatformReRetrieved.getPlatformName().equals(newName));

    }
}
