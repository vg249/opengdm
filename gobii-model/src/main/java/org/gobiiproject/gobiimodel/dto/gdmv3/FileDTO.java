package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO {

    private String fileUploadId;

    private String fileName;

    private String fileUrlPath;

    private String serverFilePath;

    private String type;

    private String mimeType;

    private String cropType;

    private String createdBy;

    private Date createdDate;

    private String modifiedBy;

    private Date modifiedDate;
}
