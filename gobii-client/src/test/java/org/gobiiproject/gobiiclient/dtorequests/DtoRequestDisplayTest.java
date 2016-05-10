// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import java.util.Date;
import java.util.UUID;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.junit.Assert;
import org.junit.Test;

public class DtoRequestDisplayTest {


    @Test
    public void testGetTableDisplayNamesWithColDisplay() throws Exception {

        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        DisplayDTO displayDTORequest = new DisplayDTO();
        displayDTORequest.getTableNamesWithColDisplay();


        DisplayDTO displayDTOResponse = dtoRequestDisplay.process(displayDTORequest);

        Assert.assertNotEquals(displayDTOResponse,null);
        Assert.assertTrue(displayDTOResponse.getTableNamesWithColDisplay().size() > 0);

    } // testGetMarkers()

    @Test
    public void testCreateDisplay() throws Exception {

        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        DisplayDTO referenceDTORequest = TestDtoFactory
                .makePopulatedDisplayDTO(DtoMetaData.ProcessType.CREATE, 1);

        // set the plain properties

        DisplayDTO referenceDTOResponse = dtoRequestDisplay.process(referenceDTORequest);

        Assert.assertNotEquals(null, referenceDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOResponse));
        Assert.assertTrue(referenceDTOResponse.getDisplayId() > 0);

    }


    @Test
    public void testUpdateDisplay() throws Exception {
        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        // create a new reference for our test
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        DisplayDTO newDisplayDto = TestDtoFactory
                .makePopulatedDisplayDTO(DtoMetaData.ProcessType.CREATE, 1);
        DisplayDTO newDisplayDTOResponse = dtoRequestDisplay.process(newDisplayDto);


        // re-retrieve the reference we just created so we start with a fresh READ mode dto
        DisplayDTO DisplayDTORequest = new DisplayDTO();
        DisplayDTORequest.setDisplayId(newDisplayDTOResponse.getDisplayId());
        DisplayDTO referenceDTOReceived = dtoRequestDisplay.process(DisplayDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOReceived));


        // so this would be the typical workflow for the client app
        referenceDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newName = UUID.randomUUID().toString();
        referenceDTOReceived.setTableName(newName);

        DisplayDTO DisplayDTOResponse = dtoRequestDisplay.process(referenceDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(DisplayDTOResponse));

        DisplayDTO dtoRequestDisplayReRetrieved =
                dtoRequestDisplay.process(DisplayDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestDisplayReRetrieved));

        Assert.assertTrue(dtoRequestDisplayReRetrieved.getTableName().equals(newName));
    }

}
