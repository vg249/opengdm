package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapCv;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapCvGroup;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapEntityStats;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvGroupDTO;
import org.gobiiproject.gobiimodel.dto.system.EntityStatsDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
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
public class DtoMapEntityStatsImpl implements DtoMapEntityStats {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapEntityStatsImpl.class);




    @Override
    public EntityStatsDTO getEntityCount(GobiiEntityNameType gobiiEntityNameType) throws GobiiDtoMappingException {

        EntityStatsDTO returnVal = new EntityStatsDTO();

        try {
//            ResultSet resultSet = rsCvDao.getCvNames();
//            while (resultSet.next()) {
//                CvDTO currentCvDTO = new CvDTO();
//                currentCvDTO.setTerm(resultSet.getString("term"));
//                currentCvDTO.setCvId(resultSet.getInt("cv_id"));
//                currentCvDTO.setGroupType(resultSet.getInt("group_type"));
//                returnVal.add(currentCvDTO);
//            }
        } catch (Exception e) {
//            LOGGER.error("Gobii Mapping error", e);
//            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

} // DtoMapNameIdListImpl
