package org.gobiiproject.gobiiprocess.digester;

import lombok.Data;
import lombok.experimental.Accessors;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.ValidationError;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class GobiiLoaderState {

	private GobiiLoaderProcedure procedure;

	private String jobName;

	private ValidationError[] fileErrors;

	private Map<String, TableStats> tableStats = new HashMap<>();

	private String logFile;


	@Data
	@Accessors(chain = true)
	public static class TableStats {

		private int linesLoaded;

		private int existingLines;

		private int invalidLines;

		private int totalLines;

		private boolean noDupsFileExists;
	}
}

