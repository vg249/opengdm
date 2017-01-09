package org.gobiiproject.gobiiclient.dtorequests.dbops.readonly;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.DataSetTypeDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 2017-01-05.
 */
public class DtoRequestDataSetTypeTest {

    @BeforeClass
    public static void setUpClass() throws Exception {

        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    @Test
    public void testGetDataSetTypes() throws Exception {

        RestUri restUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_DATASETTYPES);

        GobiiEnvelopeRestResource<DataSetTypeDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
        PayloadEnvelope<DataSetTypeDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(DataSetTypeDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<DataSetTypeDTO> dataSetTypeDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(dataSetTypeDTOList);
        Assert.assertTrue(dataSetTypeDTOList.size() > 0);
        Assert.assertNotNull(dataSetTypeDTOList.get(0).getDataSetTypeName());
    }

}
