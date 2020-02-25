package org.gobiiproject.gobiimodel.dto.instructions.loader;

import lombok.Data;

@Data
public class DigesterConfig {

	private String rootDir;
	private boolean verbose;
	private String errorLogOverride;
	private String propertiesFile;
	private String pathToHDF5Files;
}
