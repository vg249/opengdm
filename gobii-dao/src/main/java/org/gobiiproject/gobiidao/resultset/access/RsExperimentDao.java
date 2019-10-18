package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Angel on 4/19/2016.
 */
public interface RsExperimentDao {


    ResultSet getExperimentNamesByProjectId(Integer experimentId) throws GobiiDaoException;

    ResultSet getExperimentDetailsForExperimentId(int experimentId) throws GobiiDaoException;

    ResultSet getExperimentNames() throws GobiiDaoException;

    Integer createExperiment(Map<String, Object> parameters) throws GobiiDaoException;

    void updateExperiment(Map<String, Object> parameters) throws GobiiDaoException;

    ResultSet getExperimentsByNameProjectid(String experimentName,
                                            Integer projectId) throws GobiiDaoException;

    ResultSet getExperimentsByProjectIdForLoadedDatasets(Integer projectId) throws GobiiDaoException;


}
