package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.gdmv3.FileDTO;

import java.io.InputStream;

public interface FileService {

    FileDTO initiateFileUpload(String cropType) throws GobiiDomainException;

    FileDTO updateFileChunk(FileDTO fileToUpdate,
                            String cropType,
                            InputStream inputStream) throws GobiiDomainException;

}
