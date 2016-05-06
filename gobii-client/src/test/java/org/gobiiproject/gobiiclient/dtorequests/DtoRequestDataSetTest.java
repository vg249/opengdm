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
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestDataSetTest {


    @Test
    public void testGetDataSet() throws Exception {


        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = new DataSetDTO();
        dataSetDTORequest.setDataSetId(2);
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
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();

        DataSetDTO dataSetDTORequest = TestDtoFactory
                .makePopulatedDataSetDTO(DtoMetaData.ProcessType.CREATE,
                        1,
                        entityParamValues);

        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));
        Assert.assertTrue(dataSetDTOResponse.getDataSetId() > 0);
        Assert.assertNotEquals(null, dataSetDTOResponse.getCallingAnalysis());
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysis().getAnalysisId() > 0);
        Assert.assertNotEquals(null, dataSetDTOResponse.getAnalyses());
        Assert.assertTrue(dataSetDTOResponse.getAnalyses().size() > 0);
        Assert.assertTrue(dataSetDTOResponse.getAnalyses().get(0).getAnalysisId() > 0);

    }

    @Ignore
    public void UpdateDataSet() throws Exception {

        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();

        // create a new aataSet for our test
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        DataSetDTO newDataSetDto = TestDtoFactory
                .makePopulatedDataSetDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);
        DataSetDTO newDataSetDTOResponse = dtoRequestDataSet.process(newDataSetDto);


        // re-retrieve the aataSet we just created so we start with a fresh READ mode dto
        DataSetDTO dataSetDTORequest = new DataSetDTO();
        dataSetDTORequest.setDataSetId(newDataSetDTOResponse.getDataSetId());
        DataSetDTO dataSetDTOReceived = dtoRequestDataSet.process(dataSetDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOReceived));


        // so this would be the typical workflow for the client app
        dataSetDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newDataFile = UUID.randomUUID().toString();
        dataSetDTOReceived.setDataFile(newDataFile);


        AnalysisDTO newCallingAnalysisDTO = TestDtoFactory.makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                1,
                entityParamValues);
        AnalysisDTO newCollectionAnalysisDTO = TestDtoFactory.makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                2,
                entityParamValues);

        AnalysisDTO existingCallingAnalysisDTO = dataSetDTOReceived.getCallingAnalysis();
        dataSetDTOReceived.setCallingAnalysis(newCallingAnalysisDTO);

        AnalysisDTO existingAnalysisFromCollection = dataSetDTOReceived.getAnalyses().remove(0);
        dataSetDTOReceived.getAnalyses().add(newCollectionAnalysisDTO);

        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));

        DataSetDTO dtoRequestDataSetReRetrieved =
                dtoRequestDataSet.process(dataSetDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestDataSetReRetrieved));

        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataSetId() == dataSetDTORequest.getDataSetId());
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataFile().equals(newDataFile));
        Assert.assertNotEquals(null, dtoRequestDataSetReRetrieved.getCallingAnalysis().getAnalysisId());
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getCallingAnalysis().getAnalysisId()
                != existingCallingAnalysisDTO.getAnalysisId());
        Assert.assertNotEquals(null, dtoRequestDataSetReRetrieved.getAnalyses());
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getAnalyses().size() > 0);
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getAnalyses().get(0).getAnalysisId()
                != existingAnalysisFromCollection.getAnalysisId());

    }

}
