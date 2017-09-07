package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.JobService;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.DtoMapJob;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.JobDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 8/30/2017.
 */
public class JobServiceImpl implements JobService {

    Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    @Autowired
    private DtoMapJob dtoMapJob = null;

    @Autowired
    private DtoMapDataSet dtoMapDataSet = null;

    @Override
    public JobDTO createJob(JobDTO jobDTO) throws GobiiDomainException {

        JobDTO returnVal;

        // check if the payload type of the job being submitted is a matrix
        // if it is a matrix, the datasetId of the JobDTO should not be empty

        if (jobDTO.getPayloadType().equals(JobDTO.CV_PAYLOADTYPE_MATRIX) && (null == jobDTO.getDatasetId())) {

            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Missing dataset ID for job: " +
                            jobDTO.getJobName() + " with payload type matrix.");

        }

        returnVal = dtoMapJob.createJob(jobDTO);

        if (jobDTO.getPayloadType().equals(JobDTO.CV_PAYLOADTYPE_MATRIX)) {

            // get DatasetDTO

            DataSetDTO dataSetDTO = dtoMapDataSet.getDataSetDetails(jobDTO.getDatasetId());
            dataSetDTO.setJobId(jobDTO.getJobId());
            dataSetDTO = dtoMapDataSet.replaceDataSet(dataSetDTO.getDataSetId(), dataSetDTO);

        }

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


        return returnVal;
    }

    @Override
    public JobDTO replaceJob(String jobName, JobDTO jobDTO) throws GobiiDomainException {

        JobDTO returnVal;

        if (null == jobDTO.getJobName() || jobDTO.getJobName().equals(jobName)) {

            JobDTO existingJobDTO = dtoMapJob.getJobDetails(jobName);

            if (null != existingJobDTO.getJobName() && existingJobDTO.getJobName().equals(jobName)) {

                returnVal = dtoMapJob.replaceJob(jobName, jobDTO);
                returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The job name specified in the dto ("
                                + jobDTO.getJobName()
                                + ") does not match the job name passed as a parameter "
                                + "("
                                + jobName
                                + ")");

            }

        } else {

            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "The jobId specified in the dto ("
                            + jobDTO.getJobId()
                            + ") does not match the jobId passed as a parameter "
                            + "("
                            + jobName
                            + ")");

        }

        return returnVal;
    }

    @Override
    public List<JobDTO> getJobs() throws GobiiDomainException {

        List<JobDTO> returnVal;

        returnVal = dtoMapJob.getJobs();

        for (JobDTO currentJobDTO : returnVal) {

            currentJobDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            currentJobDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        }

        if (null == returnVal) {

            returnVal = new ArrayList<>();

        }

        return returnVal;

    }

    @Override
    public JobDTO getJobByJobName(String jobName) throws GobiiDomainException {

        JobDTO returnVal;

        returnVal = dtoMapJob.getJobDetails(jobName);

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


        if (null == returnVal) {
            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The specified jobId ("
                            + jobName
                            + ") does not match an existing job");

        }

        return returnVal;

    }


}
