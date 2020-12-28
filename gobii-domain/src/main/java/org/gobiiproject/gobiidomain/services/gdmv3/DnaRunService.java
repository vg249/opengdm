package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;

import java.io.InputStream;

public interface DnaRunService {
    JobDTO loadDnaRuns(InputStream dnaRunFile,
                       DnaRunUploadRequestDTO dnaRunUploadRequest,
                       String cropType);
}
