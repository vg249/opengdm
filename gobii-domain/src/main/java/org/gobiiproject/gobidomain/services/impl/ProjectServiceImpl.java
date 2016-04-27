package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiidtomapping.DtoMapProject;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;

import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


/**
 * Created by Phil on 4/6/2016.
 */
public class ProjectServiceImpl implements ProjectService {


    Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private DtoMapProject dtoMapProject = null;

    @Override
    public ProjectDTO getProject(ProjectDTO projectDTO) {

        ProjectDTO returnVal = null;
        try {

            switch (projectDTO.getProcessType()) {

                case READ:
                    returnVal = dtoMapProject.getProject(projectDTO);
                    break;

                case CREATE:
                    projectDTO.setCreatedDate(new Date());
                    projectDTO.setModifiedDate(new Date());
                    returnVal = dtoMapProject.createProject(projectDTO);
                    break;

                default:
                    GobiiDomainException gobiiDomainException = new GobiiDomainException("Unsupported process type: " + projectDTO.getProcessType().toString());
                    returnVal.getDtoHeaderResponse().addException(gobiiDomainException);
                    LOGGER.error(gobiiDomainException.getMessage());
                    break;

            } // switch


        } catch (GobiiDtoMappingException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;

    } // getProject


} // ProjectServiceImpl
