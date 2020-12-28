package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;

import java.io.InputStream;

public interface MarkerService {

    JobDTO loadMarkerData(InputStream markerFile,
                          MarkerUploadRequestDTO markerUploadRequest,
                          String cropType) throws GobiiException;
}
