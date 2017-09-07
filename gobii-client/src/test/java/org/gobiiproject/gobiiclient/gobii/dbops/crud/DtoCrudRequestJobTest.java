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
import org.gobiiproject.gobiimodel.entity.PropNameId;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.JobDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by VCalaminos on 8/25/2017.
 */
public class DtoCrudRequestJobTest implements DtoCrudRequestTest {

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
        JobDTO newJobDto = new JobDTO();
        String uniqueStemString = UUID.randomUUID().toString();
        newJobDto.setJobName(uniqueStemString + ": job");
        newJobDto.setType(JobDTO.CV_JOBTYPE_LOAD);
        newJobDto.setPayloadType(JobDTO.CV_PAYLOADTYPE_SAMPLES);
        newJobDto.setStatus(JobDTO.CV_PROGRESSSTATUS_INPROGRESS);
        newJobDto.setMessage("dummy message");
        newJobDto.setSubmittedBy(1);

        RestUri statusUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(statusUri);
        PayloadEnvelope<JobDTO> payloadEnvelope = new PayloadEnvelope<>(newJobDto, GobiiProcessType.CREATE);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource
                .post(JobDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        JobDTO jobDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(jobDTOResponse, null);
        Assert.assertTrue(jobDTOResponse.getJobId() > 0);

    }

    @Test
    @Override
    public void get() throws Exception {

        RestUri restUriStatusForAll = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceForAll = new GobiiEnvelopeRestResource<>(restUriStatusForAll);
        PayloadEnvelope<JobDTO> resultEnvelopeForAll = gobiiEnvelopeRestResourceForAll
                .get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForAll.getHeader()));
        List<JobDTO> jobDTOList = resultEnvelopeForAll.getPayload().getData();
        Assert.assertNotNull(jobDTOList);
        Assert.assertTrue(jobDTOList.size() > 0);
        Assert.assertNotNull(jobDTOList.get(0).getJobName());

        String jobName = jobDTOList.get(0).getJobName();

        RestUri restUriStatus = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_JOB);
        restUriStatus.setParamValue("id", jobName);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriStatus);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource.get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        JobDTO jobDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(jobDTOResponse, null);
        Assert.assertTrue(jobDTOResponse.getJobName().equals(jobName));

    }


    @Test
    @Override
    public void update() throws Exception {

        RestUri restUriStatusForAll = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceForAll = new GobiiEnvelopeRestResource<>(restUriStatusForAll);
        PayloadEnvelope<JobDTO> resultEnvelopeForAll = gobiiEnvelopeRestResourceForAll
                .get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForAll.getHeader()));
        List<JobDTO> jobDTOList = resultEnvelopeForAll.getPayload().getData();
        Assert.assertNotNull(jobDTOList);
        Assert.assertTrue(jobDTOList.size() > 0);
        Assert.assertNotNull(jobDTOList.get(0).getJobName());

        JobDTO jobDTOReceived = jobDTOList.get(0);
        String jobName = jobDTOReceived.getJobName();

        String newMessage = "new message";
        jobDTOReceived.setMessage(newMessage);

        RestUri restUriStatusForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_JOB);
        restUriStatusForGetById.setParamValue("id", jobName);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceGetById = new GobiiEnvelopeRestResource<>(restUriStatusForGetById);

        PayloadEnvelope<JobDTO> postRequestEnvelope = new PayloadEnvelope<>(jobDTOReceived, GobiiProcessType.UPDATE);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResourceGetById
                .put(JobDTO.class, postRequestEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        JobDTO jobDtoRetrieved = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(jobDtoRetrieved.getJobName().equals(jobName));
        Assert.assertTrue(jobDtoRetrieved.getMessage().equals(newMessage));
    }

    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriStatus = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriStatus);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<JobDTO> jobDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(jobDTOList);
        Assert.assertTrue(jobDTOList.size() > 0);
        Assert.assertNotNull(jobDTOList.get(0).getJobName());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == jobDTOList.size());

        List<Integer> itemToTest = new ArrayList<>();
        if(jobDTOList.size() > 50) {
            itemToTest = TestUtils.makeListOfIntegersInRange(10, jobDTOList.size());
        } else {
            for (int idx = 0; idx < jobDTOList.size(); idx++) {
                itemToTest.add(idx);
            }
        }
        
        for (Integer currentIdx : itemToTest) {
            
            JobDTO currentJobDto = jobDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriStatusForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriStatusForGetById);
            PayloadEnvelope<JobDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(JobDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        }


    }

    @Ignore
    @Override
    public void testEmptyResult() throws Exception {

    }

}
