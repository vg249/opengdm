package org.gobiiproject.gobidomain.services.impl.sampletracking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiidtomapping.entity.auditable.sampletracking.DtoMapProject;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectServiceImpl implements ProjectService<ProjectDTO> {

    Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private DtoMapProject dtoMapSampleTrackingProject = null;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CvDao cvDao;

    @Override
    public ProjectDTO createProject(ProjectDTO newProjectDto) throws GobiiDomainException {

        try {

            Project newProject = new Project();

            //Setting created by contactId
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();

            Integer contactId = this.contactService.getContactByUserName(userName).getContactId();

            newProjectDto.setCreatedBy(contactId);

            //Setting created date
            newProjectDto.setCreatedDate(new Date(new Date().getTime()));

            // Project status is set as 1 which corresponds to new project
            // Can be moved to stored procedure.
            // TODO: Should be moved inside Stored Procedure.
            newProjectDto.setProjectStatus(1);

            ModelMapper.mapDtoToEntity(newProjectDto, newProject);

            List<Cv> cvList = cvDao.getCvListByCvGroup(CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName());

            newProject.setProperties(this.getCvIdMappedProperties(cvList, newProjectDto.getProperties()));

            Integer createdProjectId = projectDao.createProject(newProject);

            if(createdProjectId > 0) {
                newProjectDto.setProjectId(createdProjectId);
            }
            else {
                throw new GobiiException("Failed creating project. System Error.");
            }
        }
        catch(GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE.getMessage());
            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return newProjectDto;
    }

    private JsonNode getCvIdMappedProperties(List<Cv> cvList, Map<String, String> projectProperties) {

        JsonNode returnVal;

        ObjectMapper mapper = new ObjectMapper();

        try {

            Map<String, String> cvIdMappedProperties = new HashMap<>();

            for (Cv cv : cvList) {
                if (projectProperties.containsKey(cv.getTerm())) {

                    cvIdMappedProperties.put(cv.getCvId().toString(),
                            projectProperties.get(cv.getTerm()));
                }
            }

            returnVal = mapper.reader().readTree(
                    mapper.writeValueAsString(cvIdMappedProperties));

        }
        catch(Exception e) {

            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }

        return returnVal;
    }

    @Override
    public ProjectDTO replaceProject(Integer projectId, ProjectDTO newProject)
    throws GobiiDomainException{
        return newProject;
    }

    @Override
    public List<ProjectDTO> getProjects() throws GobiiDomainException {

        List<ProjectDTO> returnVal = null;

        return returnVal;
    }

    public List<ProjectDTO> getProjects(Integer pageToken, Integer pageSize) {
        List<ProjectDTO> returnVal;
        try {

            returnVal = dtoMapSampleTrackingProject.getList(pageToken, pageSize);

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE.getMessage());

            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );

        }
        catch(Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
        return returnVal;
    }

    @Override
    public ProjectDTO getProjectById(Integer projectId)
    throws GobiiDomainException {

        ProjectDTO returnVal;
        try {

            Project project = projectDao.getProjectById(projectId);

            returnVal = dtoMapSampleTrackingProject.get(projectId);

            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Project not found for given id.");
            }
            return returnVal;
        }
        catch (GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE.getMessage());
            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }



    @Override
    public List<ProjectDTO> getProjectsForLoadedDatasets() {
        List<ProjectDTO> returnVal = null;
        return returnVal;
    }

}
