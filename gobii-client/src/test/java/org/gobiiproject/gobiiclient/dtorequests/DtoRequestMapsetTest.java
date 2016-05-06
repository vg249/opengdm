package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Phil on 4/27/2016.
 */
public class DtoRequestMapsetTest {

    @Test
    public void testGetMapsetDetails() throws Exception {
        DtoRequestMapset dtoRequestMapset = new DtoRequestMapset();
        MapsetDTO mapsetDTORequest = new MapsetDTO();
        mapsetDTORequest.setMapsetId(2);
        MapsetDTO mapsetDTOResponse = dtoRequestMapset.processMapset(mapsetDTORequest);

        Assert.assertNotEquals(null, mapsetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOResponse));
        Assert.assertFalse(mapsetDTOResponse.getName().isEmpty());
        Assert.assertTrue(mapsetDTOResponse.getMapsetId().equals(2));
        
    }


    @Test
    public void testCreateMapset() throws Exception {

        DtoRequestMapset dtoRequestMapset = new DtoRequestMapset();
        MapsetDTO mapsetDTORequest = new MapsetDTO(DtoMetaData.ProcessType.CREATE);

        // set the plain properties
        mapsetDTORequest.setName("dummy name");
        mapsetDTORequest.setCode("add dummy code");
        mapsetDTORequest.setCreatedBy(1);
        mapsetDTORequest.setCreatedDate(new Date());
        mapsetDTORequest.setDescription("dummy description");
        mapsetDTORequest.setMapType(1);
        mapsetDTORequest.setModifiedBy(1);
        mapsetDTORequest.setModifiedDate(new Date());
        mapsetDTORequest.setReferenceId(1);
        mapsetDTORequest.setStatus(1);


        MapsetDTO mapsetDTOResponse = dtoRequestMapset.processMapset(mapsetDTORequest);

        Assert.assertNotEquals(null, mapsetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOResponse));
        Assert.assertTrue(mapsetDTOResponse.getMapsetId() > 0);

    }
}
