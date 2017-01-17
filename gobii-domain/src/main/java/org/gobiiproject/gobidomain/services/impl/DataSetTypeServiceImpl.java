package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DataSetTypeService;
import org.gobiiproject.gobiidtomapping.DtoMapDataSetType;
import org.gobiiproject.gobiimodel.dto.container.DataSetTypeDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 2017-01-05.
 */
public class DataSetTypeServiceImpl implements DataSetTypeService {

    Logger LOGGER = LoggerFactory.getLogger(DataSetTypeServiceImpl.class);


    @Autowired
    DtoMapDataSetType dtoMapDataSetType = null;

    @Override
    public List<DataSetTypeDTO> getDataSetTypes() throws GobiiDomainException {

        List<DataSetTypeDTO> returnVal;

        try {
            returnVal = dtoMapDataSetType.getDataSetTypes();

            for (DataSetTypeDTO currentDataSetTypeDTO : returnVal) {
                currentDataSetTypeDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            }

            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }



}
