package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypesUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;

import java.io.InputStream;

public interface GenotypeService {

    JobDTO loadGenotypes(InputStream fileInputStream,
                         String fileOriginalName,
                         GenotypesUploadRequestDTO genotypesUploadRequest,
                         String cropType) throws GobiiException;
}
