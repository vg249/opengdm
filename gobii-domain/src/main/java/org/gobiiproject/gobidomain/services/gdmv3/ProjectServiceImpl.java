/**
 * GobiiProjectServiceImpl.java
 * 
 * Project Service for V3 API.
 * 
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-07
 */
package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.PropertiesService;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectPatchDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.modelmapper.CvMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private PropertiesService propertiesService;

    @Transactional
    @Override
    public PagedResult<GobiiProjectDTO> getProjects(Integer page, Integer pageSize, Integer piContactId) throws GobiiDtoMappingException {
        log.debug("Getting projects list offset %d size %d", page, pageSize);
        // get Cvs
        List<Cv> cvs = cvDao.getCvListByCvGroup(CvGroupTerm.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
        try {
            Objects.requireNonNull(pageSize);
            Objects.requireNonNull(page);
            List<GobiiProjectDTO> projectDTOs = new java.util.ArrayList<>();

            List<Project> projects = projectDao.getProjects(page, pageSize, piContactId);
            projects.forEach(project -> {
                GobiiProjectDTO projectDTO = new GobiiProjectDTO();
                ModelMapper.mapEntityToDto(project, projectDTO);

                List<CvPropertyDTO> propDTOs = CvMapper.listCvIdToCvTerms(cvs,
                        project.getProperties());

                projectDTO.setProperties(propDTOs);

                projectDTOs.add(projectDTO);
            });

            return PagedResult.createFrom(page, projectDTOs);
        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            log.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

    @Transactional
    @Override
    public GobiiProjectDTO createProject(GobiiProjectRequestDTO request, String createdBy) throws Exception {
        // check if contact exists
        Contact contact = contactDao.getContact(Integer.parseInt(request.getPiContactId()));
        if (contact == null)
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST, "Contact not found.");

        // Get the Cv for status, new row
        Cv cv = cvDao.getNewStatus();
        log.debug("Cv " + cv.getTerm());

        Project project = new Project();
        project.setContact(contact);
        project.setProjectName(request.getProjectName());
        project.setProjectDescription(request.getProjectDescription());
        project.setStatus(cv);

        //Project properties
        List<CvPropertyDTO> nullFiltered = new ArrayList<>();
        request.getProperties().forEach(cvItem -> {
            if (cvItem.getPropertyValue() != null) {
                nullFiltered.add(cvItem);
            }
        });
        java.util.Map<String, String> props = CvMapper.mapCvIdToCvTerms(nullFiltered);
        project.setProperties(props);
        // audit items
        Contact creator = contactDao.getContactByUsername(createdBy);
        if (creator != null)
            project.setCreatedBy(creator.getContactId());
        project.setCreatedDate(new java.util.Date());
        projectDao.createProject(project);

        GobiiProjectDTO projectDTO = new GobiiProjectDTO();
        ModelMapper.mapEntityToDto(project, projectDTO);

        //transform Cv
        List<Cv> cvs = cvDao.getCvListByCvGroup(CvGroupTerm.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
        List<CvPropertyDTO> propDTOs = CvMapper.listCvIdToCvTerms(cvs, project.getProperties());
        projectDTO.setProperties(propDTOs);
        return projectDTO;

    }

    @Override
    public String getDefaultProjectEditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)
            return auth.getName();
        return null;
    }

    @Transactional
    @Override
    public GobiiProjectDTO patchProject(Integer projectId, GobiiProjectPatchDTO request, String editedBy)
            throws Exception {
        Project project = projectDao.getProject(projectId);
        if (project == null) {
            throw new NullPointerException("Project not found.");
        }

        String projContactId = project.getPiContactId().toString();
        
        //convert
        if (request.keyInPayload("piContactId") && !projContactId.equals(request.getPiContactId())) {   
            this.updateAttributes(project, "piContactId", request.getPiContactId());
        }
        if (request.keyInPayload("projectName")) {
            this.updateAttributes(project, "projectName", request.getProjectName());
        }
        if (request.keyInPayload("projectDescription")) {
            this.updateAttributes(project, "projectDescription", request.getProjectDescription());
        }
       
        
        //audit items first
        Contact editor = contactDao.getContactByUsername(editedBy);
        if (editor != null)
            project.setModifiedBy(editor.getContactId());
        project.setModifiedDate(new java.util.Date());
        
        Optional<List<CvPropertyDTO>> propList = Optional.ofNullable(request.getProperties());
        if (propList.isPresent()) {
            this.updateProperties(project, propList.get());
        }

        //set new status
        Cv cv = cvDao.getModifiedStatus();
        project.setStatus(cv);

        projectDao.patchProject(project);
        return getProject(project.getProjectId());
    }

    @Transactional
    @Override
    public PagedResult<CvPropertyDTO> getProjectProperties(Integer page, Integer pageSize) throws Exception {
        return propertiesService.getProperties(page, pageSize, CvGroupTerm.CVGROUP_PROJECT_PROP);
    }

    @Transactional
    @Override
    public GobiiProjectDTO getProject(Integer projectId) throws Exception {
        Project project = projectDao.getProject(projectId);
        if (project == null) return null;

        GobiiProjectDTO projectDTO = this.createProjectDTO(project, null);
        return projectDTO;
    }

    private GobiiProjectDTO createProjectDTO(Project project, List<Cv> cvs)  {
        GobiiProjectDTO projectDTO = new GobiiProjectDTO();
        ModelMapper.mapEntityToDto(project, projectDTO);
        if (cvs == null) {
            cvs = cvDao.getCvListByCvGroup(CvGroupTerm.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
        }

        List<CvPropertyDTO> propDTOs = CvMapper.listCvIdToCvTerms(cvs, project.getProperties());
        projectDTO.setProperties(propDTOs);
        return projectDTO;
    }


    private void updateProperties(Project project, List<CvPropertyDTO> propertiesList) {
        java.util.Map<String, String> currentProperties = project.getProperties();
        java.util.Map<String, String> incomingProperties = CvMapper.mapCvIdToCvTerms(propertiesList);

        currentProperties.putAll(incomingProperties);
        currentProperties.values().removeAll(Collections.singleton(null)); //remove nulled values
        
        project.setProperties(currentProperties);
    }

    private void updateAttributes(Project project, String key, String value)
            throws NumberFormatException, Exception {
        switch(key) {
            case "piContactId":
                this.updateContact(project, value);
                break;
            case "projectName":
                this.updateProjectName(project, value);
                break;
            case "projectDescription":
                this.updateProjectDescription(project, value);
                break;
        }
    }

    private void updateProjectDescription(Project project, String value) {
        project.setProjectDescription(value);
    }

    private void updateProjectName(Project project, String value) throws Exception {
        if (value == null || value.trim() == "") throw new Exception("projectName is required");
        project.setProjectName(value);
    }

    private void updateContact(Project project, String value) throws NumberFormatException, Exception {
        if (project.getContact() != null && project.getPiContactId().toString().equals(value)) return;
        Contact contact = contactDao.getContact(Integer.parseInt(value));
        
        if (contact != null) {
            project.setContact(contact);
        } else {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST, "Contact not found.");
        }
    }

    @Transactional
    @Override
    public void deleteProject(Integer projectId) throws Exception {
        Project project = projectDao.getProject(projectId);
        //TODO: replace the NullPointerException with a Gobii specific error.
        if (project == null) throw new NullPointerException("Project does not exist");

        projectDao.deleteProject(project);
    }
}