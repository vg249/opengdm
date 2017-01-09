package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.DataSetTypeDTO;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gobiiproject.gobiidtomapping.DtoMapDataSetType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 2017-01-04.
 */
public class DtoMapDataSetTypeImpl implements DtoMapDataSetType{

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetTypeImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public List<DataSetTypeDTO> getDataSetTypes() throws GobiiDtoMappingException {

        List<DataSetTypeDTO> returnVal = new ArrayList<>();
        List<String> dataSetTypeNames;

        try {

            dataSetTypeNames = getDataSetTypeNames();
            Integer dummyId = 0;
            for (String name : dataSetTypeNames) {
                DataSetTypeDTO tempDataSetTypeDTO = new DataSetTypeDTO();
                tempDataSetTypeDTO.setDataSetTypeName(name);
                tempDataSetTypeDTO.setId(dummyId);
                dummyId++;

                returnVal.add(tempDataSetTypeDTO);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    public List<String> getDataSetTypeNames(){

        List<String> returnVal = new ArrayList<String>();

        for (DataSetType type : DataSetType.values()){
            returnVal.add(type.toString());
        }

        return returnVal;

    }


}
