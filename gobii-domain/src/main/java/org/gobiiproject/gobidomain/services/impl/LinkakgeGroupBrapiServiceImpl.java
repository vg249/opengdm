package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.LinkageGroupBrapiService;
import org.gobiiproject.gobidomain.services.MapsetBrapiService;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapLinkageGroupBrApi;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapMapsetBrApi;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.LinkageGroupBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LinkakgeGroupBrapiServiceImpl implements LinkageGroupBrapiService {

    Logger LOGGER = LoggerFactory.getLogger(LinkakgeGroupBrapiServiceImpl.class);

    @Autowired
    private DtoMapLinkageGroupBrApi dtoMapLinkageGroupBrApi;

    /**
     * Gets the list of LinkageGroups in the database by Page Number and Page Size
     * @param pageNum - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return List of Brapi Specified Genome Maps DTO
     * @throws GobiiDomainException
     */
    @Override
    public List<LinkageGroupBrapiDTO> getLinkageGroups(
            Integer pageNum, Integer pageSize) throws GobiiDomainException {

        List<LinkageGroupBrapiDTO> returnVal = new ArrayList<>();

        try {

            return returnVal;

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

    }

}
