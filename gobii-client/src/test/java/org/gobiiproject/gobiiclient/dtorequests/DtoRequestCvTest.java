// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.junit.Assert;
import org.junit.Test;

public class DtoRequestCvTest {
 
	   @Test
	    public void testGetExperimentDetailsByExperimentId() throws Exception {
	        DtoRequestCv dtoRequestCv = new DtoRequestCv();

	        CvDTO cvDTO = new CvDTO();
	        cvDTO.setCv_id(2);
	        dtoRequestCv.geCvNames(cvDTO);


	        CvDTO cvDTORsponse = dtoRequestCv.geCvNames(cvDTO);

	        Assert.assertNotEquals(cvDTORsponse,null);

//	        Assert.assertNotEquals(null, nameIdListDTO);
//	        Assert.assertEquals(true, nameIdListDTO.getDtoHeaderResponse().isSucceeded());
//	        Assert.assertTrue(nameIdListDTO.getProjectNamesById().size() >= 0);

	    } // testGetMarkers()


}
