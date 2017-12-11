package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrixsearch;

import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
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
        gobiiDataSetExtract.setDataSet(new PropNameId(dataSetId, null));
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

    public BrapiMetaData getStatus(String crop, String jobId, HttpServletRequest request) throws Exception {
        BrapiMetaData brapiMetaData = new BrapiMetaData();

        ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService
                .getStatus(crop, jobId);

        String brapiAsynchStatus = null;
        if ((extractorInstructionFilesDTONew.getGobiiExtractorInstructions().size() > 0) &&
                (extractorInstructionFilesDTONew.getGobiiExtractorInstructions().get(0).getDataSetExtracts().size() > 0)) {

            GobiiDataSetExtract gobiiDataSetExtract = extractorInstructionFilesDTONew
                    .getGobiiExtractorInstructions()
                    .get(0)
                    .getDataSetExtracts()
                    .get(0);
            brapiAsynchStatus = gobiiDataSetExtract.getGobiiJobStatus().getCvName();
            // Add the extracted files to response only when job is completed.
            if (gobiiDataSetExtract.getGobiiJobStatus().equals(JobProgressStatusType.CV_PROGRESSSTATUS_COMPLETED)) {
                if (gobiiDataSetExtract.getExtractedFiles().size() > 0) {

                    for (File currentFile : gobiiDataSetExtract.getExtractedFiles()) {

                            // first make the http link
                            RestUri restUri = new GobiiUriFactory(request.getServerName(),
                                    request.getServerPort(),
                                    request.getContextPath(),
                                    GobiiControllerType.GOBII)
                                    .resourceColl(GobiiServiceRequestId.URL_FILES)
                                    .addUriParam("gobiiJobId", jobId)
                                    .addUriParam("destinationType", GobiiFileProcessDir.EXTRACTOR_OUTPUT.toString().toLowerCase())
                                    .addQueryParam("fileName", currentFile.getName());

                            String fileUri = restUri.makeUrlComplete();
                            brapiMetaData.getDatafiles().add(fileUri);

                            // now the absolute path to the file
                            String filePath = FilenameUtils.normalize(currentFile.getAbsolutePath());
                            brapiMetaData.getDatafiles().add(filePath);
                        }

                    } else {
                    brapiMetaData.addStatusMessage("error", "There are no extracted files for the directory: " + gobiiDataSetExtract.getExtractDestinationDirectory());
                    }
            }
        } else {
            brapiMetaData.addStatusMessage("error", "There are not extractor instructions for job : " + jobId);
        }

        brapiMetaData.addStatusMessage("asynchstatus", brapiAsynchStatus);

        return brapiMetaData;
    }
}