package org.gobiiproject.gobiiclient.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearch;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiiclient.core.brapi.BrapiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
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

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestAlleleMatrixSearch {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());

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

        String jobId = this.makeJobId();
        Assert.assertNotNull("Job ID was not created",
                jobId);


        String testFileName = "ssr_allele_samples.txt";
        String resourcePath = "datasets/" + testFileName;
        ClassLoader classLoader = getClass().getClassLoader();
        File testResultFile = new File(classLoader.getResource(resourcePath).getFile());


        RestUri restUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .file(jobId, GobiiFileProcessDir.EXTRACTOR_OUTPUT, testFileName);


        HttpMethodResult httpMethodResult = GobiiClientContext.getInstance(null, false)
                .getHttp()
                .upload(restUri, testResultFile);

        Assert.assertTrue("Expected "
                        + HttpStatus.SC_OK
                        + " got: "
                        + httpMethodResult.getResponseCode()
                        + ": "
                        + httpMethodResult.getReasonPhrase() + ": " + httpMethodResult.getPlainPayload(),
                httpMethodResult.getResponseCode() == HttpStatus.SC_OK);


//        RestUri restUriStudiesSearch = GobiiClientContext.getInstance(null, false)
//                .getUriFactory(GobiiControllerType.BRAPI)
//                .resourceColl(GobiiServiceRequestId.URL_STUDIES_SEARCH);
//
//        BrapiRequestStudiesSearch brapiRequestStudiesSearch = new BrapiRequestStudiesSearch();
//        brapiRequestStudiesSearch.setStudyType("genotype");
//
//        BrapiEnvelopeRestResource<BrapiRequestStudiesSearch,ObjectUtils.Null,BrapiResponseStudiesSearch> brapiEnvelopeRestResource =
//                new BrapiEnvelopeRestResource<BrapiRequestStudiesSearch,ObjectUtils.Null,BrapiResponseStudiesSearch>(restUriStudiesSearch,
//                        BrapiRequestStudiesSearch.class,
//                        ObjectUtils.Null.class,
//                        BrapiResponseStudiesSearch.class);
//
//        BrapiResponseEnvelopeMasterDetail<BrapiResponseStudiesSearch> studiesResult = brapiEnvelopeRestResource.postToListResource(brapiRequestStudiesSearch);
//
//        BrapiTestResponseStructure.validatateBrapiResponseStructure(studiesResult.getBrapiMetaData());
//
//        Assert.assertTrue(studiesResult.getResult().getData().size() > 0);
//
    }
}
