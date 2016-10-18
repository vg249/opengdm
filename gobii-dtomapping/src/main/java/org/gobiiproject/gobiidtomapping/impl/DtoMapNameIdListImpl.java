package org.gobiiproject.gobiidtomapping.impl;

import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiidao.resultset.access.*;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.container.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.ArrayList;
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




    @Autowired
    private RsRoleDao rsRoleDao = null;








    private NameIdListDTO getNameIdListForRole(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsRoleDao.getContactRoleNames();
            List<NameIdDTO> listDTO = new ArrayList<>();

            NameIdDTO nameIdDTO;
            while (resultSet.next()) {
                nameIdDTO = new NameIdDTO();
                nameIdDTO.setId(resultSet.getInt("role_id"));
                nameIdDTO.setName(resultSet.getString("role_name"));
                listDTO.add(nameIdDTO);
            }


            returnVal.setNamesById(listDTO);


        } catch (Exception e) {
            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }









    @Override
    public NameIdListDTO getNameIdList(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = nameIdListDTO;

        if (nameIdListDTO.getEntityType() == NameIdListDTO.EntityType.DBTABLE) {

            switch (nameIdListDTO.getEntityName().toLowerCase()) {

                case "role":
                    returnVal = getNameIdListForRole(nameIdListDTO);
                    break;

                default:
                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            "Unsupported entity for list request: " + nameIdListDTO.getEntityName());
            }

        }

        return returnVal;

    } // getNameIdList()

    @Override
    public List<NameIdDTO> getNameIdList(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        if (this.dtoMapNameIdFetchMap.containsKey(dtoMapNameIdParams.getEntityType())) {

            returnVal = this.dtoMapNameIdFetchMap.get(dtoMapNameIdParams.getEntityType()).getNameIds(dtoMapNameIdParams);

        } else {
            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "There is no NameIDFetch instance to handle this entity type: " + dtoMapNameIdParams.getEntityType().toString());
        }

        return returnVal;
    }


} // DtoMapNameIdListImpl
