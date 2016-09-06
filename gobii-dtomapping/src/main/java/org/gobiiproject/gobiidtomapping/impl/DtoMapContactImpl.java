package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class DtoMapContactImpl implements DtoMapContact {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapContactImpl.class);


    @Autowired
    private RsContactDao rsContactDao;

    @Transactional
    @Override
    public ResultEnvelope<ContactDTO> getContactDetails(ContactDTO contactDTO) throws GobiiDtoMappingException {

        ResultEnvelope<ContactDTO> returnVal = new ResultEnvelope<>();

        try {

            ResultSet resultSet = rsContactDao.getContactDetailsByContactId(contactDTO.getContactId());

            if (resultSet.next()) {

                // apply contact values
                ResultColumnApplicator.applyColumnValues(resultSet, contactDTO);

            } // iterate resultSet

            returnVal.getResult().getData().add(contactDTO);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public ResultEnvelope<ContactDTO> createContact(ContactDTO contactDTO) throws GobiiDtoMappingException {

        ResultEnvelope<ContactDTO> returnVal = new ResultEnvelope<>();

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(contactDTO);
            Integer contactId = rsContactDao.createContact(parameters);
            contactDTO.setContactId(contactId);
            returnVal.getResult().getData().add(contactDTO);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public ResultEnvelope<ContactDTO> updateContact(ContactDTO contactDTO) throws GobiiDtoMappingException {

        ResultEnvelope<ContactDTO> returnVal = new ResultEnvelope<>();

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(contactDTO);
            rsContactDao.updateContact(parameters);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
