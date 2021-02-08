package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.gobiiproject.gobiimodel.cvnames.DatasetType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;

@Data
public class GenotypesUploadRequestDTO {

    private GobiiFileType fileType;

    private String dataFormat;

    private Integer genotypeTemplateId;

}
