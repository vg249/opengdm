// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class DtoRequestExperimentTest {

    @Test
    public void testGetExperimentDetailsByExperimentId() throws Exception {
        DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();

        ExperimentDTO experimentDTO = new ExperimentDTO();
        experimentDTO.setExperimentId(2);
        dtoRequestExperiment.process(experimentDTO);


        ExperimentDTO experimentDTOResponse = dtoRequestExperiment.process(experimentDTO);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(experimentDTOResponse));
        Assert.assertNotEquals(experimentDTOResponse, null);
        Assert.assertTrue(experimentDTOResponse.getExperimentId() > 0);

    } // testGetMarkers()

    @Test
    public void testCreateExperiment() throws Exception {

        DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();

        ExperimentDTO experimentDTORequest = new ExperimentDTO(DtoMetaData.ProcessType.CREATE);
        experimentDTORequest.setExperimentId(1);
        experimentDTORequest.setManifestId(1);
        experimentDTORequest.setPlatformId(1);
        experimentDTORequest.setProjectId(1);
        experimentDTORequest.setCreatedBy(2);
        experimentDTORequest.setModifiedBy(2);
        experimentDTORequest.setExperimentCode("foocode");
        experimentDTORequest.setExperimentDataFile("foofile");
        experimentDTORequest.setStatus(1);
        experimentDTORequest.setExperimentName(UUID.randomUUID().toString());

        dtoRequestExperiment.process(experimentDTORequest);

        ExperimentDTO experimentDTOResponse = dtoRequestExperiment.process(experimentDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(experimentDTOResponse));
        Assert.assertNotEquals(experimentDTOResponse, null);
        Assert.assertTrue(experimentDTOResponse.getExperimentId() > 0);

    } // testGetMarkers()


}
