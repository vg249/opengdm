// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestReferenceTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetReference() throws Exception {

        DtoRequestReference dtoRequestReference = new DtoRequestReference();
        ReferenceDTO referenceDTORequest = new ReferenceDTO();
        referenceDTORequest.setReferenceId(1);
        ReferenceDTO referenceDTOResponse = dtoRequestReference.processReference(referenceDTORequest);

        Assert.assertNotEquals(null, referenceDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOResponse));

    } //


    @Test
    public void testCreateReference() throws Exception {

        DtoRequestReference dtoRequestReference = new DtoRequestReference();
        ReferenceDTO referenceDTORequest = new ReferenceDTO(DtoMetaData.ProcessType.CREATE);

        // set the plain properties
        referenceDTORequest.setName("dummy reference name");
        referenceDTORequest.setFilePath("C://pathy/dummy/path");
        referenceDTORequest.setLink("dummylink.com");
        referenceDTORequest.setVersion("version1");
        ReferenceDTO referenceDTOResponse = dtoRequestReference.processReference(referenceDTORequest);

        Assert.assertNotEquals(null, referenceDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOResponse));
        Assert.assertTrue(referenceDTOResponse.getReferenceId() > 0);

    }


    @Test
    public void testUpdateReference() throws Exception {
        DtoRequestReference dtoRequestReference = new DtoRequestReference();

        // create a new reference for our test
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        ReferenceDTO newReferenceDto = TestDtoFactory
                .makePopulatedReferenceDTO(DtoMetaData.ProcessType.CREATE, 1);
        ReferenceDTO newReferenceDTOResponse = dtoRequestReference.processReference(newReferenceDto);


        // re-retrieve the reference we just created so we start with a fresh READ mode dto
        ReferenceDTO ReferenceDTORequest = new ReferenceDTO();
        ReferenceDTORequest.setReferenceId(newReferenceDTOResponse.getReferenceId());
        ReferenceDTO referenceDTOReceived = dtoRequestReference.processReference(ReferenceDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOReceived));


        // so this would be the typical workflow for the client app
        referenceDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newDataFile = UUID.randomUUID().toString();
        referenceDTOReceived.setFilePath(newDataFile);

        ReferenceDTO ReferenceDTOResponse = dtoRequestReference.processReference(referenceDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(ReferenceDTOResponse));

        ReferenceDTO dtoRequestReferenceReRetrieved =
                dtoRequestReference.processReference(ReferenceDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestReferenceReRetrieved));

        Assert.assertTrue(dtoRequestReferenceReRetrieved.getFilePath().equals(newDataFile));

    }
}
