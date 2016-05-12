package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsDisplayDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
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
public class DtoMapDisplayImpl implements DtoMapDisplay {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDisplayImpl.class);
    @Autowired
    private RsDisplayDao rsDisplayDao = null;

    @Override
    public DisplayDTO getDisplayNames(DisplayDTO displayDTO) {

        DisplayDTO returnVal = new DisplayDTO();

        try {

            String tableName = "";
            List<TableColDisplay> colDisplay = new ArrayList<TableColDisplay>();
            ResultSet resultSet = rsDisplayDao.getTableDisplayNames();

            while (resultSet.next()) {

                returnVal.setDisplayId(resultSet.getInt("display_id"));
                String newTableName = resultSet.getString("table_name");

                if (tableName.equals("")) { //if first table name from query
                    tableName = newTableName; //set table name if first table name frm query
                } else if (!tableName.equals(newTableName)) { //if new name
                    returnVal.getTableNamesWithColDisplay().put(tableName, colDisplay);
                    colDisplay = new ArrayList<TableColDisplay>();
                    tableName = newTableName;
                }

                TableColDisplay tableColDisplay = new TableColDisplay();
                tableColDisplay.setColumnName(resultSet.getString("column_name"));
                tableColDisplay.setDisplayName(resultSet.getString("display_name"));
                colDisplay.add(tableColDisplay);
            }

            //            returnVal.setTableName(displayDTO.);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }


    @Override
    public DisplayDTO createDisplay(DisplayDTO contactDTO) throws GobiiDtoMappingException {
        DisplayDTO returnVal = contactDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(contactDTO);
            Integer contactId = rsDisplayDao.createDisplay(parameters);
            returnVal.setDisplayId(contactId);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;
    }

    @Override
    public DisplayDTO updateDisplay(DisplayDTO contactDTO) throws GobiiDtoMappingException {

        DisplayDTO returnVal = contactDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsDisplayDao.updateDisplay(parameters);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }

} // DtoMapNameIdListImpl
