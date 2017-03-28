package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.QCDataDAO;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.DtoMapQCData;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.email.MailInterface;
import org.gobiiproject.gobiimodel.utils.email.QCMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DtoMapQCDataImpl implements DtoMapQCData {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapQCDataImpl.class);

    @Autowired
    private QCDataDAO qcDataDAO;

    @Autowired
    private DtoMapContact dtoMapContact;

    // /second/gobii_03/gobii_bundle/crops/test/qcnotifications/ds_x

    @Override
    public QCDataDTO createData(String cropType, QCDataDTO qcDataDTO) throws GobiiException {

        QCDataDTO returnVal = qcDataDTO;
        try {
            ConfigSettings configSettings = new ConfigSettings();
            String qcNotificationsDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.QC_NOTIFICATIONS);
            StringBuilder datasetSubdirectory = new StringBuilder("ds_").append(qcDataDTO.getDataSetId());
            Path qcDataDirectoryPath = Paths.get(qcNotificationsDirectory, datasetSubdirectory.toString());
            createDirectories(qcDataDirectoryPath.toString());

            /*String instructionFileFqpn = instructionFileDirectory
                    + qcInstructionsDTO.getDataFileName()
                    + INSTRUCTION_FILE_EXT;


            if (!(qcDataDAO.doesPathExist(instructionFileFqpn))) {

                if(qcInstructionsDTO.getGobiiJobStatus().equals(GobiiJobStatus.STARTED)) {

                    // call the KDCompute Service here
                    // boolean isCallSuccessful;
                    // isCallSuccessful = KDStartService();

                    boolean isCallSuccessful = true;

                    QCMessage qcMessage = new QCMessage();

                    MailInterface mailInterface = new MailInterface(configSettings);

                    if(isCallSuccessful) {

                        qcMessage.setBody("Quality job has started.");
                    } else {

                        qcMessage.setBody("Quality job failed.");
                    }

                    mailInterface.send(qcMessage);

                } else if(qcInstructionsDTO.getGobiiJobStatus().equals(GobiiJobStatus.COMPLETED)) {

                    qcInstructionsDAO.writeInstructions(instructionFileFqpn,
                            qcInstructionsDTO);

                    QCMessage qcMessage = new QCMessage();
                    MailInterface mailInterface = new MailInterface(configSettings);

                    qcMessage.setBody(qcInstructionsDTO.getGobiiJobStatus().toString());
                    mailInterface.send(qcMessage);

                }


            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                        "The specified instruction file already exists: " + instructionFileFqpn);
            }*/


        } catch (GobiiException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiException(e);
        }

        return returnVal;

    }

    private void createDirectories(String qcNotificationsDirectory) throws GobiiDaoException {

        if (qcNotificationsDirectory != null) {
            //if (!qcInstructionsDAO.doesPathExist(instructionFileDirectory)) {
            //    qcInstructionsDAO.makeDirectory(instructionFileDirectory);
            //} else {
            //    qcInstructionsDAO.verifyDirectoryPermissions(instructionFileDirectory);
            //}
        }

    }

}
