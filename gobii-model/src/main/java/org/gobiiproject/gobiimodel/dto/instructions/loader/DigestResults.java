package org.gobiiproject.gobiimodel.dto.instructions.loader;

import java.util.List;
import lombok.Data;
import org.gobiiproject.gobiimodel.dto.instructions.validation.ValidationResult;

@Data
public class DigestResults {

	private IFLLineCounts iflLineCounts;

	private String logFile;

	private String jobName;

	private List<ValidationResult> validationResults;
}

