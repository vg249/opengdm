package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiimodel.headerlesscontainer.StatusDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.*;

/**
 * Created by VCalaminos on 8/25/2017.
 */
public class DtoCrudRequestStatusTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    @Test
    @Override
    public void get() throws Exception {

        RestUri restUriStatus = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_STATUS);
        GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriStatus);
        PayloadEnvelope<StatusDTO> resultEnvelope = gobiiEnvelopeRestResource.get(StatusDTO.class);

        Assert.assertTrue(resultEnvelope.getHeader().getStatus().getStatusMessages().get(0).getMessage().equals("Success"));

    }

    @Test
    @Override
    public void create() throws Exception {

        StatusDTO newStatusDto = TestDtoFactory
                    .makePopulatedStatusDTO();

        PayloadEnvelope<StatusDTO> payloadEnvelope = new PayloadEnvelope<>(newStatusDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_STATUS));

        PayloadEnvelope<StatusDTO> statusDTOResponseEnvelope = gobiiEnvelopeRestResource.post(StatusDTO.class,
                payloadEnvelope);

        Assert.assertTrue(statusDTOResponseEnvelope.getHeader().getStatus().getStatusMessages().get(0).getMessage().equals("Success"));
    }

    @Test
    @Override
    public void update() throws Exception {

        StatusDTO newStatusDto = TestDtoFactory
                .makePopulatedStatusDTO();

        RestUri restUriStatusForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_STATUS);
        restUriStatusForGetById.setParamValue("id", "1");
        GobiiEnvelopeRestResource<StatusDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriStatusForGetById);
        PayloadEnvelope<StatusDTO> resultEnvelopeForGetByID = restResourceForGetById.put(StatusDTO.class,
                new PayloadEnvelope<>(newStatusDto, GobiiProcessType.UPDATE));

        Assert.assertTrue(resultEnvelopeForGetByID.getHeader().getStatus().getStatusMessages().get(0).getMessage().equals("Success"));

    }

    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriStatus = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_STATUS);
        GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriStatus);
        PayloadEnvelope<StatusDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(StatusDTO.class);


        Assert.assertTrue(resultEnvelope.getHeader().getStatus().getStatusMessages().get(0).getMessage().equals("Success"));

    }

    @Ignore
    @Override
    public void testEmptyResult() throws Exception {

    }

}
