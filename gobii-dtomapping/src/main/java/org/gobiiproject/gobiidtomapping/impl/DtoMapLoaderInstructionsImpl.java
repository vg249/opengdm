package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderInstructionsDAO;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderInstructions;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapLoaderInstructionsImpl implements DtoMapLoaderInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapLoaderInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private LoaderInstructionsDAO loaderInstructionsDAO;

    private void createDirectories(String instructionFileDirectory,
                                   LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws GobiiDaoException {


        if (null != instructionFileDirectory) {
            if (!loaderInstructionsDAO.doesPathExist(instructionFileDirectory)) {
                loaderInstructionsDAO.makeDirectory(instructionFileDirectory);
            }
        }

        if (loaderInstructionFilesDTO.isCreateSourceFile()) {
            if (!loaderInstructionsDAO.doesPathExist(loaderInstructionFilesDTO.getRawUserFilePath())) {
                loaderInstructionsDAO.makeDirectory(loaderInstructionFilesDTO.getRawUserFilePath());
            }
        }

        if (!loaderInstructionsDAO.doesPathExist(loaderInstructionFilesDTO.getIntermediateFilesDirectory())) {
            loaderInstructionsDAO.makeDirectory(loaderInstructionFilesDTO.getIntermediateFilesDirectory());
        }

    } // createDirectories()


    @Override
    public LoaderInstructionFilesDTO writeInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO) {

        LoaderInstructionFilesDTO returnVal = loaderInstructionFilesDTO;

        try {

            // check that we have all required values
            boolean allValuesSpecified = true;
            if (LineUtils.isNullOrEmpty(returnVal.getInstructionFileName())) {
                allValuesSpecified = false;
                returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                        DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                        "instruction file name is missing");
            }

            if (LineUtils.isNullOrEmpty(returnVal.getRawUserFilePath())) {
                allValuesSpecified = false;
                returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                        DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                        "digest file destination path is missing");
            }

            if (returnVal.isRequireDirectoriesToExist()) {

                if (!loaderInstructionsDAO.doesPathExist(returnVal.getIntermediateFilesDirectory())) {
                    allValuesSpecified = false;
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "require-to-exist was set to true, but the source file path does not exist: "
                                    + returnVal.getIntermediateFilesDirectory());
                }

                if (!loaderInstructionsDAO.doesPathExist(returnVal.getRawUserFilePath())) {
                    allValuesSpecified = false;
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "require-to-exist was set to true, but the digest destination file path does not exist: "
                                    + returnVal.getRawUserFilePath());
                }

            }


            // if so, proceed with processing
            if (allValuesSpecified) {

                ConfigSettings configSettings = new ConfigSettings();

                String instructionFileDirectory = configSettings
                        .getCurrentCropConfig()
                        .getInstructionFilesDirectory();

                String instructionFileFqpn = instructionFileDirectory
                        + loaderInstructionFilesDTO.getInstructionFileName()
                        + INSTRUCTION_FILE_EXT;

                // "source file" is the data file the user may have already uploaded
                if (loaderInstructionFilesDTO.isCreateSourceFile()) {

                    createDirectories(instructionFileDirectory,
                            returnVal);
                    loaderInstructionsDAO.writeInstructions(instructionFileFqpn,
                            returnVal.getGobiiLoaderInstructions());

                } else {

                    // it's supposed to exist, so we check
                    if (loaderInstructionsDAO.doesPathExist(loaderInstructionFilesDTO.getRawUserFilePath())) {

                        createDirectories(instructionFileDirectory,
                                returnVal);

                        loaderInstructionsDAO.writeInstructions(instructionFileFqpn,
                                returnVal.getGobiiLoaderInstructions());

                    } else {

                        returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "The load file was specified to exist, but does not exist: " +
                                        instructionFileFqpn);

                    } // if-else the source file exists

                } // if-else we're creating a source file

            } // if we have all the input values we need

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;

    } // writeInstructions

    @Override
    public LoaderInstructionFilesDTO readInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO) {

        LoaderInstructionFilesDTO returnVal = loaderInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileFqpn = configSettings
                    .getCurrentCropConfig()
                    .getInstructionFilesDirectory()
                    + loaderInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            if (loaderInstructionsDAO.doesPathExist(instructionFileFqpn)) {


                List<GobiiLoaderInstruction> instructions =
                        loaderInstructionsDAO
                                .getInstructions(instructionFileFqpn);

                if (null != instructions) {
                    loaderInstructionFilesDTO.setGobiiLoaderInstructions(instructions);
                } else {
                    returnVal.getDtoHeaderResponse()
                            .addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                    DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                    "The instruction file exists, but could not be read: " +
                                            instructionFileFqpn);
                }

            } else {

                returnVal.getDtoHeaderResponse()
                        .addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "The specified instruction file does not exist: " +
                                        instructionFileFqpn);

            } // if-else instruction file exists

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }
}
