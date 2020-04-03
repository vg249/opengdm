package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import lombok.Data;

@Data
public class GobiiExtractMetadata {

	private Integer contactId;
	private String contactEmail;
	private String gobiiCropType = null;
	private boolean qcCheck = false;

}
