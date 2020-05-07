package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.entity.Reference;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.MapsetDao;
import org.gobiiproject.gobiisampletrackingdao.ReferenceDao;
import org.springframework.beans.factory.annotation.Autowired;

public class MapsetServiceImpl implements MapsetService {

    @Autowired
    private MapsetDao mapsetDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ReferenceDao referenceDao;

    @Autowired
    private ContactDao contactDao;

    @Transactional
    @Override
    public PagedResult<MapsetDTO> getMapsets(Integer page, Integer pageSize, Integer mapsetTypeId) throws Exception {
        Integer offset = page * pageSize;
        List<Mapset> mapsets = mapsetDao.getMapsets(pageSize, offset, mapsetTypeId);
        List<MapsetDTO> mapsetDTOs = new ArrayList<>();
        mapsets.forEach(mapset -> {
            MapsetDTO mapsetDTO = new MapsetDTO();
            ModelMapper.mapEntityToDto(mapset, mapsetDTO);
            mapsetDTOs.add(mapsetDTO);
        });

        PagedResult<MapsetDTO> result = new PagedResult<>();
        result.setCurrentPageNum(page);
        result.setCurrentPageSize(mapsets.size());
        result.setResult(mapsetDTOs);
        
        return result;
    }

    @Transactional
    @Override
    public MapsetDTO createMapset(MapsetDTO mapset, String createdBy) throws Exception {
        //check if mapset has mapsetTypeId and if it's a valid mapsetTypeId
        Integer mapsetTypeId = mapset.getMapsetTypeId();

        Cv type = cvDao.getCvByCvId(mapsetTypeId);
        if (type == null) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST, "Mapset type not found");
        }

        //check type is correct type
        if (!type.getCvGroup().getCvGroupName().equals(CvGroup.CVGROUP_MAPSET_TYPE.getCvGroupName())) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST, "Invalid type");
        }

        //check the referenceId
        Reference reference = null;
        if (mapset.getReferenceId() != null) {
            reference = referenceDao.getReference(mapset.getReferenceId());
            if (reference == null) {
                throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST, "Unknown reference");
            }
        }

        Mapset createdMapset = new Mapset();
        createdMapset.setMapsetName(mapset.getMapsetName());
        if (mapset.getMapsetDescription() != null) createdMapset.setMapSetDescription(mapset.getMapsetDescription());
        createdMapset.setType(type);
        createdMapset.setReference(reference);
        // audit items
        Contact creator = contactDao.getContactByUsername(createdBy);
        if (creator != null)
            createdMapset.setCreatedBy(creator.getContactId());
        createdMapset.setCreatedDate(new java.util.Date());

        //other required colums
        createdMapset.setMapSetCode(
            String.format("%s_%s", type.getTerm(), createdMapset.getMapsetName()).replace(' ',  '_')
        );

        //status
        Cv status = cvDao.getNewStatus();
        createdMapset.setStatus(status.getCvId()); //TODO: replace the status with cv type
    
        createdMapset = mapsetDao.createMapset(createdMapset);
        MapsetDTO createdMapsetDTO = new MapsetDTO();
        ModelMapper.mapEntityToDto(createdMapset, createdMapsetDTO);
        return createdMapsetDTO;
    }
    
    
}