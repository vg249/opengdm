package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobidomain.services.EntityStatsService;
import org.gobiiproject.gobiidtomapping.auditable.DtoMapAnalysis;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapEntityStats;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.system.EntityStatsDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class EntityStatsServiceImpl implements EntityStatsService {

    Logger LOGGER = LoggerFactory.getLogger(EntityStatsServiceImpl.class);

    @Autowired
    DtoMapEntityStats dtoMapEntityStats;

    @Override
    public EntityStatsDTO getEntityCount(GobiiEntityNameType gobiiEntityNameType) throws GobiiDomainException {

        EntityStatsDTO returnVal;

        try {

            returnVal = dtoMapEntityStats.getEntityCount(gobiiEntityNameType);

            // When we have roles and permissions, this will be set programmatically
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }


}
