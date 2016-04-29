package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
import org.gobiiproject.gobiidao.resultset.access.RsDisplayDao;
import org.gobiiproject.gobiidtomapping.DtoMapCv;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapCvImpl implements DtoMapCv {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapCvImpl.class);


    @Autowired
    private RsCvDao rsCvDao = null;

    @Override
    public CvDTO getCvNames(CvDTO cvDTO) throws GobiiDtoMappingException {

        CvDTO returnVal = new CvDTO();

        try {

            ResultSet resultSet = rsCvDao.getDetailsForCvId(cvDTO.getCv_id());

            boolean retrievedOneRecord = false;
            while (resultSet.next()) {

                if (true == retrievedOneRecord) {
                    throw (new GobiiDtoMappingException("There are more than one cv records for cv id: " +cvDTO.getCv_id()));
                }
                
                retrievedOneRecord = true;

                int cvId = resultSet.getInt("cv_id");
                String cvGroup = resultSet.getString("group");
                String cvTerm = resultSet.getString("term");
                String cvDefinition = resultSet.getString("definition");
                int cvRank =  resultSet.getInt("rank");

                returnVal.setCv_id(cvId);
                returnVal.setDefinition(cvTerm);
                returnVal.setGroup(cvGroup);
                returnVal.setRank(cvRank);
                returnVal.setTerm(cvTerm);
            }
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }
        catch (SQLException e) {
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
