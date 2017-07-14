package org.gobiiproject.gobiiclient.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiibrapi.core.common.BrapiStatus;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseDataList;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelope;
import org.gobiiproject.gobiiclient.core.brapi.BrapiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestAlleleMatrixSearch {

    private static TestExecConfig testExecConfig = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
        testExecConfig = new GobiiTestConfiguration().getConfigSettings().getTestExecConfig();

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    private String makeJobId() throws Exception {

        String returnVal = DateUtils.makeDateIdString();

        String cropTypeFromContext = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();

        // ************** DEFINE DTO
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();


        extractorInstructionFilesDTOToSend.setInstructionFileName(returnVal);


        // ************** INSTRUCTION ONE
        GobiiExtractorInstruction gobiiExtractorInstructionOne = new GobiiExtractorInstruction();
        gobiiExtractorInstructionOne.setContactId(1);
        gobiiExtractorInstructionOne.setGobiiCropType(cropTypeFromContext);

        // ************** DATA SET EXTRACT ONE
        GobiiDataSetExtract gobiiDataSetExtractOne = new GobiiDataSetExtract();
        gobiiDataSetExtractOne.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        GobiiFileType DataSetExtractOneFileType = GobiiFileType.HAPMAP;
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractOneFileType);
        String dataSetExtractOneName = "1my_foo_Dataset1";
        gobiiDataSetExtractOne.setDataSet(new GobiiFilePropNameId(1, dataSetExtractOneName));
        gobiiDataSetExtractOne.setAccolate(true);


        gobiiExtractorInstructionOne.getDataSetExtracts().add(gobiiDataSetExtractOne);

        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(gobiiExtractorInstructionOne);


        PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);

        GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForPost =
                new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory(GobiiControllerType.GOBII)
                        .resourceColl(GobiiServiceRequestId.URL_FILE_EXTRACTOR_INSTRUCTIONS));

        PayloadEnvelope<ExtractorInstructionFilesDTO> extractorInstructionFileDTOResponseEnvelope =
                gobiiEnvelopeRestResourceForPost.post(ExtractorInstructionFilesDTO.class,
                        payloadEnvelope);


        Assert.assertNotEquals(null, extractorInstructionFileDTOResponseEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(extractorInstructionFileDTOResponseEnvelope.getHeader()));
        // ************** NOW RETRIFVE THE FILE WE JUST CREATED AND MAKE SURE IT'S REALLY THERE, IMPLICITLY TESTING LINK

