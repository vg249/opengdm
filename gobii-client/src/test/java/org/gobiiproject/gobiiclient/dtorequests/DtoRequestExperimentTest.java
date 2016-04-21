// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.junit.Assert;
import org.junit.Test;

public class DtoRequestExperimentTest {
 
	   @Test
	    public void testGetExperimentDetailsByExperimentId() throws Exception {
	        DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();

	        ExperimentDTO experimentDTO = new ExperimentDTO();
	        experimentDTO.setExperimentId(2);
	        dtoRequestExperiment.getExperiment(experimentDTO);


	        ExperimentDTO experimentDTOResponse = dtoRequestExperiment.getExperiment(experimentDTO);

	        Assert.assertNotEquals(experimentDTOResponse,null);

	        System.out.println(experimentDTOResponse.getExperimentName());
//	        Assert.assertNotEquals(null, nameIdListDTO);
//	        Assert.assertEquals(true, nameIdListDTO.getDtoHeaderResponse().isSucceeded());
//	        Assert.assertTrue(nameIdListDTO.getProjectNamesById().size() >= 0);

	    } // testGetMarkers()


}
