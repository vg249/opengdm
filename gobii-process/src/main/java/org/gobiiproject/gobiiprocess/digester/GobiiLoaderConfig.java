package org.gobiiproject.gobiiprocess.digester;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GobiiLoaderConfig {

	private String instructionFile;

	private boolean verbose;
	private String rootDir;
	private String errorLog;
	private String propertiesFile;

	private String hdf5FilesPath;
}
