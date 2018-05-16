// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.flex;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoRequestFlexTest {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    //
    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void testSucceedWithOneAuthentication() throws Exception {

        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @Test
    public void testGetVertices() throws Exception {
        RestUri restUriVertices = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_VERTICES);

        GobiiEnvelopeRestResource<VertexDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriVertices);
        PayloadEnvelope<VertexDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(VertexDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("No vertices were retrieved",
                resultEnvelope.getPayload().getData().size() > 0);
        VertexDTO vertexDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertTrue("The vertex does not have a vertexId",
                vertexDTO.getVertexId() > 0);
        Assert.assertFalse("The vertex name is empty",
                vertexDTO.getVertexName().isEmpty());
    }

}
