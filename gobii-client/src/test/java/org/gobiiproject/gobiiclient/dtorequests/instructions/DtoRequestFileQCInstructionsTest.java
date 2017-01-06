package org.gobiiproject.gobiiclient.dtorequests.instructions;

import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestConfiguration;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class DtoRequestFileQCInstructionsTest {

    private static UriFactory uriFactory;


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        uriFactory = new UriFactory(currentCropContextRoot);
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void create() throws Exception {

        QCInstructionsDTO qcInstructionsDTO = TestDtoFactory.makePopulatedQCInstructionsDTO();

        PayloadEnvelope<QCInstructionsDTO> payloadEnvelope = new PayloadEnvelope<>(qcInstructionsDTO, GobiiProcessType.CREATE);
        RestResource<QCInstructionsDTO> restResourceForPost = new RestResource<>(uriFactory
                .resourceColl(ServiceRequestId.URL_FILE_QC_INSTRUCTIONS));
        PayloadEnvelope<QCInstructionsDTO> qcInstructionFileDTOResponseEnvelope = restResourceForPost.post(QCInstructionsDTO.class,
                payloadEnvelope);

        Assert.assertNotEquals(null, qcInstructionFileDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(qcInstructionFileDTOResponseEnvelope.getHeader()));

        //get intended path for the created directory and check if file has been created
        TestConfiguration testConfiguration = new TestConfiguration();
        String testCrop = testConfiguration.getConfigSettings().getTestExecConfig().getTestCrop();
        String destinationDirectory = testConfiguration.getConfigSettings().getProcessingPath(testCrop, GobiiFileProcessDir.QC_NOTIFICATIONS);
        String createdFile = destinationDirectory +  qcInstructionsDTO.getGobiiQCComplete().getDataFileName()+".json";
        Assert.assertTrue(new File(createdFile).exists());

    }
}
