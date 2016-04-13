package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsDisplayDao;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;

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


            ResultSet resultSet = rsDisplayDao.getDisplayColumns(displayDTO.getTableName());


            while (resultSet.next()) {
                String columnName = resultSet.getString("column_name");
                String displayName = resultSet.getString("display_name");
                returnVal.getDisplayNamesByColumn().put(columnName, displayName);
            }

            returnVal.setTableName(displayDTO.getTableName());

        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }


//
//    private NameIdListDTO getNameIdListForContacts(NameIdListDTO nameIdListDTO) {
//
//        NameIdListDTO returnVal = new NameIdListDTO();
//
//        try {
//
//            ResultSet contactList = rsContact.getContactNamesForRoleName(nameIdListDTO.getFilter());
//
//            Map<String, String> contactNamesById = new HashMap<>();
//            while (contactList.next()) {
//
//                Integer contactId = contactList.getInt("contact_id");
//                String lastName = contactList.getString("lastname");
//                String firstName = contactList.getString("firstname");
//                String name = lastName + ", " + firstName;
//                contactNamesById.put(contactId.toString(), name);
//
//            }
//
//            returnVal.setNamesById(contactNamesById);
//
//        } catch (SQLException e) {
//            returnVal.getDtoHeaderResponse().addException(e);
//            LOGGER.error(e.getMessage());
//        } catch (GobiiDaoException e) {
//            returnVal.getDtoHeaderResponse().addException(e);
//            LOGGER.error(e.getMessage());
//        }
//
//
//        return (returnVal);
//
//    } // getNameIdListForContacts()
//
//

} // DtoMapNameIdListImpl
