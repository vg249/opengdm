package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.container.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public class NameIdListServiceImpl implements NameIdListService {


    private Logger LOGGER = LoggerFactory.getLogger(NameIdListServiceImpl.class);

    @Autowired
    DtoMapNameIdList dtoMapNameIdList;

    @Override
    public List<NameIdDTO> getNameIdList(DtoMapNameIdParams dtoMapNameIdParams)  throws GobiiException {

            return dtoMapNameIdList.getNameIdList(dtoMapNameIdParams);
    }
}
