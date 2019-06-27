package org.gobiiproject.gobiiprocess.qc;

import org.gobiiproject.gobiiapimodel.payload.Header;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderMetadata;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

public class QcWebExecutor implements QcExecutor {


	private GobiiExtractorInstruction qcExtractInstruction = null;

	public GobiiExtractorInstruction createQCExtractInstruction(GobiiLoaderMetadata meta) {
		GobiiExtractorInstruction gobiiExtractorInstruction;
		ErrorLogger.logInfo("Digester", "qcCheck detected");
		ErrorLogger.logInfo("Digester", "Entering into the QC Subsection #1 of 3...");
		gobiiExtractorInstruction = new GobiiExtractorInstruction();
		gobiiExtractorInstruction.setContactEmail(meta.getContactEmail());
		gobiiExtractorInstruction.setContactId(meta.getContactId());
		gobiiExtractorInstruction.setGobiiCropType(meta.getGobiiCropType());
		gobiiExtractorInstruction.getMapsetIds().add(meta.getMapset().getId());
		gobiiExtractorInstruction.setQcCheck(true);
		ErrorLogger.logInfo("Digester", "Done with the QC Subsection #1 of 3!");

		this.qcExtractInstruction = gobiiExtractorInstruction;
		return gobiiExtractorInstruction;
	}

	public void setQCExtractPaths(GobiiLoaderProcedure proc, ConfigSettings configuration, String crop, String instructionFile) {
		ErrorLogger.logInfo("Digester", "Entering into the QC Subsection #2 of 3...");
		GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
		gobiiDataSetExtract.setAccolate(false);  // It is unused/unsupported at the moment
		gobiiDataSetExtract.setDataSet(proc.getMetadata().getDataSet());
		gobiiDataSetExtract.setGobiiDatasetType(proc.getMetadata().getDatasetType());

		// According to Liz, the Gobii extract filter type is always "WHOLE_DATASET" for any QC job
		gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
		gobiiDataSetExtract.setGobiiFileType(GobiiFileType.HAPMAP);
		// It is going to be set by the Gobii web services
		gobiiDataSetExtract.setGobiiJobStatus(null);
		qcExtractInstruction.getDataSetExtracts().add(gobiiDataSetExtract);
		ErrorLogger.logInfo("Digester", "Done with the QC Subsection #2 of 3!");
	}

	public void sendQCExtract(ConfigSettings configuration, String crop) throws Exception {
		ErrorLogger.logInfo("Digester", "Entering into the QC Subsection #3 of 3...");
		ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();
		extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(qcExtractInstruction);
		extractorInstructionFilesDTOToSend.setInstructionFileName("extractor_" + DateUtils.makeDateIdString());
		GobiiClientContext gobiiClientContext = GobiiClientContext.getInstance(configuration, crop, GobiiAutoLoginType.USER_RUN_AS);
		if (LineUtils.isNullOrEmpty(gobiiClientContext.getUserToken())) {
			ErrorLogger.logError("Digester", "Unable to log in with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
			return;
		}
		String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
		GobiiUriFactory gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);
		PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);
		GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO, ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForPost = new GobiiEnvelopeRestResource<>(gobiiUriFactory
				.resourceColl(RestResourceId.GOBII_FILE_EXTRACTOR_INSTRUCTIONS));
		PayloadEnvelope<ExtractorInstructionFilesDTO> extractorInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForPost.post(ExtractorInstructionFilesDTO.class,
				payloadEnvelope);

		if (extractorInstructionFileDTOResponseEnvelope != null) {

			Header header = extractorInstructionFileDTOResponseEnvelope.getHeader();
			if (header.getStatus().isSucceeded()) {
				ErrorLogger.logInfo("Digester", "Extractor Request Sent");

			} else {

				String messages = extractorInstructionFileDTOResponseEnvelope.getHeader().getStatus().messages();

				for (HeaderStatusMessage currentStatusMesage : header.getStatus().getStatusMessages()) {
					messages += (currentStatusMesage.getMessage()) + "; ";
				}

				ErrorLogger.logError("Digester", "Error sending extract request: " + messages);

			}
		} else {
			ErrorLogger.logInfo("Digester", "Error Sending Extractor Request");
		}
		ErrorLogger.logInfo("Digester", "Done with the QC Subsection #3 of 3!");
	}
}
