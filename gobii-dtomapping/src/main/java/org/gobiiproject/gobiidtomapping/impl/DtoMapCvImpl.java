package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
import org.gobiiproject.gobiidao.resultset.access.RsDisplayDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapCv;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.entity.CvItem;
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
    public CvDTO getCvDetails(CvDTO cvDTO) throws GobiiDtoMappingException {

        CvDTO returnVal = new CvDTO();

        try {

            ResultSet resultSet = rsCvDao.getDetailsForCvId(cvDTO.getCvId());

            if (resultSet.next()) {
                // apply cv values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

            if (cvDTO.isIncludeDetailsList()) {
                ResultSet cvItemsResultSet = rsCvDao.getAllCvItems();
                String currentGroupName = "";
                while (cvItemsResultSet.next()) {

                    String newGroupName = cvItemsResultSet.getString("group");

                    if (!currentGroupName.equals(newGroupName)) {
                        currentGroupName = newGroupName; //set Group name if first Group name frm query
                        returnVal.getGroupCvItems().put(currentGroupName, new ArrayList<>());
                    }

                    CvItem currentGroupCvItem = new CvItem();
                    currentGroupCvItem.setRank(cvItemsResultSet.getInt("rank"));
                    currentGroupCvItem.setTerm(cvItemsResultSet.getString("term"));
                    currentGroupCvItem.setDefinition(cvItemsResultSet.getString("definition"));
                    returnVal.getGroupCvItems().get(currentGroupName).add(currentGroupCvItem);

                }
            }
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;
    }


    @Override
    public CvDTO createCv(CvDTO cvDTO) throws GobiiDtoMappingException {
        CvDTO returnVal = cvDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(cvDTO);
            Integer cvId = rsCvDao.createCv(parameters);
            returnVal.setCvId(cvId);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;
    }

    @Override
    public CvDTO updateCv(CvDTO cvDTO) throws GobiiDtoMappingException {

        CvDTO returnVal = cvDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsCvDao.updateCv(parameters);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }
} // DtoMapNameIdListImpl
