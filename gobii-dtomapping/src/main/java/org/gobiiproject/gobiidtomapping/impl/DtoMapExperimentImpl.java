package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;

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

                int experimentId = resultSet.getInt("experiment_id");
                String experimentName = resultSet.getString("name");
                String experimentCode = resultSet.getString("code");
                String experimentDataFile = resultSet.getString("data_file");
                int platformId =  resultSet.getInt("platform_id");
                int manifestId = resultSet.getInt("manifest_id");
                int projectId = resultSet.getInt("project_id");

                returnVal.setExperimentId(experimentId);
                returnVal.setExperimentName(experimentName);
                returnVal.setExperimentCode(experimentCode);
                returnVal.setExperimentDataFile(experimentDataFile);
                returnVal.setManifestId(manifestId);
                returnVal.setPlatformId(platformId);
                returnVal.setProjectId(projectId);
                
            }
        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }


        return returnVal;
    }
    
//    
//    public ExperimentDTO getExperimentNamesByProjectId(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {
//
//
//    	ExperimentDTO returnVal = new ExperimentDTO();
//
//        try {
//
//            ResultSet resultSet = rsExperimentDao.getExperimentNamesByProjectId(experimentDTO.getExperimentId());
//
//            boolean retrievedOneRecord = false;
//            while (resultSet.next()) {
//
//                if (true == retrievedOneRecord) {
//                    throw (new GobiiDtoMappingException("There are more than one project records for project id: " + experimentDTO.getExperimentId()));
//                }
//                
//                retrievedOneRecord = true;
//
//                int experimentId = resultSet.getInt("experiment_id");
//                String experimentName = resultSet.getString("name");
//                String experimentCode = resultSet.getString("code");
//                String experimentDataFile = resultSet.getString("data_file");
//
//                returnVal.setExperimentId(experimentId);
//                returnVal.setExperimentName(experimentName);
//                returnVal.setExperimentCode(experimentCode);
//                returnVal.setExperimentDataFile(experimentDataFile);
//            }
//        } catch (SQLException e) {
//            returnVal.getDtoHeaderResponse().addException(e);
//            LOGGER.error(e.getMessage());
//        } catch (GobiiDaoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//
//        return returnVal;
//    }
}
