package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidtomapping.DtoMapProject;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.impl.DtoMapProjectImpl;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

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

            returnVal  = dtoMapProject.getProject(projectDTO);

        } catch (GobiiDtoMappingException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return  returnVal;

    } // getProject

} // ProjectServiceImpl
