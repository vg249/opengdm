package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Phil on 4/21/2016.
 */
public interface RsDataSetDao {

    ResultSet getDatasetNamesByExperimentId(Integer experimentId) throws GobiiDaoException;
    ResultSet getDataSetDetailsByDataSetId(Integer projectId) throws GobiiDaoException;
    Integer createDataset(Map<String,Object> parameters) throws GobiiDaoException;
    void updateDataSet(Map<String,Object> parameters) throws GobiiDaoException;
	ResultSet getDatasetNames() throws GobiiDaoException;
	ResultSet getDataSetsByTypeId(Integer typeId) throws GobiiDaoException;


}
