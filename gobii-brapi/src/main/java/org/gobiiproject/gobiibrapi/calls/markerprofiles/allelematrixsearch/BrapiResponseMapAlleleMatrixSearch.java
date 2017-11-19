package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrixsearch;

import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiidao.filesystem.access.InstructionFileAccess;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapJob;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
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
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapAlleleMatrixSearch {

    @Autowired
    private ExtractorInstructionFilesService extractorInstructionFilesService;

    @Autowired
    private DtoMapJob dtoMapJob;

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
        JobDTO jobDTO = dtoMapJob.getJobDetailsByJobName(jobId);

        if (jobDTO.getStatus() == null)
            brapiMetaData.addStatusMessage("error", "No extractor instruction with job : " + jobId + ". Check jobId.");
        else {

            String jobStatus = null;
            JobProgressStatusType status = JobProgressStatusType.byValue(jobDTO.getStatus());
            switch (status) {
                case CV_PROGRESSSTATUS_FAILED:
                case CV_PROGRESSSTATUS_ABORTED:
                    jobStatus = "FAILED";
                    break;
                case CV_PROGRESSSTATUS_PENDING:
                    jobStatus = "PENDING";
                    break;
                case CV_PROGRESSSTATUS_COMPLETED:
                    jobStatus = "COMPLETED";
                    break;
                case CV_PROGRESSSTATUS_INPROGRESS:
                case CV_PROGRESSSTATUS_METADATAEXTRACT:
                case CV_PROGRESSSTATUS_FINALASSEMBLY:
                case CV_PROGRESSSTATUS_QCPROCESSING:
                    jobStatus = "IN-PROGRESS";
                    break;
            }
            if (jobStatus == null || !jobStatus.equalsIgnoreCase("COMPLETED")) {
                brapiMetaData.addStatusMessage(jobStatus, jobDTO.getMessage());
            } else {
                // Finally job is completed. Lets fill in the data.
                // Job finished implies instruction is in the done folder.
                String fileDirExtractorDoneFqpn = new ConfigSettings().getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE) + jobId + ".json";
                InstructionFileAccess<GobiiExtractorInstruction> instructionFileAccess = new InstructionFileAccess<>(GobiiExtractorInstruction.class);
                if (instructionFileAccess.doesPathExist(fileDirExtractorDoneFqpn)) {
                    List<GobiiExtractorInstruction> gobiiExtractorInstructionsFromFile = instructionFileAccess.
                            getInstructions(fileDirExtractorDoneFqpn, GobiiExtractorInstruction[].class);
                    List<GobiiDataSetExtract> dataSetExtracts = gobiiExtractorInstructionsFromFile.get(0).getDataSetExtracts();

                    File extractDirectoryFile = new File(dataSetExtracts.get(0).getExtractDestinationDirectory());
                    if( extractDirectoryFile.exists() ) {
                        File[] extractedFiles = extractDirectoryFile.listFiles();
                        for(Integer idx = 0; idx < extractedFiles.length; idx++ ) {
                            File currentFile = extractedFiles[idx];
                            // first make the http link
                            RestUri restUri = new GobiiUriFactory(request.getServerName(),
                                    request.getServerPort(),
                                    request.getContextPath(),
                                    GobiiControllerType.GOBII)
                                    .resourceColl(GobiiServiceRequestId.URL_FILES)
                                    .addUriParam("gobiiJobId",jobId)
                                    .addUriParam("destinationType", GobiiFileProcessDir.EXTRACTOR_OUTPUT.toString().toLowerCase())
                                    .addQueryParam("fileName", currentFile.getName());
                            String fileUri = restUri.makeUrlComplete();
                            brapiMetaData.getDatafiles().add(fileUri);
                            // now the absolute path to the file
                            String filePath = FilenameUtils.normalize(currentFile.getAbsolutePath());
                            brapiMetaData.getDatafiles().add(filePath);
                        }
                    } else {
                        brapiMetaData.addStatusMessage("error", "The extract directory does not exist. Issue extract instruction again.");
                    }
                }
                brapiMetaData.addStatusMessage("async-status", jobStatus);
            }
        }
        return brapiMetaData;
    }
}