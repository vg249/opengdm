package org.gobiiproject.gobiimodel.dto.gdmv3;

import lombok.Data;

@Data
public class GenotypeUploadRequestDTO {

    private String platformId;

    private String datasetId;

    private String mapId;

    private String fileType;

    private String dataType;

    private boolean loadMarkers;

    private boolean loadDnaRunNamesAsSamplesAndGermplasms;

    private Integer templateId;

}
