/**
 * GobiiProjectServiceImpl.java
 * 
 * Project Service for V3 API.
 * 
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-07
 */
package org.gobiiproject.gobidomain.services.impl;

import java.util.List;
import java.util.Objects;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.GobiiProjectService;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectPatchDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.modelmapper.CvIdCvTermMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class GobiiProjectServiceImpl implements GobiiProjectService {
    Logger LOGGER = LoggerFactory.getLogger(GobiiProjectServiceImpl.class);

    // @Autowired
    // private DtoMapV3Project dtoMapV3Project;
    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CvDao cvDao;

    @Override
    public PagedResult<GobiiProjectDTO> getProjects(Integer pageNum, Integer pageSize) throws GobiiDtoMappingException {

        LOGGER.debug("Getting projects list offset %d size %d", pageNum, pageSize);
        PagedResult<GobiiProjectDTO> pagedResult;

        // get Cvs
        List<Cv> cvs = cvDao.getCvListByCvGroup(CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
        try {
            Objects.requireNonNull(pageSize);
            Objects.requireNonNull(pageNum);
            List<GobiiProjectDTO> projectDTOs = new java.util.ArrayList<>();

            List<Project> projects = projectDao.getProjects(pageNum, pageSize);
            projects.forEach(project -> {
                GobiiProjectDTO dto = new GobiiProjectDTO();
                ModelMapper.mapEntityToDto(project, dto);

                List<CvPropertyDTO> propDTOs = CvIdCvTermMapper.listCvIdToCvTerms(cvs,
                        project.getProperties().getProperties());

                dto.setProperties(propDTOs);

                projectDTOs.add(dto);
            });

            pagedResult = new PagedResult<>();
            pagedResult.setResult(projectDTOs);
            pagedResult.setCurrentPageNum(pageNum);
            pagedResult.setCurrentPageSize(projectDTOs.size());
            return pagedResult;
        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

    @Override
    public GobiiProjectDTO createProject(GobiiProjectRequestDTO request, String createdBy) throws Exception {
        // check if contact exists
        Project project = projectDao.createProject(request.getPiContactId(), request.getProjectName(),
                request.getProjectDescription(), request.getProperties(), createdBy);
        GobiiProjectDTO dto = new GobiiProjectDTO();
        ModelMapper.mapEntityToDto(project, dto);
        List<Cv> cvs = cvDao.getCvListByCvGroup(CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
        List<CvPropertyDTO> propDTOs = CvIdCvTermMapper.listCvIdToCvTerms(cvs, project.getProperties().getProperties());
        dto.setProperties(propDTOs);
        return dto;

    }

    @Override
    public String getDefaultProjectEditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)
            return auth.getName();
        return null;
    }

    @Override
    public GobiiProjectDTO patchProject(Integer projectId, GobiiProjectPatchDTO request, String editedBy)
            throws Exception {
        //convert
        java.util.Map<String, String> attributes = new java.util.HashMap<>();
        List<CvPropertyDTO> propList = request.getProperties();
        java.util.List<java.util.Map<String, String>> properties = new java.util.ArrayList<>();
        if (request.keyInPayload("piContactId")) {
            attributes.put("piContactId", request.getPiContactId());
        }
        if (request.keyInPayload("projectName")) {
            attributes.put("projectName", request.getProjectName());
        }
        if (request.keyInPayload("projectDescription")) {
            attributes.put("projectDescription", request.getProjectDescription());
        }

        if (propList != null) {
            for (CvPropertyDTO propDTO: propList) {
                if (propDTO.keyInPayload("propertyId") && propDTO.keyInPayload("propertyValue")) {
                    java.util.Map<String, String>  entry = new java.util.HashMap<>();
                    entry.put(propDTO.getPropertyId().toString(), propDTO.getPropertyValue());
                    properties.add(entry);
                }
            }   
        }     

        Project project = projectDao.patchProject(projectId, attributes, properties, editedBy);
        GobiiProjectDTO dto = new GobiiProjectDTO();
        ModelMapper.mapEntityToDto(project, dto);
        return dto;
    }

}