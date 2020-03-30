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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.gobiiproject.gobidomain.services.gdmv3.ContactService;
import org.gobiiproject.gobidomain.services.gdmv3.ExperimentService;
import org.gobiiproject.gobidomain.services.gdmv3.ProjectService;
import org.gobiiproject.gobiiapimodel.payload.HeaderAuth;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectPatchDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.AuthDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiiweb.automation.PayloadWriter;
import org.gobiiproject.gobiiweb.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Scope(value = "request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_GOBII_V3)
@Api()
@CrossOrigin
@Slf4j
public class GOBIIControllerV3  {
    
    @Autowired
    private ProjectService projectService = null;

    @Autowired
    private ContactService contactService = null;

    @Autowired
    private ExperimentService experimentService = null;

    /**
     * Authentication Endpoint
     * Mimicking same logic used in v1
     * @param request - Request from the client
     * @param response - Response with Headers values filled in TokenFilter
     * @return
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<HeaderAuth> authenticate(HttpServletRequest request,
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
            log.error(e.getMessage(), e);
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
     * @param page 0-based page of data used in conjunction with pageSize
     * @param pageSize number of items in response list.
     * @return
     */
    @GetMapping("/projects")
    @ResponseBody 
    public ResponseEntity<BrApiMasterListPayload<GobiiProjectDTO>> getProjectsList(
            @RequestParam(required=false, defaultValue = "0") Integer page,
            @RequestParam(required=false, defaultValue = "1000") Integer pageSize,
            @RequestParam(required=false) Integer piContactId) {
        log.debug("Querying projects List");
        Integer pageSizeToUse = getPageSize(pageSize);

        PagedResult<GobiiProjectDTO> pagedResult =  projectService.getProjects(
            Math.max(0, page),
            pageSizeToUse,
            piContactId
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
        String userName = projectService.getDefaultProjectEditor();
        GobiiProjectDTO createdDTO = projectService.createProject(project, userName);
        result.setResult(createdDTO);
        result.setMetadata(null);
        return ResponseEntity.created(null).body(result);
    }

    /**
     * Get Project endpoint handler
     * 
     * @param projectId
     * @return
     * @throws Exception
     */
    @GetMapping("/projects/{projectId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<GobiiProjectDTO>> getProject(
        @PathVariable Integer projectId
    ) throws Exception {
        BrApiMasterPayload<GobiiProjectDTO> result = new BrApiMasterPayload<>();
        GobiiProjectDTO project = projectService.getProject(projectId);
        if (project == null) {
            throw new NullPointerException("Project does not exist");
        }
        result.setResult(project);
        result.setMetadata(null);
        return ResponseEntity.ok(result);

    }


    /**
     * For Patch Project
     * @return
     */
    @PatchMapping("/projects/{projectId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<GobiiProjectDTO>> patchProject(
        @PathVariable Integer projectId,
        @RequestBody @Valid final GobiiProjectPatchDTO project,
        BindingResult bindingResult
    ) throws Exception {
        String userName = projectService.getDefaultProjectEditor();
        GobiiProjectDTO dto = projectService.patchProject(projectId, project, userName);
        BrApiMasterPayload<GobiiProjectDTO> payload = new BrApiMasterPayload<>();
        payload.setResult(dto);
        payload.setMetadata(null);
        return ResponseEntity.ok(payload);
    }

    /**
     * Delete Project
     * 
     * @return
     * @throws Exception
     */
    @DeleteMapping("/projects/{projectId}")
    @ResponseBody
    public ResponseEntity<String> deleteProject(
        @PathVariable Integer projectId
    ) throws Exception {
        projectService.deleteProject(projectId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


    /**
     * List Project Properties
     * 
     */
    @GetMapping("/projects/properties")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<CvPropertyDTO>> getProjectProperties(
            @RequestParam(required=false, defaultValue = "0") Integer page,
            @RequestParam(required=false, defaultValue = "1000") Integer pageSize) throws Exception {
        log.debug("Querying project properties List");
        Integer pageSizeToUse = getPageSize(pageSize);
        PagedResult<CvPropertyDTO> pagedResult =  projectService.getProjectProperties(
            Math.max(0, page),
            pageSizeToUse
        );
        BrApiMasterListPayload<CvPropertyDTO> payload = new BrApiMasterListPayload<>(
            pagedResult.getResult(),
            pagedResult.getCurrentPageSize(),
            pagedResult.getCurrentPageNum()
        );
           
        return ResponseEntity.ok(payload);
    }


    /**
     * List Contacts
     * @return
     */
    @GetMapping("/contacts")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<ContactDTO>> getContacts(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize,
        @RequestParam(required=false) Integer organizationId
    ) throws Exception {
        Integer pageSizeToUse = getPageSize(pageSize);
        PagedResult<ContactDTO> pagedResult = contactService.getContacts(
            Math.max(0, page),
            pageSizeToUse,
            organizationId
        );
        BrApiMasterListPayload<ContactDTO> payload = new BrApiMasterListPayload<>(
            pagedResult.getResult(),
            pagedResult.getCurrentPageSize(),
            pagedResult.getCurrentPageNum()
        );
        return ResponseEntity.ok(payload);
    }

    /**
     * Lists Experiments
     * @return
     */
    @GetMapping("/experiments")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<ExperimentDTO>> getExperiments(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize,
        @RequestParam(required=false) Integer projectId
    ) throws Exception {
        Integer pageSizeToUse = getPageSize(pageSize);
        PagedResult<ExperimentDTO> pagedResult = experimentService.getExperiments(
            Math.max(0, page),
            pageSizeToUse,
            projectId
        );
        BrApiMasterListPayload<ExperimentDTO> payload = new BrApiMasterListPayload<>(
            pagedResult.getResult(),
            pagedResult.getCurrentPageSize(),
            pagedResult.getCurrentPageNum()
        );
        return ResponseEntity.ok(payload);
    }

    public ProjectService getProjectService() {
        return projectService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    private Integer getPageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) return 1000;
        return pageSize;
    }

}

