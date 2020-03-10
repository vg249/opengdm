/**
 * V3ProjectServiceImpl.java
 * 
 * Project Service for V3 API.
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-07
 */
package org.gobiiproject.gobidomain.services.impl;

import java.util.List;
import java.util.Objects;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.V3ProjectService;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.auditable.V3ProjectDTO;
import org.gobiiproject.gobiimodel.entity.V3Project;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.V3ProjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class V3ProjectServiceImpl implements V3ProjectService {
    Logger LOGGER = LoggerFactory.getLogger(V3ProjectServiceImpl.class);
    
    // @Autowired
    // private DtoMapV3Project dtoMapV3Project;
    @Autowired
    private V3ProjectDao v3ProjectDao;


    @Override
    public BrApiMasterListPayload<V3ProjectDTO> getProjects(Integer pageNum, Integer pageSize) throws GobiiDtoMappingException {
        // TODO Auto-generated method stub
        LOGGER.debug("Getting projects list offset %d size %d", pageNum, pageSize);
        BrApiMasterListPayload<V3ProjectDTO> pagedResult;
        try {
            Objects.requireNonNull(pageSize);
            Objects.requireNonNull(pageNum);
            List<V3ProjectDTO> projectDTOs = new java.util.ArrayList<>();

            List<V3Project> projects = v3ProjectDao.getProjects(pageNum, pageSize);
            projects.forEach(project -> {
                V3ProjectDTO dto = new V3ProjectDTO();
                ModelMapper.mapEntityToDto(project, dto);
                //TODO: use BeanUtils?  -- transferred from refactored dtoMap class (deleted)
                // dto.setId(project.getProjectId());
                // dto.setProjectId(project.getProjectId());
                // dto.setProjectName(project.getProjectName());
                // dto.setProjectDescription(project.getProjectDescription());
                // dto.setPiContactId(project.getPiContactId());
                // dto.setPiContactName(project.getPiContactName());
                // dto.setCreatedBy(project.getCreatedBy());
                // dto.setCreatedDate(project.getCreatedDate());
                // dto.setDatasetCount(project.getDatasetCount());
                // dto.setExperimentCount(project.getExperimentCount());
                // dto.setDnaRunsCount(project.getDnaRunsCount());
                // dto.setMarkersCount(project.getMarkersCount());
                // dto.setModifiedBy(project.getModifiedBy());
                // dto.setModifiedDate(project.getModifiedDate());
                // dto.setProjectDescription(project.getProjectDescription());
                // JsonNode props = project.getProperties();
                //TODO: convert JsonNode to PropertyDTO
                //dto.setProperties(project.getProperties());
                
                projectDTOs.add(dto);
            });
            
            pagedResult = new BrApiMasterListPayload<>(projectDTOs, projectDTOs.size(), pageNum);
            return pagedResult;
        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }


       
    }

}