//        ExtractorInstructionFilesDTO extractorInstructionFilesDTOResult = extractorInstructionFileDTOResponseEnvelope
//                .getPayload()
//                .getData()
//                .get(0);
//
//        returnVal = extractorInstructionFilesDTOResult
//                .getGobiiExtractorInstructions()
//                .get(0)
//                .getDataSetExtracts()
//                .get(0)
//                .getExtractDestinationDirectory();

        return returnVal;
    }

    @Test
    public void getAlleleMatrix() throws Exception {

        // STEP ONE: SUBMIT AN EXTRACTION JOB SO THAT THE DESTINATION DIRECTORY GETS CREATED BY THE SERVER
        String jobId = this.makeJobId();
        Assert.assertNotNull("Job ID was not created",
                jobId);


        // STEP TWO: UPLOAD A FILE TO THE JOB'S EXTRACT DESTINATION DIRECTORY
        String testFileName = "ssr_allele_samples.txt";
        String resourcePath = "datasets/" + testFileName;
        ClassLoader classLoader = getClass().getClassLoader();
        File testResultFile = new File(classLoader.getResource(resourcePath).getFile());
        Assert.assertTrue("The specified test file does not exist: " + testResultFile.getAbsolutePath(),
                testResultFile.exists());


        // For the call to the service, we are supplying the artificial file name "DS1.hmp.txt"
        // But we have to do it so that
        // the extractor files service will "correctly" report
        // that the job is done because there is a file there
        RestUri restUriUpload = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .file(jobId, GobiiFileProcessDir.EXTRACTOR_OUTPUT, "DS1.hmp.txt");


        HttpMethodResult httpMethodResult = GobiiClientContext.getInstance(null, false)
                .getHttp()
                .upload(restUriUpload, testResultFile);

        Assert.assertTrue("Expected "
                        + HttpStatus.SC_OK
                        + " got: "
                        + httpMethodResult.getResponseCode()
                        + ": "
                        + httpMethodResult.getReasonPhrase() + ": " + httpMethodResult.getPlainPayload(),
                httpMethodResult.getResponseCode() == HttpStatus.SC_OK);


        // STEP THREE: MOVE THE JOB'S INSTRUCTION FILE TO THE DIRECTORY IT SHOULD BE IN IN ORDER
        // FOR THE STATUS TO BE REPORTED AS "FINISHED"
        // 3-A : Download the instruction file
        String instructionFileName = jobId + ".json";
        String tesetClientDestinationPath = testExecConfig
                .getTestFileDownloadDirectory() + "/" + instructionFileName;
        RestUri restUriForInstructionFileDownload = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .file(jobId, GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS, instructionFileName)
                .withDestinationFqpn(tesetClientDestinationPath);


        HttpMethodResult httpMethodResultFromDownload = GobiiClientContext.getInstance(null, false)
                .getHttp()
                .get(restUriForInstructionFileDownload);

        Assert.assertTrue("Expected "
                        + HttpStatus.SC_OK
                        + " got: "
                        + httpMethodResultFromDownload.getResponseCode()
                        + ": "
                        + httpMethodResultFromDownload.getReasonPhrase(),
                httpMethodResultFromDownload.getResponseCode() == HttpStatus.SC_OK);

        Assert.assertNotNull("File name is null",
                httpMethodResultFromDownload.getFileName());

        File downloadedInstructionFile = new File(httpMethodResultFromDownload.getFileName());

        // 3-A : UPLOAD THE FILE TO THE "FINISHED" DIRECTORY
        restUriUpload.setParamValue("destinationType",GobiiFileProcessDir.EXTRACTOR_DONE.toString());
        restUriUpload.setParamValue("fileName",instructionFileName);
        HttpMethodResult httpMethodResultUploadInstructionFile = GobiiClientContext.getInstance(null, false)
                .getHttp()
                .upload(restUriUpload, downloadedInstructionFile);

        Assert.assertTrue("Expected "
                        + HttpStatus.SC_OK
                        + " got: "
                        + httpMethodResultUploadInstructionFile.getResponseCode()
                        + ": "
                        + httpMethodResultUploadInstructionFile.getReasonPhrase() + ": " + httpMethodResultUploadInstructionFile.getPlainPayload(),
                httpMethodResultUploadInstructionFile.getResponseCode() == HttpStatus.SC_OK);




        // STEP FOUR: CALL ALLELE MATRICES SEARCH
        // /gobii-test/brapi/v1/allelematrix-search/status/2017_07_07_15_30_49
        RestUri restUriStudiesSearch = GobiiClientContext.getInstance(null, false)
                .getUriFactory(GobiiControllerType.BRAPI)
                .resourceColl(GobiiServiceRequestId.URL_ALLELE_MATRIX_SEARCH_STATUS)
                .addUriParam("jobId", jobId);

        //because of how BrapiEnvelopeRestResource is type-parameterized, we have to provide BrapiResponseDataList
        //or a class that is derived from it as the third parameter; in this case, we don't need it because this call
        //only provides metadata. This is not elegant.
        BrapiEnvelopeRestResource<ObjectUtils.Null, ObjectUtils.Null, BrapiResponseDataList> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<>(restUriStudiesSearch,
                        ObjectUtils.Null.class,
                        ObjectUtils.Null.class,
                        BrapiResponseDataList.class);

        BrapiResponseEnvelope searchResult = brapiEnvelopeRestResource.getMetaDataResponse();
        BrapiTestResponseStructure.validatateBrapiResponseStructure(searchResult.getBrapiMetaData());

        List<BrapiStatus> brapiStati = searchResult
                .getBrapiMetaData()
                .getStatus()
                .stream()
                .filter(s -> s.getCode().equals("asynchstatus"))
                .collect(Collectors.toList());

        Assert.assertTrue("The asynchstatus was not reported",
                brapiStati.size() > 0);


        Assert.assertTrue("The status of the job should be FINISHED",
                brapiStati.get(0).getMessage().equals("FINISHED"));

    }
}
