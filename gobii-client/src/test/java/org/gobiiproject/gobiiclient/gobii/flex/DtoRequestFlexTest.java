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
import org.gobiiproject.gobiimodel.dto.entity.auditable.ContactDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
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

    @Test
    public void testGetVerticesValues() throws Exception {

        String jobId = DateUtils.makeDateIdString() + "_test";
        RestUri restUriVerticesValues = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_VERTICES)
                .addUriParam("jobId", jobId)
                .appendSegment(GobiiServiceRequestId.URL_VALUES);


        GobiiEntityNameType gobiiEntityNameTypeToTest = GobiiEntityNameType.PROJECT;
        VertexFilterDTO vertexFilterDTO = new VertexFilterDTO();
        vertexFilterDTO.setDestinationVertexDTO(
                new VertexDTO(
                        0,
                        null,
                        gobiiEntityNameTypeToTest,
                        null
                )
        );

        GobiiEnvelopeRestResource<VertexFilterDTO> gobiiEnvelopeRestResourceContacts = new GobiiEnvelopeRestResource<>(restUriVerticesValues);
        PayloadEnvelope<VertexFilterDTO> resultEnvelopeVertexFilter = gobiiEnvelopeRestResourceContacts.post(VertexFilterDTO.class,
                new PayloadEnvelope<>(vertexFilterDTO, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeVertexFilter.getHeader()));

        Assert.assertTrue("No vertex filter value was received",
                resultEnvelopeVertexFilter.getPayload().getData().size() > 0);

        VertexFilterDTO vertexFilterDTOReceived = resultEnvelopeVertexFilter.getPayload().getData().get(0);

        Assert.assertTrue("No vertex values were received",
                vertexFilterDTOReceived.getVertexValues().size() > 0);

        Assert.assertTrue("Not all values have the correct entity types",
                vertexFilterDTOReceived.getVertexValues().size() ==
                        vertexFilterDTOReceived
                                .getVertexValues()
                                .stream()
                                .filter(vv -> vv.getGobiiEntityNameType().equals(gobiiEntityNameTypeToTest))
                                .count());

        Assert.assertTrue("Not all values have a non-null name",
                vertexFilterDTOReceived.getVertexValues().size() ==
                        vertexFilterDTOReceived
                                .getVertexValues()
                                .stream()
                                .filter(vv -> !vv.getName().isEmpty())
                                .count());
    }

}
