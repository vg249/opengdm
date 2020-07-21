/**
 * ProjectServiceImpl.java
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
import org.gobiiproject.gobidomain.services.gdmv3.exceptions.EntityDoesNotExistException;
import org.gobiiproject.gobidomain.services.gdmv3.exceptions.UnknownEntityException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Organization;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.modelmapper.CvMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.OrganizationDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OrganizationDao organizationDao;

    @Autowired
    private PropertiesService propertiesService;

    @Autowired
    private KeycloakService keycloakService;

    @Transactional
    @Override
    public PagedResult<ProjectDTO> getProjects(Integer page, Integer pageSize, Integer piContactId) throws Exception {
        log.debug("Getting projects list offset %d size %d", page, pageSize);
        // get Cvs
        try {
            Objects.requireNonNull(pageSize);
            Objects.requireNonNull(page);
            List<ProjectDTO> projectDTOs = new java.util.ArrayList<>();
            List<Cv> cvs = cvDao.getCvListByCvGroup(CvGroupTerm.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
            List<Project> projects = projectDao.getProjects(page, pageSize, piContactId);
            projects.forEach(project -> {
                projectDTOs.add( createProjectDTO(project, cvs) );
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
    public ProjectDTO createProject(ProjectDTO request, String createdBy) throws Exception {
        // check if contact exists
        Contact contact = this.loadContact(request.getPiContactId());

        // Get the Cv for status, new row
        Cv cv = cvDao.getNewStatus();

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
        contactDao.stampCreated(project, createdBy);
        projectDao.createProject(project);

        ProjectDTO projectDTO = new ProjectDTO();
        ModelMapper.mapEntityToDto(project, projectDTO);

        //transform Cv
        List<Cv> cvs = cvDao.getCvListByCvGroup(CvGroupTerm.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
        List<CvPropertyDTO> propDTOs = CvMapper.listCvIdToCvTerms(cvs, project.getProperties());
        projectDTO.setProperties(propDTOs);
        return projectDTO;

    }

    @Override
    public String getDefaultProjectEditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                       .map(auth -> auth.getName())
                       .orElse(null);
    }

    @Transactional
    @Override
    public ProjectDTO patchProject(Integer projectId, ProjectDTO request, String editedBy)
            throws Exception {
        Project project = this.loadProject(projectId);

        //convert
        if (request.getPiContactId() != null) {   
            this.updateContact(project, request.getPiContactId());
        }
        if (request.getProjectName() != null) {
            this.updateProjectName(project, request.getProjectName());
        }
        if (request.getProjectDescription() != null) {
            this.updateProjectDescription(project, request.getProjectDescription());
        }
       
        contactDao.stampModified(project, editedBy);
        
        List<CvPropertyDTO> propList = request.getProperties();
        //update props if request props is not empty list
        if (Optional.ofNullable(propList).map(v -> v.size()).orElse(0) > 0 ) {
            this.updateProperties(project, propList);
        }

        //set new status
        Cv cv = cvDao.getModifiedStatus();
        project.setStatus(cv);

        project = projectDao.patchProject(project);
        return createProjectDTO(project, null);
    }

    @Transactional
    @Override
    public PagedResult<CvPropertyDTO> getProjectProperties(Integer page, Integer pageSize) throws Exception {
        return propertiesService.getProperties(page, pageSize, CvGroupTerm.CVGROUP_PROJECT_PROP);
    }

    @Transactional
    @Override
    public ProjectDTO getProject(Integer projectId) throws Exception {
        Project project = this.loadProject(projectId);
        ProjectDTO projectDTO = this.createProjectDTO(project, null);
        return projectDTO;
    }

    private ProjectDTO createProjectDTO(Project project, List<Cv> cvs)  {
        ProjectDTO projectDTO = new ProjectDTO();
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

    private void updateProjectDescription(Project project, String value) {
        project.setProjectDescription(value);
    }

    private void updateProjectName(Project project, String value) throws Exception {
        project.setProjectName(value);
    }

    private void updateContact(Project project, String value) throws NumberFormatException, Exception {
        Contact contact = this.loadContact(value);
        project.setContact(contact);
    }

    @Transactional
    @Override
    public void deleteProject(Integer projectId) throws Exception {
        Project project = this.loadProject(projectId);
        projectDao.deleteProject(project);
    }

    private Project loadProject(Integer projectId) throws Exception {
        return Optional.ofNullable(projectDao.getProject(projectId))
                       .orElseThrow(() -> new EntityDoesNotExistException.Project());
    }

    private Contact loadContact(String contactId) throws Exception {
        //if contactId is numeric
        if (contactId.matches("\\d+")) {
            return Optional.ofNullable(contactDao.getContact(Integer.parseInt(contactId)))
                        .orElseThrow(() -> new UnknownEntityException.Contact());
        }

        //else this should be a uuid so load or create/return
        ContactDTO keycloakUser = keycloakService.getUser(contactId);
        Contact contact = contactDao.getContactByUsername(keycloakUser.getUsername());
        

        return Optional
                .ofNullable(contact)
                .orElse(
                    this.createNewContact(keycloakUser)
                );
        
    }

    private Contact createNewContact(ContactDTO user) throws Exception {
        Organization organization = Optional.ofNullable(organizationDao.getOrganizationByName(user.getOrganizationName()))
                                    .orElse(this.createOrganization(user.getOrganizationName()));

        Contact contact = new Contact();
        contact.setUsername(user.getUsername());
        contact.setEmail(user.getEmail());
        contact.setFirstName(user.getPiContactFirstName());
        contact.setLastName(user.getPiContactLastName());
        contact.setOrganization(organization);
        contactDao.stampCreated(contact, null);
        contactDao.addContact(contact);
        return contact;
        
    }

    private Organization createOrganization(String name) throws Exception {
        Organization organization = new Organization();
        organization.setName(name);
        contactDao.stampCreated(organization, null);
        return organization;
    }

    
}