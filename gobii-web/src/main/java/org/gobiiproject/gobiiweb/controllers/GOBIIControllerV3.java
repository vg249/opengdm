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

import java.util.Optional;

import org.gobiiproject.gobidomain.services.V3ProjectService;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.dto.auditable.V3ProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Scope(value = "request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_GOBII_V3)
@Api()
@CrossOrigin
public class GOBIIControllerV3 {
    //private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProjectsController.class);
    Logger LOGGER = LoggerFactory.getLogger(GOBIIControllerV3.class);
    @Autowired
    private V3ProjectService projectService = null;

    /**
     * getProjectsList 
     * 
     * Gets list of projects
     * 
     * @param pageNum 0-based page of data used in conjunction with pageSize
     * @param pageSize number of items in response list.
     * @return
     */
    @GetMapping("/projects")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<V3ProjectDTO>> getProjectsList(
            @RequestParam Optional<Integer> pageNum,
            @RequestParam Optional<Integer> pageSize) {
        LOGGER.debug("Querying projects List");
        Integer pageSizeToUse = pageSize.orElse(1000);
        if (pageSizeToUse < 0)  pageSizeToUse = 1000;
        BrApiMasterListPayload<V3ProjectDTO> pagedResult = 
            projectService.getProjects(
                Math.max(0, pageNum.orElse(0)),
                pageSizeToUse
            );
        
        return ResponseEntity.ok(pagedResult);
    }

    public V3ProjectService getProjectService() {
        return projectService;
    }

    public void setProjectService(V3ProjectService projectService) {
        this.projectService = projectService;
    }

}

