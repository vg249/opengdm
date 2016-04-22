package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/21/2016.
 */
public class DataSetServiceImpl implements DataSetService {

    Logger LOGGER = LoggerFactory.getLogger(DataSetServiceImpl.class);


    @Autowired
    DtoMapDataSet dtoMapDataSet = null;

    @Override
    public DataSetDTO getDataSetDetails(DataSetDTO dataSetDTO) {

        DataSetDTO returnVal = new DataSetDTO();

        try {
            returnVal = dtoMapDataSet.getDataSetDetails(dataSetDTO);

        } catch (GobiiDtoMappingException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }
}
