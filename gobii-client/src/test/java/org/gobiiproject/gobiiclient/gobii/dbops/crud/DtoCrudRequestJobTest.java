package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

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
        JobDTO newJobDto = TestDtoFactory.makePopulateJobDTO();

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

    /* This unit test should test the creation of a matrix job with dataset ID is not specified
    *  The exception should be tested in this function
    * */
    @Ignore
    public void createFailMatrixJob() throws Exception {

        JobDTO newJobDto = TestDtoFactory.makePopulateJobDTO();

        newJobDto.setPayloadType(JobPayloadType.CV_PAYLOADTYPE_MATRIX.getCvName());

        RestUri jobUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(jobUri);
        PayloadEnvelope<JobDTO> payloadEnvelope = new PayloadEnvelope<>(newJobDto, GobiiProcessType.CREATE);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource
                .post(JobDTO.class, payloadEnvelope);

        System.out.print(resultEnvelope.getHeader().getStatus().getStatusMessages().get(0).getMessage());
        Assert.assertTrue("The error message should contain 'Missing dataset ID for job'",
                resultEnvelope.getHeader()
                    .getStatus()
                    .getStatusMessages()
                    .stream()
                    .filter(m -> m.getMessage().contains("Missing dataset ID for job"))
                    .count()
                    > 0);

        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);

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
                .resourceByUriIdParamName("jobName", GobiiServiceRequestId.URL_JOB);
        restUriStatus.setParamValue("jobName", jobName);
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
                .resourceByUriIdParamName("jobName", GobiiServiceRequestId.URL_JOB);
        restUriStatusForGetById.setParamValue("jobName", jobName);
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
    public void getJobByDataSetId() throws Exception {

        // create analysis for new dataset for this test

        RestUri namesUri = GobiiClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CV.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.NAMES_BY_TYPE_NAME.toString()));
        namesUri.setParamValue("filterValue", "analysis_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForAnalysisTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelopeAnalysis = gobiiEnvelopeRestResourceForAnalysisTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeAnalysis.getHeader()));
        List<NameIdDTO> analysisTypes = resultEnvelopeAnalysis.getPayload().getData();

        List<NameIdDTO> analysisProperTerm = new ArrayList<>(analysisTypes);
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(analysisProperTerm, 1);

        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<AnalysisDTO> payloadEnvelopeAnalysis = new PayloadEnvelope<>(analysisDTORequest, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceAnalysis = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));
        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelope = gobiiEnvelopeRestResourceAnalysis.post(AnalysisDTO.class,
                payloadEnvelopeAnalysis);
        AnalysisDTO callingAnalysisDTO = analysisDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelope.getHeader()));

        List<AnalysisDTO> analyses = new ArrayList<>();
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        2,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        3,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        4,
                        entityParamValues));

        List<Integer> analysisIds = new ArrayList<>();

        for (AnalysisDTO currentAnalysis : analyses) {

            payloadEnvelopeAnalysis = new PayloadEnvelope<>(currentAnalysis, GobiiProcessType.CREATE);
            gobiiEnvelopeRestResourceAnalysis = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));
            analysisDTOResponseEnvelope = gobiiEnvelopeRestResourceAnalysis.post(AnalysisDTO.class,
                    payloadEnvelopeAnalysis);
            AnalysisDTO createdAnalysis = analysisDTOResponseEnvelope.getPayload().getData().get(0);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelope.getHeader()));

            analysisIds.add(createdAnalysis.getAnalysisId());
        }


        // make new dataset
        DataSetDTO dataSetDTO = TestDtoFactory
                .makePopulatedDataSetDTO(1,
                        callingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        RestUri dataSetUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDataSetPost = new GobiiEnvelopeRestResource<>(dataSetUri);
        PayloadEnvelope<DataSetDTO> resultEnvelopeDataSet = gobiiEnvelopeRestResourceForDataSetPost
                .post(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTO, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeDataSet.getHeader()));

        DataSetDTO newDataSetDtoResponse = resultEnvelopeDataSet.getPayload().getData().get(0);
        Assert.assertTrue(newDataSetDtoResponse.getDataSetId() > 0);

        // create job

        JobDTO newJobDto = TestDtoFactory.makePopulateJobDTO();

        newJobDto.setPayloadType(JobPayloadType.CV_PAYLOADTYPE_MATRIX.getCvName());
        newJobDto.setDatasetId(newDataSetDtoResponse.getDataSetId());

        RestUri jobUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(jobUri);
        PayloadEnvelope<JobDTO> payloadEnvelope = new PayloadEnvelope<>(newJobDto, GobiiProcessType.CREATE);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource
                .post(JobDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        JobDTO createdJobDto = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotNull(createdJobDto.getJobId());

        RestUri restUriStatus = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParamName("jobName", GobiiServiceRequestId.URL_JOB);
        restUriStatus.setParamValue("jobName", createdJobDto.getJobName());
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriStatus);
        PayloadEnvelope<JobDTO> resultEnvelopeGet = gobiiEnvelopeRestResource.get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeGet.getHeader()));
        JobDTO jobDTOResponse = resultEnvelopeGet.getPayload().getData().get(0);

        Assert.assertNotNull(jobDTOResponse.getJobName());


        // retrieve job by datasetID

        RestUri restUriJobByDataSetID = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DATASETS)
                .addUriParam("datasetId")
                .setParamValue("datasetId", newDataSetDtoResponse.getDataSetId().toString())
                .appendSegment(GobiiServiceRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceByDatasetID = new GobiiEnvelopeRestResource<>(restUriJobByDataSetID);
        PayloadEnvelope<JobDTO> resultEnvelopeByDatasetID = gobiiEnvelopeRestResourceByDatasetID
                .get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeByDatasetID.getHeader()));
        JobDTO jobDTOResponseByDataSetID = resultEnvelopeByDatasetID.getPayload().getData().get(0);

        Assert.assertNotNull(jobDTOResponseByDataSetID.getJobName());
        Assert.assertTrue(jobDTOResponse.getJobName().equals(jobDTOResponseByDataSetID.getJobName()));


    }

    @Ignore
    @Override
    public void testEmptyResult() throws Exception {

    }

}
