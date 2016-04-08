package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsContact;
import org.gobiiproject.gobiidao.resultset.access.RsProject;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapNameIdListImpl implements DtoMapNameIdList {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);


    @Autowired
    private RsContact rsContact = null;

    @Autowired
    private RsProject rsProject = null;

    private NameIdListDTO getNameIdListForContacts(NameIdListDTO nameIdListDTO) throws GobiiDaoException {

        NameIdListDTO returnVal = new NameIdListDTO();


        List<Map<String, Object>> contactList = rsContact.getContactNamesForRoleName(nameIdListDTO.getFilter());

        Map<String, String> contactNamesById = new HashMap<>();
        for (Map<String, Object> currentRow : contactList) {

            Integer contactId = (Integer) currentRow.get("contact_id");
            String lastName = currentRow.get("lastname").toString();
            String firstName = currentRow.get("firstname").toString();
            String name = lastName + ", " + firstName;
            contactNamesById.put(contactId.toString(), name);

        }

        returnVal.setNamesById(contactNamesById);

        return (returnVal);

    } // getNameIdListForContacts()

    private NameIdListDTO getNameIdListForProjects(NameIdListDTO nameIdListDTO) throws GobiiDaoException {

        NameIdListDTO returnVal = new NameIdListDTO();

        List<Map<String, Object>> projectList = rsProject.getProjectNamesForContactId(Integer.parseInt(nameIdListDTO.getFilter()));

        Map<String, String> projectNameIdList = new HashMap<>();
        for (Map<String, Object> currentRow : projectList) {
            Integer projectId = (Integer) currentRow.get("project_id");
            String name = currentRow.get("name").toString();
            projectNameIdList.put(projectId.toString(), name);
        }

        returnVal.setNamesById(projectNameIdList);

        return (returnVal);

    } // getNameIdListForContacts()

    @Override
    public NameIdListDTO getNameIdList(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {


            switch (nameIdListDTO.getEntityName()) {

                case "contact":
                    returnVal = getNameIdListForContacts(nameIdListDTO);
                    break;

                case "project":
                    returnVal = getNameIdListForProjects(nameIdListDTO);
                    break;

                default:
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.Error,
                            "Unsupported entity for list request: " + nameIdListDTO.getEntityName());
            } // switch on entity name
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;

    } // getNameIdList()

} // DtoMapNameIdListImpl
