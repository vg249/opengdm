package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetTypeDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.entity.Reference;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
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
        // check if mapset has mapsetTypeId and if it's a valid mapsetTypeId
        Cv type = this.getType(mapset.getMapsetTypeId());

        // check the referenceId
        Reference reference = null;
        if (mapset.getReferenceId() != null) {
            reference = this.getReference(mapset.getReferenceId());
        }

        Mapset createdMapset = new Mapset();
        createdMapset.setMapsetName(mapset.getMapsetName());
        if (mapset.getMapsetDescription() != null)
            createdMapset.setMapSetDescription(mapset.getMapsetDescription());
        createdMapset.setType(type);
        createdMapset.setReference(reference);
        // audit items
        Contact creator = contactDao.getContactByUsername(createdBy);
        if (creator != null)
            createdMapset.setCreatedBy(creator.getContactId());
        createdMapset.setCreatedDate(new java.util.Date());

        // other required colums
        createdMapset
                .setMapSetCode(String.format("%s_%s", type.getTerm(), createdMapset.getMapsetName()).replace(' ', '_'));

        // status
        Cv status = cvDao.getNewStatus();
        createdMapset.setStatus(status.getCvId()); // TODO: replace the status with cv type

        createdMapset = mapsetDao.createMapset(createdMapset);
        MapsetDTO createdMapsetDTO = new MapsetDTO();
        ModelMapper.mapEntityToDto(createdMapset, createdMapsetDTO);
        return createdMapsetDTO;
    }

    @Transactional
    @Override
    public MapsetDTO getMapset(Integer mapsetId) {
        Mapset mapset = mapsetDao.getMapset(mapsetId);
        if (mapset == null) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Not found");
        }
        MapsetDTO mapsetDTO = new MapsetDTO();
        ModelMapper.mapEntityToDto(mapset, mapsetDTO);
        return mapsetDTO;
    }

    @Transactional
    @Override
    public MapsetDTO updateMapset(Integer mapsetId, MapsetDTO patchData, String editedBy) throws Exception {
        Mapset mapset = mapsetDao.getMapset(mapsetId);
        if (mapset == null) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Not found");
        }

        if (patchData.getMapsetName() != null) {
            mapset.setMapsetName(patchData.getMapsetName());
            // TODO: also update code?
        }

        if (patchData.getMapsetDescription() != null) {
            mapset.setMapSetDescription(patchData.getMapsetDescription()); // TODO: refactor MapSetDescription
        }

        if (patchData.getMapsetTypeId() != null) {
            Cv type = this.getType(patchData.getMapsetTypeId());
            mapset.setType(type);
        }

        if (patchData.getReferenceId() != null) {
            Reference reference = this.getReference(patchData.getReferenceId());
            mapset.setReference(reference);
        }

        // audit items
        // audit items
        Contact creator = contactDao.getContactByUsername(editedBy);
        if (creator != null)
            mapset.setModifiedBy(creator.getContactId());
        mapset.setModifiedDate(new java.util.Date());

        // updated status
        // status
        Cv status = cvDao.getModifiedStatus();
        mapset.setStatus(status.getCvId()); // TODO: replace the status with cv type

        mapset = mapsetDao.updateMapset(mapset);
        MapsetDTO updatedMapsetDTO = new MapsetDTO();
        ModelMapper.mapEntityToDto(mapset, updatedMapsetDTO);
        return updatedMapsetDTO;

    }

    private Reference getReference(Integer referenceId) throws Exception {
        Reference reference = referenceDao.getReference(referenceId);
        if (reference == null) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Unknown reference");
        }
        return reference;

    }

    private Cv getType(Integer cvId) throws Exception {
        Cv type = cvDao.getCvByCvId(cvId);
        if (type == null) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Mapset type not found");
        }

        // check type is correct type
        if (!type.getCvGroup().getCvGroupName().equals(CvGroup.CVGROUP_MAPSET_TYPE.getCvGroupName())) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST, "Invalid type");
        }
        return type;
    }

    @Transactional
    @Override
    public void deleteMapset(Integer mapsetId) throws Exception {
        Mapset mapset = mapsetDao.getMapset(mapsetId);
        if (mapset == null) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Not found");
        }

        mapsetDao.deleteMapset(mapset);
    }

    @Transactional
    @Override
    public MapsetTypeDTO createMapsetType(String mapsetTypeName, String mapsetTypeDescription, String user) {
        
        org.gobiiproject.gobiimodel.entity.CvGroup cvGroup = cvDao.getCvGroupByNameAndType(
            CvGroup.CVGROUP_MAPSET_TYPE.getCvGroupName(),
            2 //TODO:  this is custom type
        );
        if (cvGroup == null) throw new GobiiDaoException("Missing CvGroup for Analysis Type");

        Cv cv = new Cv();
        cv.setCvGroup(cvGroup);
		cv.setTerm(mapsetTypeName);
		
		if (mapsetTypeDescription != null)
        	cv.setDefinition(mapsetTypeDescription);

        //get the new row status
        // Get the Cv for status, new row
        Cv status = cvDao.getNewStatus();
        cv.setStatus(status.getCvId());

        //set rank
        cv.setRank(0);
        cv = cvDao.createCv(cv);
        
        MapsetTypeDTO mapsetDTO = new MapsetTypeDTO();
        ModelMapper.mapEntityToDto(cv, mapsetDTO);
        return mapsetDTO;
    }

    @Transactional
    @Override
    public PagedResult<MapsetTypeDTO> getMapsetTypes(Integer page, Integer pageSize) {
        List<Cv> cvs = cvDao.getCvs(null, CvGroup.CVGROUP_MAPSET_TYPE.getCvGroupName(), null, page, pageSize);
        List<MapsetTypeDTO> mapsetTypeDTOs = new ArrayList<>();

        cvs.forEach(cv -> {
            MapsetTypeDTO mapsetTypeDTO = new MapsetTypeDTO();
            ModelMapper.mapEntityToDto(cv, mapsetTypeDTO);
            mapsetTypeDTOs.add(mapsetTypeDTO);
        });
        PagedResult<MapsetTypeDTO> result = new PagedResult<>();
        result.setCurrentPageNum(page);
        result.setCurrentPageSize(mapsetTypeDTOs.size());
        result.setResult(mapsetTypeDTOs);

        return result;
    }
    
    
    
}