/**
 * ProjectController.java
 * Gobii API endpoint controllers for projects
 * 
 * 
 * @author Rodolfo N. Duldulao, Jr. <rnduldulaojr@gmail.com>
 * @version 1.0
 * @since 2020-03-06
 */
package org.gobiiproject.gobiiweb.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseDataList;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiimodel.dto.auditable.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Scope(value = "request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_GOBII_V3)
@Api()
@CrossOrigin
public class ProjectsController {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProjectsController.class);

    @Autowired
    private ProjectService<ProjectDTO> projectService = null;

    @GetMapping("/projects")
    public BrapiResponseEnvelopeMaster<BrapiResponseDataList<ProjectDTO>> getProjectsList(
           @RequestParam Optional<Integer> pageNum,
           @RequestParam Optional<Integer> pageSize
    ) {
        LOGGER.debug("ProjectService " + projectService);
        BrapiResponseEnvelopeMaster<BrapiResponseDataList<ProjectDTO>> resp = new BrapiResponseEnvelopeMaster<BrapiResponseDataList<ProjectDTO>>();
        BrapiResponseDataList<ProjectDTO> list = new BrapiResponseDataList<>();

        List<ProjectDTO> projectsList = projectService.getProjects(
            pageNum.orElse(1), pageSize.orElse(1000)
        );
        list.setData(projectsList);
        
        resp.setResult(list);
        return resp;
        
    }

    public ProjectService<ProjectDTO> getProjectService() {
        return projectService;
    }

    public void setProjectService(ProjectService<ProjectDTO> projectService) {
        this.projectService = projectService;
    }

}

