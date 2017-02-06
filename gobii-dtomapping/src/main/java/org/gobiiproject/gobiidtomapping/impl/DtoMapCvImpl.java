package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapCv;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapCvImpl implements DtoMapCv {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapCvImpl.class);


    @Autowired
    private RsCvDao rsCvDao = null;

    @Override
    public List<CvDTO> getCvs() throws GobiiDtoMappingException {

        List<CvDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsCvDao.getCvNames();
            while (resultSet.next()) {
                CvDTO currentCvDTO = new CvDTO();
                currentCvDTO.setTerm(resultSet.getString("term"));
                currentCvDTO.setCvId(resultSet.getInt("cv_id"));
                returnVal.add(currentCvDTO);
            }
        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping error" ,e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public CvDTO getCvDetails(Integer cvId) throws GobiiDtoMappingException {

        CvDTO returnVal = new CvDTO();

        try {

            ResultSet resultSet = rsCvDao.getDetailsForCvId(cvId);

            if (resultSet.next()) {
                // apply cv values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }


    @Override
    public CvDTO createCv(CvDTO cvDTO) throws GobiiDtoMappingException {
        CvDTO returnVal = cvDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            Integer cvId = rsCvDao.createCv(parameters);
            returnVal.setCvId(cvId);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public CvDTO replaceCv(Integer cvId, CvDTO cvDTO) throws GobiiDtoMappingException {

        CvDTO returnVal = cvDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("cvId", cvId);
            rsCvDao.updateCv(parameters);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public CvDTO deleteCv(CvDTO cvDTO) throws GobiiDtoMappingException {

        CvDTO returnVal = cvDTO;

        try {

            returnVal.setEntityStatus(0);
            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsCvDao.deleteCv(parameters);

            returnVal.setCvId(-1);
            returnVal.setGroupId(null);
            returnVal.setXrefId(null);
            returnVal.setTerm(null);
            returnVal.setAbbreviation(null);
            returnVal.setDefinition(null);
            returnVal.setRank(null);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }
} // DtoMapNameIdListImpl
