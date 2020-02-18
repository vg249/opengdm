package org.gobiiproject.gobiimodel.dto.instructions.loader;

import lombok.Data;

@Data
public class IFLLineCounts {

	private String key;

	private int loadedData, existingData, invalidData, totalLines;

	private boolean noDupsFileExists;
}
