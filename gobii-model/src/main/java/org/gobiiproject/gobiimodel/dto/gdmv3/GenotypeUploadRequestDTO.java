package org.gobiiproject.gobiimodel.dto.gdmv3;

import lombok.Data;

@Data
public class GenotypeUploadRequestDTO {

    private String platformId;

    private String datasetId;

    private String fileType;

    private String dataType;

    private boolean loadMarkers;

    private Integer templateId;

}
