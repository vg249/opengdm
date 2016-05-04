// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

public class DtoRequestAnalysisTest {

    @Test
    public void testAnalysisSet() throws Exception {


        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = new AnalysisDTO();
        analysisDTORequest.setAnalysisId(1);
        AnalysisDTO analysisDTOResponse = dtoRequestAnalysis.process(analysisDTORequest);

        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
        Assert.assertNotEquals(null, analysisDTOResponse.getProgram());


    }

    @Test
    public void testAnalysisCreate() throws Exception {

        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();


        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO analysisDTOResponse = dtoRequestAnalysis.process(analysisDTORequest);

        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
        Assert.assertTrue(analysisDTOResponse.getAnalysisId() > 1);

        Assert.assertNotEquals(null, analysisDTOResponse.getParameters());
        Assert.assertTrue(analysisDTOResponse.getParameters().size() > 0);
        Assert.assertTrue("Parameter values do not match",
                entityParamValues.compare(analysisDTOResponse.getParameters()));

    } // testAnalysisCreate

    @Ignore
    public void testUpdateAnalysis() throws Exception {

        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO AnalysisDTORequest = new AnalysisDTO();
        AnalysisDTORequest.setAnalysisId(2);
        AnalysisDTO AnalysisDTOReceived = dtoRequestAnalysis.process(AnalysisDTORequest);


        AnalysisDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);

        String newDataFile = UUID.randomUUID().toString();

        AnalysisDTOReceived.setSourceName(newDataFile);

        AnalysisDTO AnalysisDTOResponse = dtoRequestAnalysis.process(AnalysisDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(AnalysisDTOResponse));


        AnalysisDTO dtoRequestAnalysisAnalysisReRetrieved =
                dtoRequestAnalysis.process(AnalysisDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestAnalysisAnalysisReRetrieved));

        Assert.assertTrue(dtoRequestAnalysisAnalysisReRetrieved.getSourceName().equals(newDataFile));

    }

}
