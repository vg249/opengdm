package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.QCInstructionsDAO;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.DtoMapQCInstructions;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapQCInstructionsImpl implements DtoMapQCInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapQCInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private QCInstructionsDAO qcInstructionsDAO;

    @Autowired
    DtoMapContact dtoMapContact;

    private void createDirectories(String instructionFileDirectory) throws GobiiDaoException {


        if (null != instructionFileDirectory) {
            if (!qcInstructionsDAO.doesPathExist(instructionFileDirectory)) {
                qcInstructionsDAO.makeDirectory(instructionFileDirectory);
            } else {
                qcInstructionsDAO.verifyDirectoryPermissions(instructionFileDirectory);
            }
        }

    } // createDirectories()


    @Override
    public QCInstructionsDTO createInstructions(String cropType, QCInstructionsDTO qcInstructionsDTO) throws GobiiException {

        QCInstructionsDTO returnVal = qcInstructionsDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileDirectory = configSettings.getProcessingPath(cropType,
                    GobiiFileProcessDir.QC_NOTIFICATIONS);

            createDirectories(instructionFileDirectory);

            String instructionFileFqpn = instructionFileDirectory
                    + qcInstructionsDTO.getGobiiQCComplete().getDataFileName()
                    + INSTRUCTION_FILE_EXT;


            if (!qcInstructionsDAO.doesPathExist(instructionFileFqpn)) {
                qcInstructionsDAO.writeInstructions(instructionFileFqpn,
                        qcInstructionsDTO.getGobiiQCComplete());

            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                        "The specified instruction file already exists: " + instructionFileFqpn);
            }


        } catch (GobiiException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiException(e);
        }


        return returnVal;

    } // writeInstructions
}
