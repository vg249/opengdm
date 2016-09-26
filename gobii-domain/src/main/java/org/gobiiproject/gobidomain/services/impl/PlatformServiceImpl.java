package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.PlatformService;
import org.gobiiproject.gobiidtomapping.DtoMapPlatform;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/27/2016.
 */
public class PlatformServiceImpl implements PlatformService {

    Logger LOGGER = LoggerFactory.getLogger(PlatformServiceImpl.class);

    @Autowired
    DtoMapPlatform dtoMapPlatform;

    @Override
    public PlatformDTO processPlatform(PlatformDTO platformDTO) {

        PlatformDTO returnVal = platformDTO;

        try {

            switch (returnVal.getGobiiProcessType()) {
                case CREATE:
                    returnVal = dtoMapPlatform.createPlatform(returnVal);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                case UPDATE:
                    returnVal = dtoMapPlatform.replacePlatform(returnVal.getPlatformId(),returnVal);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                default:
                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Unsupported procesCv type " + platformDTO.getGobiiProcessType().toString());

            } // switch()

        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }


        return returnVal;
    }

    @Override
    public List<PlatformDTO> getPlatforms() throws GobiiDomainException {

        List<PlatformDTO> returnVal;

        try {
            returnVal = dtoMapPlatform.getPlatforms();
//            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
//            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public PlatformDTO getPlatformById(Integer platformId) {

        PlatformDTO returnVal;

        try {
            returnVal = dtoMapPlatform.getPlatformDetails(platformId);
//            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
//            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified platformId ("
                                + platformId
                                + ") does not match an existing platform ");
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public PlatformDTO createPlatform(PlatformDTO platformDTO) throws GobiiDomainException {
        PlatformDTO returnVal;

        try {

            returnVal = dtoMapPlatform.createPlatform(platformDTO);

            // When we have roles and permissions, this will be set programmatically
//            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
//            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
        return returnVal;
    }

    @Override
    public PlatformDTO replacePlatform(Integer platformId, PlatformDTO platformDTO) throws GobiiDomainException {
        PlatformDTO returnVal;

        try {

            if (null == platformDTO.getPlatformId() ||
                    platformDTO.getPlatformId().equals(platformId)) {


                PlatformDTO existingPlatformDTO = dtoMapPlatform.getPlatformDetails(platformId);
                if (null != existingPlatformDTO.getPlatformId() && existingPlatformDTO.getPlatformId().equals(platformId)) {


                    returnVal = dtoMapPlatform.replacePlatform(platformId, platformDTO);
//                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
//                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified platformId ("
                                    + platformId
                                    + ") does not match an existing platform ");
                }

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The platformId specified in the dto ("
                                + platformDTO.getPlatformId()
                                + ") does not match the platformId passed as a parameter "
                                + "("
                                + platformId
                                + ")");

            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }


        return returnVal;
    }
}
