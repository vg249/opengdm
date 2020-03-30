package org.gobiiproject.gobiidtomapping.instructions.impl;

import java.util.Date;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapPlatform;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapProject;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapProtocol;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapJob;
import org.gobiiproject.gobiidtomapping.instructions.DtoMapDigesterProcedure;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProtocolDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.VendorProtocolDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapDigesterProcedureImpl implements DtoMapDigesterProcedure {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDigesterProcedureImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    DtoMapExperiment dtoMapExperiment;

    @Autowired
    DtoMapProject dtoMapProject;

    @Autowired
    DtoMapPlatform dtoMapPlatform;

    @Autowired
    DtoMapDataSet dtoMapDataSet;

    @Autowired
    DtoMapProtocol dtoMapProtocol;

    @Autowired
    private DtoMapJob dtoMapJob = null;


    private void createDirectories(GobiiFile gobiiFile) throws GobiiDaoException {

        if (gobiiFile.isCreateSource()) {
            HelperFunctions.createDirectory(gobiiFile.getSource());
        }

        HelperFunctions.createDirectory(gobiiFile.getDestination());

    } // createDirectories()


    @Override
    public LoaderInstructionFilesDTO createInstruction(String cropType, LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws GobiiException {

        LoaderInstructionFilesDTO returnVal = loaderInstructionFilesDTO;

        if (LineUtils.isNullOrEmpty(returnVal.getName())) {
            throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                    "The instruction file DTO is missing the instruction file name"
            );
        }


        try {


            ConfigSettings configSettings = new ConfigSettings();


            if (null == cropType) {
                throw new GobiiDtoMappingException("Loader instruction request does not specify a crop");
            }


            if (loaderInstructionFilesDTO.getProcedure().getMetadata().getJobPayloadType() == null) {
                throw new Exception("The primary instruction does not have a payload type");
            }


            if (LineUtils.isNullOrEmpty(loaderInstructionFilesDTO.getProcedure().getMetadata().getGobiiCropType())) {
                loaderInstructionFilesDTO.getProcedure().getMetadata().setGobiiCropType(cropType);
            }

            GobiiFile currentGobiiFile = loaderInstructionFilesDTO.getProcedure().getMetadata().getGobiiFile();

            // check that we have all required values
            if (LineUtils.isNullOrEmpty(currentGobiiFile.getSource())) {
                throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                        "The file associated with instruction is missing the source file path"
                );
            }

            if (LineUtils.isNullOrEmpty(currentGobiiFile.getDestination())) {
                throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                        "The file associated with instruction  is missing the destination file path"
                );
            }

            if (currentGobiiFile.isRequireDirectoriesToExist()) {

                if (!HelperFunctions.doesPathExist(currentGobiiFile.getSource())) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "require-to-exist was set to true, but the source file path does not exist: "
                                    + currentGobiiFile.getSource());

                }

                if (!HelperFunctions.doesPathExist(currentGobiiFile.getDestination())) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "require-to-exist was set to true, but the source file path does not exist: "
                                    + currentGobiiFile.getSource());
                }

            }


            // if so, proceed with processing

            //validate loader instruction

            // check if the dataset is referenced by the specified experiment
            if (loaderInstructionFilesDTO.getProcedure().getMetadata().getDataset().getId() != null) {

                DataSetDTO dataSetDTO = dtoMapDataSet.get(loaderInstructionFilesDTO.getProcedure().getMetadata().getDataset().getId());

                // check if the experiment is referenced by the specified project
                if (loaderInstructionFilesDTO.getProcedure().getMetadata().getExperiment().getId() != null) {

                    if (!dataSetDTO.getExperimentId().equals(loaderInstructionFilesDTO.getProcedure().getMetadata().getExperiment().getId())) {

                        throw new GobiiDtoMappingException("The specified experiment in the dataset is incorrect");
                    }

                    ExperimentDTO experimentDTO = dtoMapExperiment.get(loaderInstructionFilesDTO.getProcedure().getMetadata().getExperiment().getId());

                    if (!experimentDTO.getProjectId().equals(loaderInstructionFilesDTO.getProcedure().getMetadata().getProject().getId())) {

                        throw new GobiiDtoMappingException("The specified project in the experiment is incorrect");

                    }

                }

                // check if the datatype is referenced by the dataset
                if (loaderInstructionFilesDTO.getProcedure().getMetadata().getDatasetType().getId() != null) {

                    if (!dataSetDTO.getDatatypeId().equals(loaderInstructionFilesDTO.getProcedure().getMetadata().getDatasetType().getId())) {

                        throw new GobiiDtoMappingException("The specified data type in the dataset is incorrect");

                    }

                }

            }


            if (loaderInstructionFilesDTO.getProcedure().getMetadata().getPlatform().getId() != null
                    && loaderInstructionFilesDTO.getProcedure().getMetadata().getExperiment().getId() != null) {

                ExperimentDTO experimentDTO = dtoMapExperiment.get(loaderInstructionFilesDTO.getProcedure().getMetadata().getExperiment().getId());

                if (experimentDTO.getVendorProtocolId() != null) {

                    VendorProtocolDTO vendorProtocolDTO = dtoMapProtocol.getVendorProtocolByVendorProtocolId(experimentDTO.getVendorProtocolId());

                    if (vendorProtocolDTO.getProtocolId() != null) {

                        ProtocolDTO protocolDTO = dtoMapProtocol.get(vendorProtocolDTO.getProtocolId());

                        if (protocolDTO.getPlatformId() != null) {

                            Integer loaderPlatformId = loaderInstructionFilesDTO.getProcedure().getMetadata().getPlatform().getId();

                            if (!loaderPlatformId.equals(protocolDTO.getPlatformId())) {

                                throw new GobiiDtoMappingException("The specified platform in the experiment is incorrect");

                            }

                        }

                    }

                }

            }


            // "source file" is the data file the user may have already uploaded
            if (currentGobiiFile.isCreateSource()) {

                createDirectories(currentGobiiFile);

            } else {

                // it's supposed to exist, so we check
                if (HelperFunctions.doesPathExist(currentGobiiFile.getSource())) {

                    createDirectories(currentGobiiFile);
                } else {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The load file was specified to exist, but does not exist: "
                                    + currentGobiiFile.getSource());

                } // if-else the source file exists

            } // if-else we're creating a source file


