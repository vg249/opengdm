package org.gobiiproject.gobiimodel.dto.instructions.loader;

import lombok.Data;
import lombok.experimental.Accessors;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.types.DatasetOrientationType;

@Data
@Accessors(chain = true)
public class GobiiLoaderMetadata {

	private PropNameId project = new PropNameId();
	private PropNameId platform = new PropNameId();
	private PropNameId experiment = new PropNameId();

	private DatasetOrientationType datasetOrientationType;
	private PropNameId dataset = new PropNameId();
	private PropNameId datasetType = new PropNameId();
	private PropNameId mapset = new PropNameId();

	private GobiiFile gobiiFile = new GobiiFile();

	private String gobiiCropType;

	private JobPayloadType jobPayloadType;
	private JobProgressStatusType gobiiJobStatus = JobProgressStatusType.CV_PROGRESSSTATUS_NOSTATUS;

	private Integer contactId;
	private String contactEmail;

	private boolean qcCheck;

}