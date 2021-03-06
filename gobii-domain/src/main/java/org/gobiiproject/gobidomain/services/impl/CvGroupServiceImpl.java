package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapCvGroup;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvGroupDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gobiiproject.gobidomain.services.CvGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public class CvGroupServiceImpl implements CvGroupService {

    Logger LOGGER = LoggerFactory.getLogger(CvGroupServiceImpl.class);

    @Autowired
    DtoMapCvGroup dtoMapCvGroup = null;

    @Override
    public List<CvDTO> getCvsForGroup(Integer groupId) throws GobiiDtoMappingException {

        List<CvDTO> returnVal;

        returnVal = dtoMapCvGroup.getCvsForGroup(groupId);

        for (CvDTO currentCvDTO : returnVal) {

            currentCvDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
        }

        return returnVal;

    }

    @Override
    public List<CvGroupDTO> getCvsForType(GobiiCvGroupType gobiiCvGroupType) throws GobiiDomainException {

        List<CvGroupDTO> returnVal;

        returnVal = dtoMapCvGroup.getCvGroupsForType(gobiiCvGroupType);

        for (CvGroupDTO currentCvDTO : returnVal) {

            currentCvDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
        }

        return returnVal;
    }

    @Override
    public CvGroupDTO getCvGroupDetailsByGroupName(String groupName, Integer cvGroupTypeId) throws GobiiDomainException {

        CvGroupDTO returnVal;

        returnVal = dtoMapCvGroup.getCvGroupDetailsByGroupName(groupName, cvGroupTypeId);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        if (null == returnVal) {
            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The specified cv group name (" +
                            groupName + ") does not match an existig cv group");
        }

        return returnVal;

    }

}
