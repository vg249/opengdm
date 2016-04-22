// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.Collectors;

public class DtoRequestDataSetTest {


    @Test
    public void testGetDataSet() throws Exception {


        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = new DataSetDTO();
        dataSetDTORequest.setDatasetId(2);
        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.getDataSetDetails(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));
        Assert.assertNotEquals(null, dataSetDTOResponse.getDataFile());
//        Assert.assertTrue(dataSetDTOResponse.getProperties().size() > 0);

    } // testGetMarkers()



}
