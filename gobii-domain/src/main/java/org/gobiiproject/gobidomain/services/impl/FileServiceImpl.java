package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobidomain.services.FilesService;
import org.gobiiproject.gobiidao.filesystem.access.InstructionFileAccess;
import org.gobiiproject.gobiidtomapping.DtoMapExtractorInstructions;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;


/**
 * Created by Angel on 6/8/2016.
 */
public class FileServiceImpl implements FilesService {

    private Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);


    InstructionFileAccess<Void> instructionFileAccess = new InstructionFileAccess<>(Void.class);

    @Override
    public void writeFile(String cropType,
                          String fileNameStem,
                          GobiiFileProcessDir gobiiFileProcessDir,
                          String extension,
                          byte[] byteArray) throws GobiiException, Exception {

        instructionFileAccess.writeFileToFileProcDir(cropType,
                fileNameStem,
                gobiiFileProcessDir,
                extension,
                byteArray);
    }

    @Override
    public File readFile(String cropType,
                  String fileName,
                  GobiiFileProcessDir gobiiFileProcessDir) throws GobiiException, Exception {

        File returnVal;

        returnVal = instructionFileAccess.readFileFromProcDir(cropType,
                fileName,
                gobiiFileProcessDir);

        return returnVal;
    }



} // ExtractorInstructionFileServiceImpl
