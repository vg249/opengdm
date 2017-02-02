package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsCvGroupDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapCvGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public class DtoMapCvGroupImpl implements DtoMapCvGroup {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapCvGroupImpl.class);

    @Autowired
    private RsCvGroupDao rsCvGroupDao;

    @Transactional
    @Override
    public List<CvDTO> getCvsForGroup(Integer groupId) throws GobiiDtoMappingException {

        List<CvDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsCvGroupDao.getCvsByGroupId(groupId);

            while (resultSet.next()) {

                CvDTO currentCvDTO = new CvDTO();
                currentCvDTO.setCvId(resultSet.getInt("cv_id"));
                currentCvDTO.setGroup(resultSet.getInt("group"));
                currentCvDTO.setGroupId(resultSet.getInt("group"));
                currentCvDTO.setTerm(resultSet.getString("term"));
                currentCvDTO.setDefinition(resultSet.getString("definition"));
                currentCvDTO.setRank(resultSet.getInt("rank"));
                returnVal.add(currentCvDTO);

            }


        } catch (SQLException e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw  new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

}
