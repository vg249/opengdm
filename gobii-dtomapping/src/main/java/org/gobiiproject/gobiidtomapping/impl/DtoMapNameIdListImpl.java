package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapNameIdListImpl implements DtoMapNameIdList {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);


    private Map<GobiiEntityNameType, DtoMapNameIdFetch> dtoMapNameIdFetchMap = new HashMap<>();

    public void setDtoMapNameIdFetchMap(Map<GobiiEntityNameType, DtoMapNameIdFetch> dtoMapNameIdFetchMap) {
        this.dtoMapNameIdFetchMap = dtoMapNameIdFetchMap;
    }

    @Override
    public List<NameIdDTO> getNameIdList(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        if (this.dtoMapNameIdFetchMap.containsKey(dtoMapNameIdParams.getEntityType())) {

            DtoMapNameIdFetch dtoMapNameIdFetch = this.dtoMapNameIdFetchMap.get(dtoMapNameIdParams.getEntityType());
            returnVal = dtoMapNameIdFetch.getNameIds(dtoMapNameIdParams);

        } else {
            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "There is no NameIDFetch instance to handle this entity type: " + dtoMapNameIdParams.getEntityType().toString());
        }

        return returnVal;
    }

} // DtoMapNameIdListImpl
