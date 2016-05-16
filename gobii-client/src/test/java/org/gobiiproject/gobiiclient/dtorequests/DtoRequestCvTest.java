// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

public class DtoRequestCvTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }



    @Test
    public void testGetCvDetailsByCvId() throws Exception {
        DtoRequestCv dtoRequestCv = new DtoRequestCv();

        CvDTO cvDTORequest = new CvDTO();
        cvDTORequest.setCvId(2);
        cvDTORequest.setIncludeDetailsList(true);


        CvDTO cvDTOResponse = dtoRequestCv.process(cvDTORequest);

        Assert.assertNotEquals(cvDTOResponse, null);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponse));

        Assert.assertNotEquals(null, cvDTOResponse);
        Assert.assertTrue(cvDTOResponse.getCvId() >= 0);
        Assert.assertTrue(cvDTOResponse.getGroupCvItems().size() > 0);

    } // testGetMarkers()


    @Test
    public void testCreateCv() throws Exception {

        DtoRequestCv dtoRequestCv = new DtoRequestCv();
        CvDTO cvDTORequest = TestDtoFactory
                .makePopulatedCvDTO(DtoMetaData.ProcessType.CREATE, 1);
        // set the plain properties

        CvDTO cvDTOResponse = dtoRequestCv.process(cvDTORequest);

        Assert.assertNotEquals(null, cvDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponse));
        Assert.assertTrue(cvDTOResponse.getCvId() > 0);

    }


    @Test
    public void testUpdateCv() throws Exception {
        DtoRequestCv dtoRequestCv = new DtoRequestCv();

        // create a new cv for our test
        CvDTO newCvDto = TestDtoFactory
                .makePopulatedCvDTO(DtoMetaData.ProcessType.CREATE, 1);
        CvDTO newCvDTOResponse = dtoRequestCv.process(newCvDto);


        // re-retrieve the cv we just created so we start with a fresh READ mode dto
        CvDTO CvDTORequest = new CvDTO();
        CvDTORequest.setCvId(newCvDTOResponse.getCvId());
        CvDTO cvDTOReceived = dtoRequestCv.process(CvDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOReceived));


        // so this would be the typical workflow for the client app
        cvDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newName = UUID.randomUUID().toString();
        cvDTOReceived.setGroup(newName);

        CvDTO CvDTOResponse = dtoRequestCv.process(cvDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(CvDTOResponse));

        CvDTO dtoRequestCvReRetrieved =
                dtoRequestCv.process(CvDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestCvReRetrieved));

        Assert.assertTrue(dtoRequestCvReRetrieved.getGroup().equals(newName));

    }
}
