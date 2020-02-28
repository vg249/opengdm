package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.brapi.LinkageGroupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LinkakgeGroupServiceImpl implements LinkageGroupService {

    Logger LOGGER = LoggerFactory.getLogger(LinkakgeGroupServiceImpl.class);

    /**
     * Gets the list of LinkageGroups in the database by Page Number and Page Size
     * @param pageNum - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return List of Brapi Specified Linkage Group DTO
     * @throws GobiiDomainException
     */
    @Override
    public List<LinkageGroupDTO> getLinkageGroups(
            Integer pageNum, Integer pageSize) throws GobiiDomainException {

        List<LinkageGroupDTO> returnVal = new ArrayList<>();

        try {

            return returnVal;

        } catch (Exception e) {

            LOGGER.error("Gobii service error: Unknown system error. ", e);
            throw new GobiiDomainException(e);

        }

    }

    /**
     * Gets the list of LinkageGroups in the database by MapId, Page Number and Page Size
     * @param mapId - Linkage Group Id
     * @param pageNum - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return List of Brapi Linkage Group DTO
     * @throws GobiiDomainException
     */
    @Override
    public List<LinkageGroupDTO> getLinkageGroupsByMapId(Integer mapId,
                                                         Integer pageNum, Integer pageSize) throws GobiiDomainException {

        List<LinkageGroupDTO> returnVal = new ArrayList<>();

        try {

            return returnVal;

        } catch (Exception e) {

            LOGGER.error("Gobii service error: Unknown system error", e);
            throw new GobiiDomainException(e);

        }

    }

}
