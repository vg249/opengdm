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
import org.gobiiproject.gobidomain.services.GobiiProjectService;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
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

        //get Cvs
        List<Cv> cvs = cvDao.getCvListByCvGroup(CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
        try {
            Objects.requireNonNull(pageSize);
            Objects.requireNonNull(pageNum);
            List<GobiiProjectDTO> projectDTOs = new java.util.ArrayList<>();

            List<Project> projects = projectDao.getProjects(pageNum, pageSize);
            projects.forEach(project -> {
                GobiiProjectDTO dto = new GobiiProjectDTO();
                ModelMapper.mapEntityToDto(project, dto);
                
                List<CvPropertyDTO> propDTOs = CvIdCvTermMapper.listCvIdToCvTerms(cvs, project.getProperties());
               
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

}