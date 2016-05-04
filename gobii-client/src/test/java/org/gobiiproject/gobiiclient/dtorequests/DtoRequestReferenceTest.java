// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DtoRequestReferenceTest {


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


}
