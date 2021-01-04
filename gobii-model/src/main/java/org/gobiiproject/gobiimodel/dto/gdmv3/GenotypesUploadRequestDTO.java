package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class GenotypesUploadRequestDTO {

    private String fileType;

    private String dataFormat;

    private Integer genotypeTemplateId;

}
