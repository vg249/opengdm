package org.gobiiproject.gobiimodel.dto.gdmv3;

import java.util.List;

import org.gobiiproject.gobiimodel.types.GobiiFileType;

import lombok.Data;

@Data
public class GenotypeUploadRequestDTO {

    private String platformId;

    private String datasetId;

    private String mapId;

    private GobiiFileType fileType;

    private List<FileDTO> inputFiles;

    private String dataType;

    private boolean loadMarkers;

    private boolean loadDnaRunNamesAsSamplesAndGermplasms;

    private Integer templateId;

}
