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

import org.gobiiproject.gobidomain.services.GobiiProjectService;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private static Logger LOGGER = LoggerFactory.getLogger(GOBIIControllerV3.class);
    
    @Autowired
    private GobiiProjectService projectService = null;

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
    @ResponseBody 
    public ResponseEntity<BrApiMasterListPayload<GobiiProjectDTO>> getProjectsList(
            @RequestParam(required=false, defaultValue = "0") Integer pageNum,
            @RequestParam(required=false, defaultValue = "1000") Integer pageSize) {
        LOGGER.debug("Querying projects List");
        Integer pageSizeToUse = pageSize;

        if (pageSizeToUse < 0)  pageSizeToUse = 1000;
        PagedResult<GobiiProjectDTO> pagedResult =  projectService.getProjects(
            Math.max(0, pageNum),
            pageSizeToUse
        );
        BrApiMasterListPayload<GobiiProjectDTO> payload = new BrApiMasterListPayload<>(
            pagedResult.getResult(),
            pagedResult.getCurrentPageSize(),
            pagedResult.getCurrentPageNum()
        );
           
        
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/projects")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<GobiiProjectDTO>> createProject(
            @RequestBody final GobiiProjectRequestDTO project
    ) throws Exception {
        BrApiMasterPayload<GobiiProjectDTO> result = new BrApiMasterPayload<>();
        GobiiProjectDTO createdDTO = projectService.createProject(project);
        result.setResult(createdDTO);
        return ResponseEntity.created(null).body(result);
    }

    public GobiiProjectService getProjectService() {
        return projectService;
    }

    public void setProjectService(GobiiProjectService projectService) {
        this.projectService = projectService;
    }

}

