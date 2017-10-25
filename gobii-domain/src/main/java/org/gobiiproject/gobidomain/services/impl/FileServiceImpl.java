package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.FilesService;
import org.gobiiproject.gobiidao.filesystem.access.InstructionFileAccess;
import org.gobiiproject.gobiidtomapping.instructions.DtoMapExtractorInstructions;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
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

    @Autowired
    DtoMapExtractorInstructions extractorInstructions;


    private String getFilePath(String cropType, String jobId, GobiiFileProcessDir gobiiFileProcessDir) throws Exception {

        String returnVal = null;

        ConfigSettings configSettings = new ConfigSettings();

        if (gobiiFileProcessDir.equals(GobiiFileProcessDir.EXTRACTOR_OUTPUT)) {

            ExtractorInstructionFilesDTO extractorInstructionFilesDTO = extractorInstructions.getStatus(cropType, jobId);
            if (extractorInstructionFilesDTO
                    .getGobiiExtractorInstructions().size() > 0
                    && extractorInstructionFilesDTO
                    .getGobiiExtractorInstructions()
                    .get(0)
                    .getDataSetExtracts().size() > 0) {

                GobiiDataSetExtract gobiiDataSetExtract = extractorInstructionFilesDTO
                        .getGobiiExtractorInstructions()
                        .get(0)
                        .getDataSetExtracts()
                        .get(0);

                returnVal = gobiiDataSetExtract.getExtractDestinationDirectory();


            } else {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.NONE, "There is no instruction for the job ");
            }

        } else {
            returnVal = configSettings.getProcessingPath(cropType,
                    gobiiFileProcessDir);
        }

        return returnVal;
    }


    @Override
    public void writeFileToProcessDir(String cropType,
                                      String fileName,
                                      GobiiFileProcessDir gobiiFileProcessDir,
                                      byte[] byteArray) throws Exception {


        // our goal here would be to prevent anyone but a test system-defined user from writing
        // arbitrary files to the server. We would  need to do this more systematically when we
        // implement a real authoriization mechanism; however, the authentication object comes
        // out null here. For now it's ok because at least a user has to be authenticated to
        // get access to this service.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//        if( currentPrincipalName != "USER_READER" ) {
        ConfigSettings configSettings = new ConfigSettings();

        String path = configSettings.getProcessingPath(cropType, gobiiFileProcessDir);
        String fqpn = instructionFileAccess.makeFileName(path, fileName);
        instructionFileAccess.writeFile(fqpn, byteArray);
//        } else {
//           throw new GobiiDomainException("Unauthorized access");
//        }
    }

    @Override
    public void deleteFileFromProcessDir(String cropType,
                                  String fileName,
                                  GobiiFileProcessDir gobiiFileProcessDir) throws Exception {

        ConfigSettings configSettings = new ConfigSettings();
        String path = configSettings.getProcessingPath(cropType, gobiiFileProcessDir);
        String fqpn = instructionFileAccess.makeFileName(path, fileName);
        instructionFileAccess.deleteFile(fqpn);


    }



    @Override
    public void writeJobFileForCrop(String cropType,
                                    String jobId,
                                    String fileName,
                                    GobiiFileProcessDir gobiiFileProcessDir,
                                    byte[] byteArray) throws GobiiException, Exception {

        String path = this.getFilePath(cropType, jobId, gobiiFileProcessDir);
        String fqpn = instructionFileAccess.makeFileName(path, fileName);
        instructionFileAccess.writeFile(fqpn, byteArray);
    }

    @Override
    public File readCropFileForJob(String cropType,
                                   String gobiiJobId,
                                   String fileName,
                                   GobiiFileProcessDir gobiiFileProcessDir) throws GobiiException, Exception {

        File returnVal;


        String path = this.getFilePath(cropType, gobiiJobId, gobiiFileProcessDir);

        String fqpn = instructionFileAccess.makeFileName(path, fileName);

        returnVal = instructionFileAccess.readFile(fqpn);

        return returnVal;
    }


} // ExtractorInstructionFileServiceImpl
