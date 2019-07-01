package org.gobiiproject.gobiimodel.dto.instructions.loader;

import lombok.Data;
import lombok.experimental.Accessors;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.types.DataSetOrientationType;

@Data
@Accessors(chain = true)
public class GobiiLoaderMetadata {

    private PropNameId project = new PropNameId();
    private PropNameId platform = new PropNameId();
    private PropNameId dataset = new PropNameId();
    private PropNameId datasetType = new PropNameId();
    private PropNameId experiment = new PropNameId();
    private PropNameId mapset = new PropNameId();

    private String gobiiCropType;

    private GobiiFile gobiiFile = new GobiiFile();

    private JobPayloadType jobPayloadType;

    private boolean qcCheck;

    private String contactEmail;

    private Integer contactId;

    private DataSetOrientationType dataSetOrientationType;
}
