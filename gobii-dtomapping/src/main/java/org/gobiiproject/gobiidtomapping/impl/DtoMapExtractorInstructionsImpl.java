package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.ExtractorInstructionsDAO;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.DtoMapExtractorInstructions;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapExtractorInstructionsImpl implements DtoMapExtractorInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapExtractorInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private ExtractorInstructionsDAO extractorInstructionsDAO;

    @Autowired
    DtoMapContact dtoMapContact;

    private void createDirectories(String instructionFileDirectory) throws GobiiDaoException {


        if (null != instructionFileDirectory) {
            if (!extractorInstructionsDAO.doesPathExist(instructionFileDirectory)) {
                extractorInstructionsDAO.makeDirectory(instructionFileDirectory);
            } else {
                extractorInstructionsDAO.verifyDirectoryPermissions(instructionFileDirectory);
            }
        }

    } // createDirectories()


    @Override
    public ExtractorInstructionFilesDTO writeInstructions(String cropType, ExtractorInstructionFilesDTO extractorInstructionFilesDTO) throws GobiiException {

        ExtractorInstructionFilesDTO returnVal = extractorInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

//            String currentGobiiCropType = extractorInstructionFilesDTO.getCropType();
//            if (null == currentGobiiCropType) {
//                throw new Exception("Extractor instruction request does not specify a crop");
//            }

            String instructionFileDirectory = configSettings
                    .getCropConfig(cropType)
                    .getExtractorInstructionFilesDirectory();

            createDirectories(instructionFileDirectory);

            String instructionFileFqpn = instructionFileDirectory
                    + extractorInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            boolean allValuesSpecified = true;
            for (GobiiExtractorInstruction currentExtractorInstruction :
                    extractorInstructionFilesDTO.getGobiiExtractorInstructions()) {

                if (LineUtils.isNullOrEmpty(returnVal.getInstructionFileName())) {
                    allValuesSpecified = false;
                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "instruction file name is missing");
                }

                if (null != currentExtractorInstruction.getContactId() && currentExtractorInstruction.getContactId() > 0) {

                    ContactDTO contactDTO = dtoMapContact.getContactDetails(currentExtractorInstruction.getContactId());


                    if (!LineUtils.isNullOrEmpty(contactDTO.getEmail())) {
                        currentExtractorInstruction.setContactEmail(contactDTO.getEmail());
                    } else {
                        allValuesSpecified = false;
                        returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                "The contact record for contactId "
                                        + currentExtractorInstruction.getContactId()
                                        + " does not have an email address");
                    }

                } else {
                    allValuesSpecified = false;
                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "contactId is missing");
                }

                String extractionFileDestinationPath = configSettings
                        .getCropConfig(cropType)
                        .getExtractorInstructionFilesOutputDirectory();


                for (GobiiDataSetExtract currentGobiiDataSetExtract :
                        currentExtractorInstruction.getDataSetExtracts()) {

                    // check that we have all required values
                    if (LineUtils.isNullOrEmpty(currentGobiiDataSetExtract.getDataSetName())) {
                        allValuesSpecified = false;
                        returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                "DataSet name is missing");
                    }

                    if (LineUtils.isNullOrEmpty(Integer.toString(currentGobiiDataSetExtract.getDataSetId()))) {
                        allValuesSpecified = false;
                        returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                "Dataset ID is missing");
                    }

                    String formatName = currentGobiiDataSetExtract.getGobiiFileType().toString().toLowerCase();
                    String dataSetId = currentGobiiDataSetExtract.getDataSetId().toString();
                    String extractorFileDestinationLocation =
                            extractionFileDestinationPath
                                    + formatName
                                    + "/"
                                    + "ds_"
                                    + dataSetId
                                    + "/";

                    if (!extractorInstructionsDAO.doesPathExist(extractorFileDestinationLocation)) {
                        extractorInstructionsDAO.makeDirectory(extractorFileDestinationLocation);
                    } else {
                        extractorInstructionsDAO.verifyDirectoryPermissions(extractorFileDestinationLocation);
                    }


                    currentGobiiDataSetExtract.setExtractDestinationDirectory(extractorFileDestinationLocation);

                }


            } // iterate instructions/files

            if (allValuesSpecified) {

                if (0 ==
                        returnVal
                                .getStatus()
                                .getStatusMessages()
                                .stream()
                                .filter(m -> m.getGobiiStatusLevel().equals(GobiiStatusLevel.ERROR))
                                .collect(Collectors.toList())
                                .size()
                        ) {


                    if (!extractorInstructionsDAO.doesPathExist(instructionFileFqpn)) {

                        extractorInstructionsDAO.writeInstructions(instructionFileFqpn,
                                returnVal.getGobiiExtractorInstructions());
                    } else {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                                "The specified instruction file already exists: " + instructionFileFqpn);
                    }
                }

            } // if all values were specified

        } catch (GobiiException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiException(e);
        }


        return returnVal;

    } // writeInstructions

    @Override
    public ExtractorInstructionFilesDTO readInstructions(String cropType, String instructionFileName) throws GobiiException {

        ExtractorInstructionFilesDTO returnVal = new ExtractorInstructionFilesDTO();

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileFqpn = configSettings
                    .getCropConfig(cropType)
                    .getExtractorInstructionFilesDirectory()
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;


            if (extractorInstructionsDAO.doesPathExist(instructionFileFqpn)) {


                List<GobiiExtractorInstruction> instructions =
                        extractorInstructionsDAO
                                .getInstructions(instructionFileFqpn);

                if (null != instructions) {
                    returnVal.setGobiiExtractorInstructions(instructions);
                } else {
                    returnVal.getStatus()
                            .addStatusMessage(GobiiStatusLevel.ERROR,
                                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                    "The instruction file exists, but could not be read: " +
                                            instructionFileFqpn);
                }

            } else {

                returnVal.getStatus()
                        .addStatusMessage(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "The specified instruction file does not exist: " +
                                        instructionFileFqpn);

            } // if-else instruction file exists

        } catch (Exception e) {
            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
