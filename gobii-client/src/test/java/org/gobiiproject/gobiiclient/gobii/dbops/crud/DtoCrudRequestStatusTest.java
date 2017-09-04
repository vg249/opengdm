package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
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
    public void create() throws Exception {

        // make dummy statusDTO
        StatusDTO newStatusDto = new StatusDTO();

        // get cv ID for job_type group
        RestUri restUriForCv = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_CV);
        restUriForCv.setParamValue("id", StatusDTO.CVGROUP_JOBTYPE);

        GobiiEnvelopeRestResource<CvDTO> restResourceForCV = new GobiiEnvelopeRestResource<>(restUriForCv);
        PayloadEnvelope<CvDTO> resultEnvelopeForCV = restResourceForCV.get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForCV.getHeader()));
        List<CvDTO> cvDTOList = resultEnvelopeForCV.getPayload().getData();
        Assert.assertNotNull(cvDTOList);
        Assert.assertTrue(cvDTOList.size() >= 0);

        Integer typeId = null;

        for (CvDTO currentCvDTO : cvDTOList) {

            if (currentCvDTO.getTerm().toLowerCase().equals(StatusDTO.CV_JOBTYPE_LOAD)){
                typeId = currentCvDTO.getCvId();
                break;
            }

        }

        Assert.assertNotNull(typeId);

        newStatusDto.setTypeId(typeId);

        // get cv ID for payload_type group
        restUriForCv = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_CV);
        restUriForCv.setParamValue("id", StatusDTO.CVGROUP_PAYLOADTYPE);

        restResourceForCV = new GobiiEnvelopeRestResource<>(restUriForCv);
        resultEnvelopeForCV = restResourceForCV.get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForCV.getHeader()));
        cvDTOList = resultEnvelopeForCV.getPayload().getData();
        Assert.assertNotNull(cvDTOList);
        Assert.assertTrue(cvDTOList.size() >= 0);

        Integer payloadTypeId = null;

        for (CvDTO currentCvDTO : cvDTOList) {

            if (currentCvDTO.getTerm().toLowerCase().equals(StatusDTO.CV_PAYLOADTYPE_SAMPLES)){
                payloadTypeId = currentCvDTO.getCvId();
                break;
            }

        }

        Assert.assertNotNull(payloadTypeId);

        newStatusDto.setPayloadTypeId(payloadTypeId);

        // get cv ID for job_status group
        restUriForCv = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_CV);
        restUriForCv.setParamValue("id", StatusDTO.CVGROUP_JOBSTATUS);

        restResourceForCV = new GobiiEnvelopeRestResource<>(restUriForCv);
        resultEnvelopeForCV = restResourceForCV.get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForCV.getHeader()));
        cvDTOList = resultEnvelopeForCV.getPayload().getData();
        Assert.assertNotNull(cvDTOList);
        Assert.assertTrue(cvDTOList.size() >= 0);

        Integer statusId = null;

        for (CvDTO currentCvDTO : cvDTOList) {

            if (currentCvDTO.getTerm().toLowerCase().equals(StatusDTO.CV_PROGRESSSTATUS_PENDING)){
                statusId = currentCvDTO.getCvId();
                break;
            }

        }

        Assert.assertNotNull(statusId);

        newStatusDto.setStatus(statusId);
        newStatusDto.setMessage("dummy message");
        newStatusDto.setSubmittedBy(1);



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
    public void get() throws Exception {

        RestUri restUriStatusForAll = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_STATUS);
        GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResourceForAll = new GobiiEnvelopeRestResource<>(restUriStatusForAll);
        PayloadEnvelope<StatusDTO> resultEnvelopeForAll = gobiiEnvelopeRestResourceForAll
                .get(StatusDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForAll.getHeader()));
        List<StatusDTO> statusDTOList = resultEnvelopeForAll.getPayload().getData();
        Assert.assertNotNull(statusDTOList);
        Assert.assertTrue(statusDTOList.size() > 0);
        Assert.assertNotNull(statusDTOList.get(0).getJobId());

        Integer statusId = statusDTOList.get(0).getJobId();

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
    public void update() throws Exception {

        RestUri restUriStatusForAll = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_STATUS);
        GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResourceForAll = new GobiiEnvelopeRestResource<>(restUriStatusForAll);
        PayloadEnvelope<StatusDTO> resultEnvelopeForAll = gobiiEnvelopeRestResourceForAll
                .get(StatusDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForAll.getHeader()));
        List<StatusDTO> statusDTOList = resultEnvelopeForAll.getPayload().getData();
        Assert.assertNotNull(statusDTOList);
        Assert.assertTrue(statusDTOList.size() > 0);
        Assert.assertNotNull(statusDTOList.get(0).getJobId());

        StatusDTO statusDTOReceived = statusDTOList.get(0);
        Integer statusId = statusDTOReceived.getJobId();

        String newMessage = "new message";
        statusDTOReceived.setMessage(newMessage);

        RestUri restUriStatusForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_STATUS);
        restUriStatusForGetById.setParamValue("id", statusId.toString());
        GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResourceGetById = new GobiiEnvelopeRestResource<>(restUriStatusForGetById);

        PayloadEnvelope<StatusDTO> postRequestEnvelope = new PayloadEnvelope<>(statusDTOReceived, GobiiProcessType.UPDATE);
        PayloadEnvelope<StatusDTO> resultEnvelope = gobiiEnvelopeRestResourceGetById
                .put(StatusDTO.class, postRequestEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        StatusDTO statusDtoRetrieved = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(statusDtoRetrieved.getMessage().equals(newMessage));
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
        Assert.assertNotNull(statusDTOList.get(0).getStatus());

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
