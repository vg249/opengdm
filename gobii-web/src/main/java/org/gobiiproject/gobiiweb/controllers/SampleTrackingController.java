package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.http.HttpRequest;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.ListPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.ResponseProcessingException;
import java.security.Principal;
import java.util.List;

@Scope(value="request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_SAMPLE_TRACKING)
public class SampleTrackingController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SampleTrackingController.class);

    @Autowired
    private ProjectService<ProjectDTO> sampleTrackingProjectService = null;

    @RequestMapping(value="/projects", method= RequestMethod.GET)
    public @ResponseBody ResponseEntity listProjects(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            List<ProjectDTO> projectsList = sampleTrackingProjectService.getProjects();
            ListPayload<ProjectDTO> payload = new ListPayload<ProjectDTO>();
            payload.setData(projectsList);
            return ResponseEntity.ok(payload);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    @RequestMapping(value="/projects/{projectId:[\\d]+}")
    public @ResponseBody ResponseEntity getProjectById(
            @PathVariable Integer projectId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            ProjectDTO project = sampleTrackingProjectService.getProjectById(projectId);
            return ResponseEntity.ok(project);
        }
        catch (GobiiException gobiiE) {
           if(gobiiE.getGobiiValidationStatusType() == GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gobiiE.getMessage());
           }
           else {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gobiiE.getMessage());
           }
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    @RequestMapping(value="/projects", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity createProject(
            @RequestBody ProjectDTO newProject,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            sampleTrackingProjectService.createProject(newProject);
            return ResponseEntity.ok(newProject);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Server Error");
        }
    }
}
