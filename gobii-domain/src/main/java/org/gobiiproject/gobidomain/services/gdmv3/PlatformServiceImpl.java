package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.gdmv3.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Platform;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.PlatformDao;
import org.springframework.beans.factory.annotation.Autowired;

public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private CvDao cvDao;

    @Transactional
    @Override
    public PlatformDTO createPlatform(PlatformDTO request, String createdBy) throws Exception {

        // Get Cv
        Cv platformType = cvDao.getCvByCvId(request.getPlatformTypeId());
        if (platformType == null || // no platform found
                !platformType.getCvGroup().getCvGroupName().equals(CvGroup.CVGROUP_PLATFORM_TYPE.getCvGroupName()) // incorrect
                                                                                                                   // cv
                                                                                                                   // type
        ) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid platform type");
        }

        Platform platform = new Platform();
        platform.setPlatformName(request.getPlatformName());
        platform.setType(platformType);

        // code?
        platform.setPlatformCode(request.getPlatformName().replace(' ', '_'));
        // Audit
        Contact creator = contactDao.getContactByUsername(createdBy);
        if (creator != null)
            platform.setCreatedBy(creator.getContactId());
        platform.setCreatedDate(new java.util.Date());
        // status
        Cv newStatus = cvDao.getNewStatus();
        platform.setStatus(newStatus);

        Platform createdPlatform = platformDao.createPlatform(platform);

        PlatformDTO platformDTO = new PlatformDTO();
        ModelMapper.mapEntityToDto(createdPlatform, platformDTO);
        return platformDTO;
    }

    @Transactional
    @Override
    public PagedResult<PlatformDTO> getPlatforms(Integer page, Integer pageSize, Integer platformTypeId) throws Exception {
        Integer offset = page * pageSize;

        List<Platform> platforms = platformDao.getPlatforms(offset, pageSize, platformTypeId);
        List<PlatformDTO> platformDTOs = new ArrayList<>();
        
        platforms.forEach(platform -> {
            PlatformDTO platformDTO = new PlatformDTO();
            ModelMapper.mapEntityToDto(platform, platformDTO);
            platformDTOs.add(platformDTO);
        });

        return PagedResult.createFrom(page, platformDTOs);
    }

    @Transactional
    @Override
    public PlatformDTO getPlatform(Integer platformId) throws Exception {
        Platform platform = platformDao.getPlatform(platformId);
        if (platform == null) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Not found");
        }
        
        PlatformDTO platformDTO = new PlatformDTO();
        ModelMapper.mapEntityToDto(platform, platformDTO);
        return platformDTO;
    }
    
}