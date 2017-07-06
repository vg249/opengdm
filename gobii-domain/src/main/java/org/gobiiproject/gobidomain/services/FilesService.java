package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;


/**
 * Created by Phil on 4/12/2016.
 */
public interface FilesService {

    void writeDataFile(String cropType,
                  String fileNameStem,
                  GobiiFileProcessDir gobiiFileProcessDir,
                  String extension,
                  byte[] byteArray) throws GobiiException, Exception;
}
