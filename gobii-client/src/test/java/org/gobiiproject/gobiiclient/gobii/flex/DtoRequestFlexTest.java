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

import java.util.ArrayList;
import java.util.Arrays;

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

    } // testGetVertices()

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

    } // testGetVerticesValues()


    @Test
    public void testGetVerticesMarkerSampleCount() throws Exception {

        String jobId = DateUtils.makeDateIdString() + "_test";
        RestUri restUriVerticesValuesCount = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_VERTICES)
                .addUriParam("jobId", jobId)
                .appendSegment(GobiiServiceRequestId.URL_COUNT);


        VertexFilterDTO vertexFilterDTO = new VertexFilterDTO();
        // *********************** destination vertex
        GobiiEntityNameType gobiiDestinationEntityNameTypeToTest = GobiiEntityNameType.PROJECT;
        VertexDTO destinationVertexDTO =
                new VertexDTO(
                        0,
                        null,
                        gobiiDestinationEntityNameTypeToTest,
                        null
                );

        vertexFilterDTO.setDestinationVertexDTO(destinationVertexDTO);


        // *********************** filter vertices -- these are totally bogus values to start with
        GobiiEntityNameType gobiiFilterEntityTypeF1 = GobiiEntityNameType.CONTACT;
        VertexDTO filterF1VertexDTO =
                new VertexDTO(
                        0,
                        null,
                        gobiiFilterEntityTypeF1,
                        null
                );
        filterF1VertexDTO.setFilterVals(new ArrayList<>(Arrays.asList(1,2,3)));

        GobiiEntityNameType gobiiFilterEntityTypeF2 = GobiiEntityNameType.DATASET;
        VertexDTO filterF2VertexDTO =
                new VertexDTO(
                        0,
                        null,
                        gobiiFilterEntityTypeF2,
                        null
                );
        filterF2VertexDTO.setFilterVals(new ArrayList<>(Arrays.asList(1,2,3)));

        vertexFilterDTO.getFilterVertices().add(filterF1VertexDTO);
        vertexFilterDTO.getFilterVertices().add(filterF2VertexDTO);




        GobiiEnvelopeRestResource<VertexFilterDTO> gobiiEnvelopeRestResourceContacts = new GobiiEnvelopeRestResource<>(restUriVerticesValuesCount);
        PayloadEnvelope<VertexFilterDTO> resultEnvelopeVertexFilter = gobiiEnvelopeRestResourceContacts.post(VertexFilterDTO.class,
                new PayloadEnvelope<>(vertexFilterDTO, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeVertexFilter.getHeader()));

        Assert.assertTrue("No vertex filter value was received",
                resultEnvelopeVertexFilter.getPayload().getData().size() > 0);

        VertexFilterDTO vertexFilterDTOReceived = resultEnvelopeVertexFilter.getPayload().getData().get(0);

        Assert.assertTrue("There is no marker count value",
                vertexFilterDTOReceived.getMarkerCount() > 0);

        Assert.assertTrue("There is no sample count value",
                vertexFilterDTOReceived.getSampleCount() > 0);

    } // testGetVerticesValues()

}
