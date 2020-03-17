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

import javax.validation.Valid;

import org.gobiiproject.gobidomain.services.GobiiProjectService;
import org.gobiiproject.gobiiapimodel.payload.HeaderAuth;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiiweb.exceptions.ValidationException;
import org.gobiiproject.gobiimodel.dto.system.AuthDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiiweb.automation.PayloadWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Scope(value = "request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_GOBII_V3)
@Api()
@CrossOrigin
public class GOBIIControllerV3  {

    private static Logger LOGGER = LoggerFactory.getLogger(GOBIIControllerV3.class);
    
    @Autowired
    private GobiiProjectService projectService = null;

    /**
     * Authentication Endpoint
     * Mimicking same logic used in v1
     * @param request - Request from the client
     * @param response - Response with Headers values filled in TokenFilter
     * @return
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity authenticate(HttpServletRequest request,
                                       HttpServletResponse response) {

        try {

            HeaderAuth dtoHeaderAuth = new HeaderAuth();

            PayloadWriter<AuthDTO> payloadWriter = new PayloadWriter<>(
                    request, response, AuthDTO.class);

            payloadWriter.setAuthHeader(dtoHeaderAuth, response);

            return ResponseEntity.ok(dtoHeaderAuth);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }


    }

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

    /**
     * createProject
     * 
     * Create new project
     * @since 2020-03-13
     */
    @PostMapping("/projects")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<GobiiProjectDTO>> createProject(
            @RequestBody @Valid final GobiiProjectRequestDTO project,
            BindingResult bindingResult
    ) throws Exception {
        if (bindingResult.hasErrors()) {
            List<String> info = new ArrayList<String>();
        
            bindingResult.getFieldErrors().forEach(
                objErr -> {
                    info.add(objErr.getField() + " " + objErr.getDefaultMessage());
                }
            );
            throw new ValidationException("Bad Request. " + String.join(", ", info.toArray(new String[info.size()])));
        } 
        BrApiMasterPayload<GobiiProjectDTO> result = new BrApiMasterPayload<>();

        //Get the current user
        String userName = projectService.getDefaultProjectCreator();
        GobiiProjectDTO createdDTO = projectService.createProject(project, userName);
        result.setResult(createdDTO);
        result.setMetadata(null);
        return ResponseEntity.created(null).body(result);
    }

    public GobiiProjectService getProjectService() {
        return projectService;
    }

    public void setProjectService(GobiiProjectService projectService) {
        this.projectService = projectService;
    }

}

