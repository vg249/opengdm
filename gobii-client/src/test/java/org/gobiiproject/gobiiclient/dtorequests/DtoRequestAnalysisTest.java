// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.Collectors;

public class DtoRequestAnalysisTest {


    @Test
    public void testAnalysisSet() throws Exception {


    	DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = new AnalysisDTO();
        analysisDTORequest.setAnalysisId(1);
        AnalysisDTO analysisDTOResponse = dtoRequestAnalysis.getAnalysisDetails(analysisDTORequest);

        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
        Assert.assertNotEquals(null, analysisDTOResponse.getProgram());

//        if (analysisDTOResponse.getA != null && analysisDTOResponse.getAnalyses().size() > 0) {
//            Assert.assertNotEquals(null, analysisDTOResponse.getAnalyses().get(0).getAnalysisId());
//            Assert.assertTrue(analysisDTOResponse.getAnalyses().get(0).getAnalysisId() > 0);
//        }
//        Assert.assertTrue(dataSetDTOResponse.getProperties().size() > 0);

    } // testGetMarkers()


}
