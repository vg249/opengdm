package org.gobiiproject.gobiidtomapping.impl;

import org.apache.commons.lang.time.DateUtils;
import org.gobiiproject.gobiidao.resultset.access.RsJobDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.DtoMapJob;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.JobDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public class DtoMapJobImpl implements DtoMapJob {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapJobImpl.class);

    @Autowired
    private RsJobDao rsJobDao;
    @Autowired
    private DtoMapDataSet dtoMapDataSet = null;


    @Override
    public List<JobDTO> getJobs() throws GobiiDtoMappingException {

        List<JobDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsJobDao.getJobs();

            while (resultSet.next()) {

                JobDTO currentJobDTO = new JobDTO();
                ResultColumnApplicator.applyColumnValues(resultSet, currentJobDTO);

                returnVal.add(currentJobDTO);

            }


        } catch (SQLException e) {
            LOGGER.error("GObii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }

    @Override
    public JobDTO getJobDetailsByJobName(String jobName) throws GobiiDtoMappingException {

        JobDTO returnVal = new JobDTO();


        ResultSet resultSet = rsJobDao.getJobDetailsForJobName(jobName);

        boolean retrievedOneRecord = false;

        try {

            while (resultSet.next()) {

                if (true == retrievedOneRecord) {

                    throw (new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "There are more than one job records for job id: " + jobName));
                }

                retrievedOneRecord = true;

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }


        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public JobDTO createJob(JobDTO jobDTO) throws GobiiDtoMappingException, ParseException {

        JobDTO returnVal = jobDTO;

        // check if the payload type of the job being submitted is a matrix
        // if it is a matrix, the datasetId of the JobDTO should not be empty

        if (jobDTO.getPayloadType().equals(JobPayloadType.CV_PAYLOADTYPE_MATRIX) && (null == jobDTO.getDatasetId())) {

            throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Missing dataset ID for job: " +
                            jobDTO.getJobName() + " with payload type matrix.");

        }


        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        Integer jobId = rsJobDao.createJobWithCvTerms(parameters);
        returnVal.setJobId(jobId);

        if (returnVal.getType().equals(JobType.CV_JOBTYPE_LOAD)
                && returnVal.getPayloadType().equals(JobPayloadType.CV_PAYLOADTYPE_MATRIX.getCvName())) {

            // get DatasetDTO

            DataSetDTO dataSetDTO = dtoMapDataSet.get(jobDTO.getDatasetId());

            String[] datePattern = {"yyyy-MM-dd"};

            Date parsedDate;

            try {

                parsedDate = DateUtils.parseDateStrictly(dataSetDTO.getCreatedDate().toString(), datePattern);

            } catch (Exception e) {

                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Something went wrong with setting the createdDate of the datasetDTO");
            }

            dataSetDTO.setCreatedDate(parsedDate);
            dataSetDTO.setModifiedDate(jobDTO.getSubmittedDate());
            dataSetDTO.setJobId(jobDTO.getJobId());
            dtoMapDataSet.replace(returnVal.getDatasetId(), dataSetDTO);

        }

        return returnVal;

    }

    @Override
    public JobDTO replaceJob(String jobName, JobDTO jobDTO) throws GobiiDtoMappingException {

        JobDTO returnVal = jobDTO;

        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("jobName", jobName);
        rsJobDao.updateJobWithCvTerms(parameters);

        return returnVal;

    }

    @Override
    public JobDTO getJobDetailsByDatasetId(Integer datasetId) throws GobiiDtoMappingException {

        JobDTO returnVal = new JobDTO();

        ResultSet resultSet = rsJobDao.getJobDetailsByDatasetId(datasetId);
        try {
            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

        } catch (SQLException e) {
            throw new GobiiDtoMappingException(e);
        }
        return returnVal;


    }

}
