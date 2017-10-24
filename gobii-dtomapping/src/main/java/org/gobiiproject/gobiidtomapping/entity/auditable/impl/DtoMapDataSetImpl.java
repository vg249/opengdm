package org.gobiiproject.gobiidtomapping.entity.auditable.impl;

import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoMapDataSetImpl implements DtoMapDataSet {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    @Autowired
    private RsDataSetDao rsDataSetDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @SuppressWarnings("unchecked")
    @Override
    public List<DataSetDTO> getList() throws GobiiDtoMappingException {

        List<DataSetDTO> returnVal = new ArrayList<>();

        try {

            returnVal = (List<DataSetDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_DATASET_ALL,null);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public List<DataSetDTO> getByTypeId(Integer typeId) throws GobiiDtoMappingException {

        List<DataSetDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsDataSetDao.getDataSetsByTypeId(typeId);

            while(resultSet.next()) {

                DataSetDTO currentDataSetDTO = new DataSetDTO();

                currentDataSetDTO.setName(resultSet.getString("name"));
                currentDataSetDTO.setDataSetId(resultSet.getInt("dataset_id"));
                currentDataSetDTO.setTypeId(resultSet.getInt("type_id"));

                returnVal.add(currentDataSetDTO);

            }


        } catch (SQLException e) {

            throw new GobiiDtoMappingException(e);

        }

        return returnVal;


    }

    @Transactional
    @Override
    public DataSetDTO get(Integer dataSetId) throws GobiiDtoMappingException {

        DataSetDTO returnVal = new DataSetDTO();


        try {
            ResultSet resultSet = rsDataSetDao.getDataSetDetailsByDataSetId(dataSetId);

            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }
        } catch(SQLException e) {
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public DataSetDTO create(DataSetDTO dataSetDTO) throws GobiiDtoMappingException {

        DataSetDTO returnVal = dataSetDTO;


        Map<String, Object> parameters = ParamExtractor.makeParamVals(dataSetDTO);
        Integer datasetId = rsDataSetDao.createDataset(parameters);
        returnVal.setDataSetId(datasetId);

        return returnVal;
    }

    @Override
    public DataSetDTO replace(Integer dataSetId, DataSetDTO dataSetDTO) throws GobiiDtoMappingException {

        DataSetDTO returnVal = dataSetDTO;


        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("projectId", dataSetId);
        rsDataSetDao.updateDataSet(parameters);


        return returnVal;
    }


}
