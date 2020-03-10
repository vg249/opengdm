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
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.auditable.V3ProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.v3.Project;
import org.gobiiproject.gobiimodel.modelmapper.CvIdCvTermMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class V3ProjectServiceImpl implements V3ProjectService {
    Logger LOGGER = LoggerFactory.getLogger(V3ProjectServiceImpl.class);
    
    // @Autowired
    // private DtoMapV3Project dtoMapV3Project;
    @Autowired
    private ProjectDao v3ProjectDao;

    @Autowired
    private CvDao cvDao;


    @Override
    public BrApiMasterListPayload<V3ProjectDTO> getProjects(Integer pageNum, Integer pageSize) throws GobiiDtoMappingException {

        LOGGER.debug("Getting projects list offset %d size %d", pageNum, pageSize);
        BrApiMasterListPayload<V3ProjectDTO> pagedResult;

        //get Cvs
        List<Cv> cvs = cvDao.getCvListByCvGroup(CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
        try {
            Objects.requireNonNull(pageSize);
            Objects.requireNonNull(pageNum);
            List<V3ProjectDTO> projectDTOs = new java.util.ArrayList<>();

            List<Project> projects = v3ProjectDao.getProjects(pageNum, pageSize);
            projects.forEach(project -> {
                V3ProjectDTO dto = new V3ProjectDTO();
                ModelMapper.mapEntityToDto(project, dto);
                
                List<CvPropertyDTO> propDTOs = CvIdCvTermMapper.listCvIdToCvTerms(cvs, project.getProperties());
               
                dto.setProperties(propDTOs);
                
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