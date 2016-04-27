// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

public class DtoRequestDataSetTest {


    @Test
    public void testGetDataSet() throws Exception {


        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = new DataSetDTO();
        dataSetDTORequest.setDatasetId(2);
        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));
        Assert.assertNotEquals(null, dataSetDTOResponse.getDataFile());

        if (dataSetDTOResponse.getAnalyses() != null && dataSetDTOResponse.getAnalyses().size() > 0) {
            Assert.assertNotEquals(null, dataSetDTOResponse.getAnalyses().get(0).getAnalysisId());
            Assert.assertTrue(dataSetDTOResponse.getAnalyses().get(0).getAnalysisId() > 0);
        }
//        Assert.assertTrue(dataSetDTOResponse.getProperties().size() > 0);

    } //


    @Test
    public void testCreateDataSet() throws Exception {

        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = new DataSetDTO(DtoMetaData.ProcessType.CREATE);


        // set the big-ticket items
        Integer analysisUniqueStem = 0;
        dataSetDTORequest.setCallingAnalysis(TestDtoFactory.makePopulatedAnalysisDTO(
                DtoMetaData.ProcessType.CREATE,
                ++analysisUniqueStem));

        dataSetDTORequest.getAnalyses().add(TestDtoFactory.makePopulatedAnalysisDTO(
                DtoMetaData.ProcessType.CREATE,
                ++analysisUniqueStem));
        dataSetDTORequest.getAnalyses().add(TestDtoFactory.makePopulatedAnalysisDTO(
                DtoMetaData.ProcessType.CREATE,
                ++analysisUniqueStem));
        dataSetDTORequest.getAnalyses().add(TestDtoFactory.makePopulatedAnalysisDTO(
                DtoMetaData.ProcessType.CREATE,
                ++analysisUniqueStem));

        dataSetDTORequest.getScores().add(1);
        dataSetDTORequest.getScores().add(2);
        dataSetDTORequest.getScores().add(3);

        // set the plain properties
        dataSetDTORequest.setStatus(1);
        dataSetDTORequest.setCreatedBy(1);
        dataSetDTORequest.setCreatedDate(new Date());
        dataSetDTORequest.setDataFile("foo file");
        dataSetDTORequest.setQualityFile("foo quality file");
        dataSetDTORequest.setExperimentId(2);
        dataSetDTORequest.setDataTable("foo table");
        dataSetDTORequest.setModifiedBy(1);
        dataSetDTORequest.setModifiedDate(new Date());

        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));
        Assert.assertTrue(dataSetDTOResponse.getDatasetId() > 0);
        Assert.assertNotEquals(null, dataSetDTOResponse.getCallingAnalysis());
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysis().getAnalysisId() > 0);
        Assert.assertNotEquals(null, dataSetDTOResponse.getAnalyses());
        Assert.assertTrue(dataSetDTOResponse.getAnalyses().size() > 0);
        Assert.assertTrue(dataSetDTOResponse.getAnalyses().get(0).getAnalysisId() > 0);

    }


}