//            } // iterate instructions/files


            // NOW CREATE THE JOB RECORD *********************************************************
            Integer dataSetId = null;
            if (JobPayloadType.CV_PAYLOADTYPE_MATRIX.equals(loaderInstructionFilesDTO.getProcedure().getMetadata().getJobPayloadType())) {
                if (loaderInstructionFilesDTO.getProcedure().getMetadata().getDataset().getId() != null) {
                    dataSetId = loaderInstructionFilesDTO.getProcedure().getMetadata().getDataset().getId();
                } else {
                    if (loaderInstructionFilesDTO.getProcedure().getMetadata().getDataset() != null
                            && loaderInstructionFilesDTO.getProcedure().getMetadata().getDataset().getId() != null) {
                        dataSetId = loaderInstructionFilesDTO.getProcedure().getMetadata().getDataset().getId();
                    }
                }

                if (dataSetId == null || dataSetId <= 0) {
                    throw new GobiiException("The specified job has payload type MATRIX, but no dataset ID: " + loaderInstructionFilesDTO.getName());
                }

            }


            Integer contactId = loaderInstructionFilesDTO.getProcedure().getMetadata().getContactId();

            if (contactId != null || contactId > 0) {


                //check for duplicate job name and provide meaningful error message
                JobDTO jobDTOExisting = dtoMapJob.getJobDetailsByJobName(loaderInstructionFilesDTO.getName());
                if (jobDTOExisting.getJobId() == null || jobDTOExisting.getJobId() <= 0) {

                    JobDTO jobDTONew = new JobDTO();

                    jobDTONew.setJobName(loaderInstructionFilesDTO.getName());
                    jobDTONew.getDatasetIds().add(dataSetId);
                    jobDTONew.setSubmittedBy(contactId);
                    jobDTONew.setMessage("Instruction file written by web services");
                    jobDTONew.setStatus(JobProgressStatusType.CV_PROGRESSSTATUS_PENDING.getCvName());
                    jobDTONew.setPayloadType(loaderInstructionFilesDTO.getProcedure().getMetadata().getJobPayloadType().getCvName());
                    jobDTONew.setType(JobType.CV_JOBTYPE_LOAD.getCvName());
                    jobDTONew.setSubmittedDate(new Date());
                    jobDTONew.setProcedure(loaderInstructionFilesDTO.toString());

                    dtoMapJob.createJob(jobDTONew);

                } else {

                    throw new GobiiException("The specified loader job already exists: " + loaderInstructionFilesDTO.getName());

                }// if-else a job with that name already exists

            } else {

                throw new GobiiException("The specified job does not have a contact ID: " + loaderInstructionFilesDTO.getName());

            } //if-else we have a contact id


        } catch (GobiiException e) {
            throw e;
        } catch (Exception e) {
            throw new GobiiException(e);
        }

        return returnVal;

    } // writeInstructions

}
