package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ProtocolService;
import org.gobiiproject.gobiidtomapping.DtoMapProtocol;
import org.gobiiproject.gobiimodel.dto.container.ProtocolDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public class ProtocolServiceImpl implements ProtocolService {

    Logger LOGGER = LoggerFactory.getLogger(ProtocolServiceImpl.class);

    @Autowired
    DtoMapProtocol dtoMapProtocol = null;

    @Override
    public ProtocolDTO createProtocol(ProtocolDTO protocolDTO) throws GobiiDomainException {

        ProtocolDTO returnVal;

        try {

            returnVal = dtoMapProtocol.createProtocol(protocolDTO);

            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        } catch (Exception e){

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }
        return returnVal;

    }

    @Override
    public ProtocolDTO replaceProtocol(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDomainException {

        ProtocolDTO returnVal;

        try {

            if (null == protocolDTO.getProtocolId() ||
                    protocolDTO.getProtocolId().equals(protocolId)) {


                ProtocolDTO existingProtocolDTO = dtoMapProtocol.getProtocolDetails(protocolId);
                if (null != existingProtocolDTO.getProtocolId() && existingProtocolDTO.getProtocolId().equals(protocolId)) {


                    returnVal = dtoMapProtocol.replaceProtocol(protocolId, protocolDTO);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified contactId ("
                                    + protocolId
                                    + ") does not match an existing contact ");
                }

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The contactId specified in the dto ("
                                + protocolDTO.getProtocolId()
                                + ") does not match the contactId passed as a parameter "
                                + "("
                                + protocolId
                                + ")");

            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }

}
