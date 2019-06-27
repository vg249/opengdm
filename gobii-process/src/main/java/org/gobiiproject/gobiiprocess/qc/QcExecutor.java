package org.gobiiproject.gobiiprocess.qc;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderMetadata;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;

public interface QcExecutor {

	GobiiExtractorInstruction createQCExtractInstruction(GobiiLoaderMetadata meta);

	void setQCExtractPaths(GobiiLoaderProcedure proc, ConfigSettings configuration, String crop, String instructionFile);

	void sendQCExtract(ConfigSettings configuration, String crop) throws Exception;
}
