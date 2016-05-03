// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.junit.Assert;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.junit.Test;

public class DtoRequestCvTest {
 
	   @Test
	    public void testGetCvDetailsByCvId() throws Exception {
	        DtoRequestCv dtoRequestCv = new DtoRequestCv();

	        CvDTO cvDTORequest = new CvDTO();
		   cvDTORequest.setcvId(2);


	        CvDTO cvDTOResponse = dtoRequestCv.process(cvDTORequest);

	        Assert.assertNotEquals(cvDTOResponse,null);
		    Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponse));

	        Assert.assertNotEquals(null, cvDTOResponse);
	        Assert.assertTrue(cvDTOResponse.getCvId() >= 0);

	    } // testGetMarkers()


}
