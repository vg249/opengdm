package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class DtoMapExperimentImpl implements DtoMapExperiment {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);

    @Autowired
    private RsExperimentDao rsExperimentDao;

    public ExperimentDTO getExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {


        ExperimentDTO returnVal = new ExperimentDTO();

        try {

            ResultSet resultSet = rsExperimentDao.getExperimentDetailsForExperimentId(experimentDTO.getExperimentId());

            boolean retrievedOneRecord = false;
            while (resultSet.next()) {

                if (true == retrievedOneRecord) {
                    throw (new GobiiDtoMappingException("There are more than one project records for project id: " + experimentDTO.getExperimentId()));
                }

                retrievedOneRecord = true;

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }


        return returnVal;
    }

    @Override
    public ExperimentDTO createExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {

        ExperimentDTO returnVal = experimentDTO;

        try {
            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            Integer experimentId = rsExperimentDao.createExperiment(parameters);
            returnVal.setExperimentId(experimentId);
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return  returnVal;
    }

}
