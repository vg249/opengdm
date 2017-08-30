package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.StatusDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

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

        Integer statusId = 1;

        RestUri restUriStatus = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_STATUS);
        restUriStatus.setParamValue("id", statusId.toString());
        GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriStatus);
        PayloadEnvelope<StatusDTO> resultEnvelope = gobiiEnvelopeRestResource.get(StatusDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        StatusDTO statusDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(statusDTOResponse, null);
        Assert.assertTrue(statusDTOResponse.getJobId() > 0);

    }

    @Test
    @Override
    public void create() throws Exception {

        StatusDTO newStatusDto = TestDtoFactory
                    .makePopulatedStatusDTO();

        RestUri statusUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_STATUS);
        GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(statusUri);
        PayloadEnvelope<StatusDTO> payloadEnvelope = new PayloadEnvelope<>(newStatusDto, GobiiProcessType.CREATE);
        PayloadEnvelope<StatusDTO> resultEnvelope = gobiiEnvelopeRestResource
                .post(StatusDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        StatusDTO statusDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(statusDTOResponse, null);
        Assert.assertTrue(statusDTOResponse.getJobId() > 0);

    }

    @Test
    @Override
    public void update() throws Exception {

        Integer statusId = 1;

        RestUri restUriStatusForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_STATUS);
        restUriStatusForGetById.setParamValue("id", statusId.toString());
        GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResourceGetById = new GobiiEnvelopeRestResource<>(restUriStatusForGetById);
        PayloadEnvelope<StatusDTO> resultEnvelope = gobiiEnvelopeRestResourceGetById
                .get(StatusDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        StatusDTO statusDTOReceived = resultEnvelope.getPayload().getData().get(0);

        String newMessage = "new message";
        statusDTOReceived.setMessages(newMessage);

        PayloadEnvelope<StatusDTO> postRequestEnvelope = new PayloadEnvelope<>(statusDTOReceived, GobiiProcessType.UPDATE);
        resultEnvelope = gobiiEnvelopeRestResourceGetById
                .put(StatusDTO.class, postRequestEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        StatusDTO statusDtoRetrieved = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(statusDtoRetrieved.getMessages().equals(newMessage));
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

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<StatusDTO> statusDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(statusDTOList);
        Assert.assertTrue(statusDTOList.size() > 0);
        Assert.assertNotNull(statusDTOList.get(0).getProcessStatus());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == statusDTOList.size());

        List<Integer> itemToTest = new ArrayList<>();
        if(statusDTOList.size() > 50) {
            itemToTest = TestUtils.makeListOfIntegersInRange(10, statusDTOList.size());
        } else {
            for (int idx = 0; idx < statusDTOList.size(); idx++) {
                itemToTest.add(idx);
            }
        }
        
        for (Integer currentIdx : itemToTest) {
            
            StatusDTO currentStatusDto = statusDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriStatusForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriStatusForGetById);
            PayloadEnvelope<StatusDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(StatusDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        }


    }

    @Ignore
    @Override
    public void testEmptyResult() throws Exception {

    }

}
