package org.gobiiproject.gobiimodel.utils.email;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigestResults;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterConfig;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.dto.instructions.loader.IFLLineCounts;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.validation.ValidationConstants;
import org.gobiiproject.gobiimodel.dto.instructions.validation.ValidationResult;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.utils.SimpleTimer;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.springframework.util.CollectionUtils;

public class DigesterProcessMessage extends ProcessMessage{

	public static DigesterProcessMessage create(ConfigSettings config, DigesterConfig digestConfig, LoaderInstructionFilesDTO instruction, DigestResults results) throws Exception {

		GobiiLoaderProcedure procedure = instruction.getProcedure();

		DigesterProcessMessage pm = new DigesterProcessMessage();

		pm.addIdentifier("Project", procedure.getMetadata().getProject());
		pm.addIdentifier("Platform", procedure.getMetadata().getPlatform());
		pm.addIdentifier("Experiment", procedure.getMetadata().getExperiment());
		pm.addIdentifier("Dataset", procedure.getMetadata().getDataset());
		pm.addIdentifier("Mapset", procedure.getMetadata().getMapset());
		pm.addIdentifier("Dataset Type", procedure.getMetadata().getDatasetType());

		pm.addFolderPath("Destination Directory", procedure.getMetadata().getGobiiFile().getDestination() + "/",config);
		pm.addFolderPath("Input Directory", procedure.getMetadata().getGobiiFile().getSource()+"/", config);

		pm.setUser(procedure.getMetadata().getContactEmail());

		pm.addPath("matrix directory", digestConfig.getPathToHDF5Files(), config, false);

		if (! CollectionUtils.isEmpty(results.getValidationResults())) {
			results.getIflLineCounts().forEach(count -> addIflLineCounts(pm, count));
		}

		pm.addPath("Error Log", results.getLogFile(), config, false);

		GobiiFileType loadType = procedure.getMetadata().getGobiiFile().getGobiiFileType();
		String loadTypeName = "";//No load type name if default
		if (loadType != GobiiFileType.GENERIC) loadTypeName = loadType.name();
		pm.setBody(results.getJobName(), loadTypeName, SimpleTimer.stop("FileRead"), Logger.getFirstErrorReason(), Logger.success(), Logger.getAllErrorStringsHTML());


		if (! CollectionUtils.isEmpty(results.getValidationResults())) {
			boolean hasAnyFailedStatuses = results.getValidationResults().stream()
					.map(ValidationResult::getStatus)
					.anyMatch(ValidationConstants.FAILURE::equalsIgnoreCase);

			for (ValidationResult result : results.getValidationResults()) {
				if (result.status.equalsIgnoreCase(ValidationConstants.FAILURE)) {
					for (int i = 0; i < result.failures.size(); i++) {
						pm.addValidateTableElement(result.fileName, result.status, result.failures.get(i).reason, result.failures.get(i).columnName, result.failures.get(i).values);
					}
				}
				if (result.status.equalsIgnoreCase(ValidationConstants.SUCCESS)) {
					//If any failed statii(statuses) exist, we should have this table, otherwise it should not exist
					if (hasAnyFailedStatuses) {
						pm.addValidateTableElement(result.fileName, result.status);
					}
				}
			}
		}

		return pm;
	}

	private static boolean isVariableLengthTable(String tableKey) {
		return tableKey.contains("_prop");
	}

	private static void addIflLineCounts(DigesterProcessMessage pm, IFLLineCounts iflLineCounts) {

		//Default to 'we had an error'
		String totalLinesVal, linesLoadedVal, existingLinesVal, invalidLinesVal;
		totalLinesVal = linesLoadedVal = existingLinesVal = invalidLinesVal = "error";

		if (isVariableLengthTable(iflLineCounts.getKey())) {
			totalLinesVal = iflLineCounts.getTotalLines() + "";
			linesLoadedVal = iflLineCounts.getLoadedData() + "";
			//Existing and Invalid may be absolutely random numbers in EAV JSON objects
			//Also, loaded may be waaaay above total, this is normal. So lets not report these two fields at all
			existingLinesVal = "";
			invalidLinesVal = "";

			//We can still warn people if no lines were loaded
			if (iflLineCounts.getLoadedData() == 0) {
				linesLoadedVal = "<b style=\"background-color:yellow\">" + iflLineCounts.getLoadedData() + "</b>";
			}
		} else {
			totalLinesVal = iflLineCounts.getTotalLines() + "";
			linesLoadedVal = iflLineCounts.getLoadedData() + "";//Header
			existingLinesVal = iflLineCounts.getExistingData() + "";
			invalidLinesVal = iflLineCounts.getInvalidData() + "";
			if (! iflLineCounts.isNoDupsFileExists()) {
				existingLinesVal = "";
			}
			if (iflLineCounts.getInvalidData() != 0) {
				invalidLinesVal = "<b style=\"background-color:red\">" + iflLineCounts.getInvalidData() + "</b>";
			}
			if (iflLineCounts.getLoadedData() == 0) {
				linesLoadedVal = "<b style=\"background-color:yellow\">" + iflLineCounts.getLoadedData() + "</b>";
			}
		}
		pm.addEntry(iflLineCounts.getKey(), totalLinesVal, linesLoadedVal, existingLinesVal, invalidLinesVal);
	}

}
