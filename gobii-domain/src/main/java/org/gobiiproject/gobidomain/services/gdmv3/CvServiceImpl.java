package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.CvGroup;
import org.gobiiproject.gobiimodel.modelmapper.CvMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.GobiiDaoException;
import org.springframework.beans.factory.annotation.Autowired;

public class CvServiceImpl implements CvService {

    @Autowired
    private CvDao cvDao;

    @Transactional
    @Override
    public CvDTO createCv(CvDTO request) throws Exception {
        Cv cv = new Cv();
        // check the properties first if the propertyId exists and it is a property
        if (request.getProperties() != null && request.getProperties().size() > 0) {
            Map<String, String> propsMap = new HashMap<>();
            for (int i = 0; i < request.getProperties().size(); i++) {
                CvPropertyDTO cvPropDTO = request.getProperties().get(i);
                Cv propCv = cvDao.getCvByCvId(cvPropDTO.getPropertyId());
                if (propCv == null) {
                    throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid property Id");
                }
                if (!propCv.getCvGroup().getCvGroupName()
                        .equals(org.gobiiproject.gobiimodel.cvnames.CvGroupTerm.CVGROUP_CV_PROP.getCvGroupName())) {
                    throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid cv property");

                }
                propsMap.put(cvPropDTO.getPropertyId().toString(), cvPropDTO.getPropertyValue());

            }
            cv.setProperties(propsMap);
        }

        // check cvGroup Id
        CvGroup cvGroup = cvDao.getCvGroupById(request.getCvGroupId());
        if (cvGroup == null || !cvGroup.getCvGroupType().equals(GobiiCvGroupType.GROUP_TYPE_USER.getGroupTypeId())) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid cv group");
        }
        // check correct cv group type?
        cv.setCvGroup(cvGroup);

        cv.setTerm(request.getCvName());
        cv.setDefinition(request.getCvDescription());
        cv.setRank(0);

        Cv newStatus = cvDao.getNewStatus();
        cv.setStatus(newStatus.getCvId());

        cvDao.createCv(cv);

        CvDTO resultDTO = new CvDTO();
        ModelMapper.mapEntityToDto(cv, resultDTO);

        // set status
        resultDTO.setCvStatus(newStatus.getTerm());
        resultDTO.setProperties(this.convertToListDTO(resultDTO.getPropertiesMap()));

