package org.gobiiproject.gobidomain.services.impl.sampletracking;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.sampletracking.DtoMapProject;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.rest.RestResourceProfile;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class ProjectServiceImpl implements ProjectService<ProjectDTO> {

    Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private DtoMapProject dtoMapSampleTrackingProject = null;

    @Autowired
    private ContactService contactService;

    @Override
    public ProjectDTO createProject(ProjectDTO newProject) throws GobiiDomainException {

        ProjectDTO returnVal;
        try {

            //Setting created by contactId
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();

            Integer contactId = this.contactService.getContactByUserName(userName).getContactId();

            newProject.setCreatedBy(contactId);

            //Setting created date
            newProject.setCreatedDate(new Date(new Date().getTime()));

            // Project status is set as 1 which corresponds to new project
            // Can be moved to stored procedure.
            // TODO: Should be moved inside Stored Procedure.
            newProject.setProjectStatus(1);

            returnVal = dtoMapSampleTrackingProject.create(newProject);

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
