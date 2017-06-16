package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrixsearch;

import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapAlleleMatrixSearch {

    @Autowired
    private ExtractorInstructionFilesService extractorInstructionFilesService;

    public BrapiMetaData search(String crop, String matrixDbId) {

        BrapiMetaData brapiMetaData = new BrapiMetaData();

        Integer dataSetId = Integer.parseInt(matrixDbId);

        ExtractorInstructionFilesDTO extractorInstructionFilesDTO = new ExtractorInstructionFilesDTO();
        GobiiExtractorInstruction gobiiExtractorInstruction = new GobiiExtractorInstruction();
        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.FLAPJACK);
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        gobiiDataSetExtract.setDataSet(new GobiiFilePropNameId(dataSetId, null));
        gobiiExtractorInstruction.getDataSetExtracts().add(gobiiDataSetExtract);
        gobiiExtractorInstruction.setContactId(1);
        extractorInstructionFilesDTO.getGobiiExtractorInstructions().add(gobiiExtractorInstruction);

        String jobId = DateUtils.makeDateIdString();
        extractorInstructionFilesDTO.setInstructionFileName(jobId);


        ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService
                .createInstruction(crop, extractorInstructionFilesDTO);

        brapiMetaData.addStatusMessage("asynchid", extractorInstructionFilesDTONew.getJobId());

        return brapiMetaData;
    }

    public BrapiMetaData getStatus(String crop, String jobId, HttpServletRequest request) {

        BrapiMetaData brapiMetaData = new BrapiMetaData();

        ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService
                .getStatus(crop, jobId);

        GobiiJobStatus gobiiJobStatus = extractorInstructionFilesDTONew
                .getGobiiExtractorInstructions()
                .get(0)
                .getDataSetExtracts()
                .get(0)
                .getGobiiJobStatus();

        String brapiAsynchStatus = null;
        switch (gobiiJobStatus) {

            case FAILED:
                brapiAsynchStatus = "FAILED";
                break;

            case STARTED:
                brapiAsynchStatus = "PENDING";
                break;

            case COMPLETED:
                brapiAsynchStatus = "FINISHED";
                break;

            case IN_PROGRESS:
                brapiAsynchStatus = "INPROCESS";
                break;

        }


        // this is only for test purposes!!! -- it should
        if (!gobiiJobStatus.equals(GobiiJobStatus.COMPLETED)) {

            try {

                Thread.sleep(4000); // make it look like we're processing

                String testFileName = "illumina.data";
                ClassLoader classLoader = getClass().getClassLoader();
                File testResultFile = new File(classLoader.getResource(testFileName).getFile());


                if (testResultFile.exists()) {

                    RestUri restUri = new GobiiUriFactory(request.getContextPath(),
                            GobiiControllerType.BRAPI)
                            .resourceColl(GobiiServiceRequestId.URL_FILES);


                    String serverName = request.getServerName();
                    int portNumber = request.getServerPort();

                    String fileUri = "http://"
                            + serverName
                            + ":"
                            + portNumber
                            + "/"
                            + restUri.makeUrl()
                            + "?fqpn=" + testResultFile.getAbsolutePath();

                    brapiMetaData.getDatafiles().add(fileUri);

                    brapiAsynchStatus = "FINISHED";
                } else {
                    brapiMetaData.addStatusMessage("error", "The test file is not present: " + testFileName);
                }


            } catch (Exception e) {
                brapiMetaData.addStatusMessage("Exception", e.getMessage());
            }
        }


        brapiMetaData.addStatusMessage("asynchstatus", brapiAsynchStatus);

        return brapiMetaData;
    }

}