        return resultDTO;
    }

    @Transactional
    @Override
    public CvDTO updateCv(Integer id, CvDTO request) throws Exception {
        //check
        boolean updated = false;
        Cv cv = this.loadCv(id);

        // check if change of name
        if (!LineUtils.isNullOrEmpty(request.getCvName())) {
            cv.setTerm(request.getCvName());
            updated = true;
        };

        // check if change description
        if (!LineUtils.isNullOrEmpty(request.getCvDescription())) {
            cv.setDefinition(request.getCvDescription());
            updated = true;
        }

        // check if change of group
        if (request.getCvGroupId() != null) {
            // check if new group id exists
            CvGroup cvGroup = cvDao.getCvGroupById(request.getCvGroupId());
            if (cvGroup == null || !cvGroup.getCvGroupType().equals(GobiiCvGroupType.GROUP_TYPE_USER.getGroupTypeId())) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid cv group");
            }
            cv.setCvGroup(cvGroup);
            updated = true;
        }

        // check if change of properties
        try {
        if (request.getProperties() != null && request.getProperties().size() > 0) {
            Map<String, String> properties;
            if (request.getPropertiesMap() != null) properties =  request.getPropertiesMap();
            else properties = new HashMap<>();
            for (int i = 0; i < request.getProperties().size(); i++) {
                CvPropertyDTO cvPropertyDTO = request.getProperties().get(i);
                // check if id does exist and correct group
                Cv propertyCv = cvDao.getCvByCvId(cvPropertyDTO.getPropertyId());
                if (propertyCv == null || !propertyCv.getCvGroup().getCvGroupName()
                        .equals(org.gobiiproject.gobiimodel.cvnames.CvGroupTerm.CVGROUP_CV_PROP.getCvGroupName())) {
                    throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid cv property");
                }
                // check which operation is being done
                boolean existingProperty = this.isExistingProperty(properties, cvPropertyDTO);
                if (existingProperty) {
                    // check if delete or update value
                    if (cvPropertyDTO.getPropertyValue() == null) {
                        properties.remove(cvPropertyDTO.getPropertyId().toString());
                    } else {
                        properties.put(cvPropertyDTO.getPropertyId().toString(), cvPropertyDTO.getPropertyValue());
                    }
                } else {
                    if (cvPropertyDTO.getPropertyValue() != null) {
                        properties.put(cvPropertyDTO.getPropertyId().toString(), cvPropertyDTO.getPropertyValue());
                    }
                }
            }
            cv.setProperties(properties);
            updated = true;
        }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (updated) {
            Cv modifiedCv = cvDao.getModifiedStatus();
            cv.setStatus(modifiedCv.getCvId());
        }
        
        Cv updatedCv = cvDao.updateCv(cv);
        CvDTO updatedCvDTO = new CvDTO();

        ModelMapper.mapEntityToDto(updatedCv, updatedCvDTO);
        updatedCvDTO.setProperties(this.convertToListDTO(updatedCvDTO.getPropertiesMap()));

        return updatedCvDTO;
    }

    private List<CvPropertyDTO> convertToListDTO(Map<String, String> propertiesMap) {
        List<Cv> cvs = cvDao
                .getCvListByCvGroup(org.gobiiproject.gobiimodel.cvnames.CvGroupTerm.CVGROUP_CV_PROP.getCvGroupName(), null);
        List<CvPropertyDTO> propDTOs = CvMapper.listCvIdToCvTerms(cvs, propertiesMap);
        return propDTOs;
    }

    private List<CvPropertyDTO> convertToListDTO(List<Cv> cvs, Map<String, String> propertiesMap) {
        List<CvPropertyDTO> propDTOs = CvMapper.listCvIdToCvTerms(cvs, propertiesMap);
        return propDTOs;
    }

    private boolean isExistingProperty(Map<String, String> properties, CvPropertyDTO cvPropertyDTO) {
        return properties.get(cvPropertyDTO.getPropertyId().toString()) != null;
    }

    @Transactional
    @Override
    public PagedResult<CvDTO> getCvs(Integer page, Integer pageSize, String cvGroupName, String cvGroupType) throws Exception {
        GobiiCvGroupType groupType = null;
        if (!LineUtils.isNullOrEmpty(cvGroupType)) {
            if (cvGroupType.toLowerCase().equals("system_defined") || cvGroupType.equals("1")) {
                groupType = GobiiCvGroupType.GROUP_TYPE_SYSTEM;
            } else if (cvGroupType.toLowerCase().equals("user_defined") || cvGroupType.equals("2")) {
                groupType = GobiiCvGroupType.GROUP_TYPE_USER;
            } else {
                groupType = GobiiCvGroupType.GROUP_TYPE_UNKNOWN;
            }
        }

        List<Cv> cvs = cvDao.getCvs(null, cvGroupName, groupType, page, pageSize);
        List<CvDTO> cvDTOs = new ArrayList<>();
        
        Cv newStatus = cvDao.getNewStatus();
        Cv modStatus = cvDao.getModifiedStatus();
        List<Cv> cvProps = cvDao.getCvListByCvGroup(
            org.gobiiproject.gobiimodel.cvnames.CvGroupTerm.CVGROUP_CV_PROP.getCvGroupName(),
            null
        );
        cvs.forEach(cv -> {
            CvDTO cvDTO = new CvDTO();
            ModelMapper.mapEntityToDto(cv, cvDTO);
            cvDTO.setProperties(this.convertToListDTO(cvProps, cvDTO.getPropertiesMap()));
            
            if(cvDTO.getStatus() == newStatus.getCvId()) {
                cvDTO.setCvStatus(newStatus.getTerm());
            } else {
                cvDTO.setCvStatus(modStatus.getTerm());
            }

            cvDTOs.add(cvDTO);

        });

        return PagedResult.createFrom(page, cvDTOs);
    }

    @Transactional
    @Override
    public CvDTO getCv(Integer id) throws Exception {
        Cv cv = this.loadCv(id);

        CvDTO cvDTO = new CvDTO();
        ModelMapper.mapEntityToDto(cv, cvDTO);

        //get status cv
        Cv statusCv = cvDao.getCvByCvId(cv.getStatus());

        cvDTO.setProperties(this.convertToListDTO( cvDTO.getPropertiesMap()));
        cvDTO.setCvStatus(statusCv.getTerm());

        return cvDTO;
    }

    @Transactional
    @Override
    public PagedResult<CvPropertyDTO> getCvProperties(Integer page, Integer pageSize) {
        List<Cv> cvProps = cvDao.getCvs(
            null,
            org.gobiiproject.gobiimodel.cvnames.CvGroupTerm.CVGROUP_CV_PROP.getCvGroupName(),
            null,
            page,
            pageSize
        );

        List<CvPropertyDTO> cvPropDTOs = new ArrayList<>();

        cvProps.forEach(cv -> {
            CvPropertyDTO cvPropertyDTO = new CvPropertyDTO();
            ModelMapper.mapEntityToDto(cv, cvPropertyDTO);
            cvPropDTOs.add(cvPropertyDTO);
        });

        return PagedResult.createFrom(page, cvPropDTOs);
    }

    @Transactional
    @Override
    public CvPropertyDTO addCvProperty(CvPropertyDTO request) {
        Cv cv = new Cv();
        cv.setTerm(request.getPropertyName());
        cv.setDefinition(request.getPropertyDescription());

        //set the group
        CvGroup cvGroup = cvDao.getCvGroupByNameAndType(
            org.gobiiproject.gobiimodel.cvnames.CvGroupTerm.CVGROUP_CV_PROP.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_USER.getGroupTypeId()
        );

        cv.setCvGroup(cvGroup);
        cv.setRank(0);
        cv.setStatus(cvDao.getNewStatus().getCvId());

        cvDao.createCv(cv);

        CvPropertyDTO cvPropertyDTO = new CvPropertyDTO();
        ModelMapper.mapEntityToDto(cv, cvPropertyDTO);

        return cvPropertyDTO;
    }

    @Transactional
    @Override
    public void deleteCv(Integer id) throws Exception {
        Cv cv = this.loadCv(id);

        //check group
        if (cv.getCvGroup().getCvGroupType() != GobiiCvGroupType.GROUP_TYPE_USER.getGroupTypeId()) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                            "Cannot delete system defined cv");
        }

        cvDao.deleteCv(cv);

    }

    private Cv loadCv(Integer id) throws Exception {
        Cv cv = cvDao.getCvByCvId(id);
        if (cv == null) {
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Cv not found");
        }
        return cv;
    }
    
}