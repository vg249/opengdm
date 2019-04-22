package org.gobiiproject.gobiiweb.controllers;

import org.apache.http.HttpRequest;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            List<ProjectDTO> projectDTOs = sampleTrackingProjectService.getProjects();
            return ResponseEntity.ok(projectDTOs);
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
            //projectService.createProject(newProject);
            sampleTrackingProjectService.createProject(newProject);
            return ResponseEntity.ok(newProject);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Server Error");
        }
    }
}
