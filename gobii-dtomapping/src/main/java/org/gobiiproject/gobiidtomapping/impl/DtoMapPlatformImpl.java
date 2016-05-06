package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapPlatform;
import org.gobiiproject.gobiidtomapping.DtoMapPlatform;
import org.gobiiproject.gobiidtomapping.DtoMapPlatform;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoMapPlatformImpl implements DtoMapPlatform {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapPlatformImpl.class);


    @Autowired
    private RsPlatformDao rsPlatformDao;

    @Override
    public PlatformDTO getPlatformDetails(PlatformDTO platformDTO) throws GobiiDtoMappingException {

        PlatformDTO returnVal = platformDTO;

        try {

            ResultSet resultSet = rsPlatformDao.getPlatformDetailsByPlatformId(platformDTO.getPlatformId());

            if (resultSet.next()) {

                // apply platform values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);


            } // if result set has a row

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;

    } // getPlatformDetails()

    @Override
    public PlatformDTO createPlatform(PlatformDTO platformDTO) throws GobiiDtoMappingException {
        PlatformDTO returnVal = platformDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(platformDTO);
            Integer platformId = rsPlatformDao.createPlatform(parameters);
            returnVal.setPlatformId(platformId);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;
    }


    @Override
    public PlatformDTO updatePlatform(PlatformDTO platformDTO) throws GobiiDtoMappingException {

        PlatformDTO returnVal = platformDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsPlatformDao.updatePlatform(parameters);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }
}
