package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.access.RsProjectDao;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.metamodel.EntityType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapNameIdListImpl implements DtoMapNameIdList {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);


    @Autowired
    private RsContactDao rsContactDao = null;

    @Autowired
    private RsProjectDao rsProjectDao = null;

    @Autowired
    private RsPlatformDao rsPlatformDao = null;

    private NameIdListDTO getNameIdListForContacts(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet contactList = rsContactDao.getContactNamesForRoleName(nameIdListDTO.getFilter());

            Map<String, String> contactNamesById = new HashMap<>();
            while (contactList.next()) {

                Integer contactId = contactList.getInt("contact_id");
                String lastName = contactList.getString("lastname");
                String firstName = contactList.getString("firstname");
                String name = lastName + ", " + firstName;
                contactNamesById.put(contactId.toString(), name);
            }

            returnVal.setNamesById(contactNamesById);

        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }


        return (returnVal);

    } // getNameIdListForContacts()

    private NameIdListDTO getNameIdListForPlatforms(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsPlatformDao.getPlatformNames();
            Map<String, String> platformNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer platformId = resultSet.getInt("platform_id");
                String platformName = resultSet.getString("name");
                platformNamesById.put(platformId.toString(), platformName);
            }


            returnVal.setNamesById(platformNamesById);


        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForProjects(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsProjectDao.getProjectNamesForContactId(Integer.parseInt(nameIdListDTO.getFilter()));

            Map<String, String> projectNameIdList = new HashMap<>();

            while (resultSet.next()) {
                Integer projectId = resultSet.getInt("project_id");
                String name = resultSet.getString("name").toString();
                projectNameIdList.put(projectId.toString(), name);
            }

            returnVal.setNamesById(projectNameIdList);
        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;

    } // getNameIdListForContacts()

    @Override
    public NameIdListDTO getNameIdList(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();


        if (nameIdListDTO.getEntityType() == NameIdListDTO.EntityType.DBTABLE) {
            switch (nameIdListDTO.getEntityName()) {

                case "contact":
                    returnVal = getNameIdListForContacts(nameIdListDTO);
                    break;

                case "project":
                    returnVal = getNameIdListForProjects(nameIdListDTO);
                    break;

                case "platform":
                    returnVal = getNameIdListForPlatforms(nameIdListDTO);
                    break;

                default:
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.Error,
                            "Unsupported entity for list request: " + nameIdListDTO.getEntityName());
            } // switch on entity name
        } else if (nameIdListDTO.getEntityType() == NameIdListDTO.EntityType.CVTERM) {

            // getNamedIdListFromCvList(nameIDListDTO) // getEntityName() == group name
            //select cv_id,term
            //from cv
            // where cv.group= ?
        }

        return returnVal;

    } // getNameIdList()

} // DtoMapNameIdListImpl
