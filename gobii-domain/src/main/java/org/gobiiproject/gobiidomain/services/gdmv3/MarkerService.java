package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.noaudit.JobDTO;

public interface MarkerService {

    JobDTO uploadMarkerFile(byte[] markerFile,
                            MarkerUploadRequestDTO markerUploadRequest,
                            String cropType) throws GobiiException;
}
