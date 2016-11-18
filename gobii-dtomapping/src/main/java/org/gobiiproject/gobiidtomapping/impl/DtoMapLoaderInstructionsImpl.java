package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderInstructionsDAO;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderInstructions;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapLoaderInstructionsImpl implements DtoMapLoaderInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapLoaderInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private LoaderInstructionsDAO loaderInstructionsDAO;

    private void createDirectories(String instructionFileDirectory,
                                   GobiiFile gobiiFile) throws GobiiDaoException {


        if (null != instructionFileDirectory) {
            if (!loaderInstructionsDAO.doesPathExist(instructionFileDirectory)) {
                loaderInstructionsDAO.makeDirectory(instructionFileDirectory);
            } else {
                loaderInstructionsDAO.verifyDirectoryPermissions(instructionFileDirectory);
            }
        }

        if (gobiiFile.isCreateSource()) {
            if (!loaderInstructionsDAO.doesPathExist(gobiiFile.getSource())) {
                loaderInstructionsDAO.makeDirectory(gobiiFile.getSource());
            } else {
                loaderInstructionsDAO.verifyDirectoryPermissions(gobiiFile.getSource());
            }
        }

        if (!loaderInstructionsDAO.doesPathExist(gobiiFile.getDestination())) {
            loaderInstructionsDAO.makeDirectory(gobiiFile.getDestination());
        } else {
            loaderInstructionsDAO.verifyDirectoryPermissions(gobiiFile.getDestination());
        }

    } // createDirectories()


    @Override
    public LoaderInstructionFilesDTO createInstruction(String cropType, LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws GobiiException {

        LoaderInstructionFilesDTO returnVal = loaderInstructionFilesDTO;

        try {
            ConfigSettings configSettings = new ConfigSettings();


            if (null == cropType) {
                throw new GobiiDtoMappingException("Loader instruction request does not specify a crop");
            }

            String instructionFileDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.LOADER_INSTRUCTIONS);
//                .getCropConfig(cropType)
//                .getLoaderInstructionFilesDirectory();

            String instructionFileFqpn = instructionFileDirectory
                    + loaderInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            for (GobiiLoaderInstruction currentLoaderInstruction :
                    loaderInstructionFilesDTO.getGobiiLoaderInstructions()) {

            if( LineUtils.isNullOrEmpty(currentLoaderInstruction.getGobiiCropType())) {
                currentLoaderInstruction.setGobiiCropType(cropType);
            }

                GobiiFile currentGobiiFile = currentLoaderInstruction.getGobiiFile();

                // check that we have all required values
                if (LineUtils.isNullOrEmpty(returnVal.getInstructionFileName())) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "User file destination is missing"
                    );
                }

                if (LineUtils.isNullOrEmpty(currentGobiiFile.getSource())) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "User file destination is missing"
                    );
                }

                if (LineUtils.isNullOrEmpty(currentGobiiFile.getDestination())) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "User file destination is missing"
                    );
                }

                if (currentGobiiFile.isRequireDirectoriesToExist()) {

                    if (!loaderInstructionsDAO.doesPathExist(currentGobiiFile.getSource())) {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "require-to-exist was set to true, but the source file path does not exist: "
                                        + currentGobiiFile.getSource());

                    }

                    if (!loaderInstructionsDAO.doesPathExist(currentGobiiFile.getDestination())) {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "require-to-exist was set to true, but the source file path does not exist: "
                                        + currentGobiiFile.getSource());
                    }

                }


                // if so, proceed with processing


                // "source file" is the data file the user may have already uploaded
                if (currentGobiiFile.isCreateSource()) {

                    createDirectories(instructionFileDirectory,
                            currentGobiiFile);


                } else {

                    // it's supposed to exist, so we check
                    if (loaderInstructionsDAO.doesPathExist(currentGobiiFile.getSource())) {

                        createDirectories(instructionFileDirectory,
                                currentGobiiFile);
                    } else {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "The load file was specified to exist, but does not exist: "
                                        + currentGobiiFile.getSource());

                    } // if-else the source file exists

                } // if-else we're creating a source file


            } // iterate instructions/files


            loaderInstructionsDAO.writeInstructions(instructionFileFqpn,
                    returnVal.getGobiiLoaderInstructions());

        } catch(GobiiException e) {
            throw e;
        } catch (Exception e) {
            throw new GobiiException(e);
        }

        return returnVal;

    } // writeInstructions

    @Override
    public LoaderInstructionFilesDTO getInstruction(String cropType, String instructionFileName) {

        LoaderInstructionFilesDTO returnVal = new LoaderInstructionFilesDTO();

        try {
            ConfigSettings configSettings = new ConfigSettings();
            String instructionFile = configSettings.getProcessingPath(cropType,GobiiFileProcessDir.LOADER_INSTRUCTIONS)
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;



            if (loaderInstructionsDAO.doesPathExist(instructionFile)) {


                List<GobiiLoaderInstruction> instructions =
                        loaderInstructionsDAO
                                .getInstructions(instructionFile);

                if (null != instructions) {
                    returnVal.setInstructionFileName(instructionFileName);
                    returnVal.setGobiiLoaderInstructions(instructions);

                } else {

                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The instruction file exists, but could not be read: " +
                                    instructionFile);

                }

            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified instruction file does not exist: " +
                                instructionFile);

            } // if-else instruction file exists

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            System.out.println(e);
        }

        return returnVal;
    }
}
