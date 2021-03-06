package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapProject;

import org.gobiiproject.gobiimodel.dto.entity.auditable.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Phil on 4/6/2016.
 */
public class ProjectServiceImpl implements ProjectService {


    Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private DtoMapProject dtoMapProject = null;


    @Override
    public List<ProjectDTO> getProjects() throws GobiiDomainException {

        List<ProjectDTO> returnVal;

        try {
            returnVal = dtoMapProject.getList();

            for (ProjectDTO currentProjectDTO : returnVal) {
                currentProjectDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
                currentProjectDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
            }


            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public ProjectDTO getProjectById(Integer projectId) throws GobiiDomainException {

        ProjectDTO returnVal;

        try {
            returnVal = dtoMapProject.get(projectId);

            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified projectId ("
                                + projectId
                                + ") does not match an existing project ");
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) throws GobiiDomainException {

        ProjectDTO returnVal;

        returnVal = dtoMapProject.create(projectDTO);

        // When we have roles and permissions, this will be set programmatically
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        return returnVal;
    }

    @Override
    public ProjectDTO replaceProject(Integer projectId, ProjectDTO projectDTO) throws GobiiDomainException {
        ProjectDTO returnVal;

        try {

            if (null == projectDTO.getProjectId() ||
                    projectDTO.getProjectId().equals(projectId)) {


                ProjectDTO existingProjectDTO = dtoMapProject.get(projectId);

                if (null != existingProjectDTO.getProjectId() && existingProjectDTO.getProjectId().equals(projectId)) {


                    returnVal = dtoMapProject.replace(projectId, projectDTO);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified projectId ("
                                    + projectId
                                    + ") does not match an existing project ");
                }

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The projectId specified in the dto ("
                                + projectDTO.getProjectId()
                                + ") does not match the projectId passed as a parameter "
                                + "("
                                + projectId
                                + ")");

            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }


        return returnVal;
    }

    @Override
    public List<ProjectDTO> getProjectsForLoadedDatasets() throws GobiiDomainException {

        List<ProjectDTO> returnVal;
        try {

            returnVal = dtoMapProject.getProjectsForLoadedDatasets();

            for (ProjectDTO currentProjectDTO : returnVal) {
                currentProjectDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
                currentProjectDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
            }

            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;

    }

} // ProjectServiceImpl
