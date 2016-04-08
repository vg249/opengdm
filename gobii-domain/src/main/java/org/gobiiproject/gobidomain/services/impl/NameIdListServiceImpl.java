package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/6/2016.
 */
public class NameIdListServiceImpl implements NameIdListService {

    @Autowired
    DtoMapNameIdList dtoMapNameIdList;

    @Override
    public NameIdListDTO getNameIdList(NameIdListDTO nameIdListDTO) {
        return dtoMapNameIdList.getNameIdList(nameIdListDTO);
    }
}
