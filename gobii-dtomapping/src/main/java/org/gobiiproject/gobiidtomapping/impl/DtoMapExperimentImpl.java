package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class DtoMapExperimentImpl implements DtoMapExperiment {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);

    @Autowired
    private RsExperimentDao rsExperimentDao;

    @Override
    public List<ExperimentDTO> getExperiments() throws GobiiDtoMappingException {

        List<ExperimentDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsExperimentDao.getExperiments();


            while (resultSet.next()) {
                ExperimentDTO currentExperimentDao = new ExperimentDTO();
                ResultColumnApplicator.applyColumnValues(resultSet, currentExperimentDao);
                returnVal.add(currentExperimentDao);
            }


        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }
    

    @Override
    public ExperimentDTO getExperimentDetails(Integer experimentId) throws GobiiDtoMappingException {


        ExperimentDTO returnVal = new ExperimentDTO();

        try {

            ResultSet resultSet = rsExperimentDao.getExperimentDetailsForExperimentId(experimentId);

            boolean retrievedOneRecord = false;
            while (resultSet.next()) {

                if (true == retrievedOneRecord) {
                    throw (new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "There are more than one project records for project id: " +experimentId));
                }

                retrievedOneRecord = true;

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    private boolean validateExperimentRequest(ExperimentDTO experimentDTO) throws Exception {

        boolean returnVal = true;

            String experimentName = experimentDTO.getExperimentName();
            Integer projectId = experimentDTO.getProjectId();
            Integer platformId = experimentDTO.getPlatformId();

            ResultSet resultSetExistingProject =
                    rsExperimentDao.getExperimentsByNameProjectidPlatformId(experimentName, projectId, platformId);

            if (resultSetExistingProject.next()) {

                returnVal = false;
                experimentDTO.getStatus().addStatusMessage(GobiiStatusLevel.OK,
                        GobiiValidationStatusType.VALIDATION_COMPOUND_UNIQUE,
                        "An experiment with name "
                                + experimentName
                                + " and project id "
                                + projectId
                                + "and platform id"
                                + platformId
                                + "already exists");
            }

        return returnVal;

    }

    @Override
    public ExperimentDTO createExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {

        ExperimentDTO returnVal = experimentDTO;

        try {

            if (validateExperimentRequest(returnVal)) {
                Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
                Integer experimentId = rsExperimentDao.createExperiment(parameters);
                returnVal.setExperimentId(experimentId);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public ExperimentDTO replaceExperiment(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDtoMappingException {

        ExperimentDTO returnVal = experimentDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("experimentId", experimentId);
            rsExperimentDao.updateExperiment(parameters);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }
}
