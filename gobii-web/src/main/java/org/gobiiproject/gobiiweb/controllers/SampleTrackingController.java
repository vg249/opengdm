package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.ListPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.GermplasmDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GermplasmListDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.ProjectSamplesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Scope(value="request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_SAMPLE_TRACKING)
public class SampleTrackingController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SampleTrackingController.class);

    @Autowired
    private ProjectService<ProjectDTO> sampleTrackingProjectService = null;

    @Autowired
    private ExperimentService<ExperimentDTO> sampleTrackingExperimentService = null;

    //@Autowired
    //private DnaSampleService<DnaSampleDTO> sampleTrackingDnaSampleService = null;
    @Autowired
    private ContactService contactService;


    @RequestMapping(value="/projects", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity listProjects(
    ) {
            List<ProjectDTO> projectsList = sampleTrackingProjectService.getProjects();
            BrApiMasterPayload<List<ProjectDTO>> payload = new BrApiMasterPayload<>(projectsList);
            return ResponseEntity.ok(payload);
    }

    @RequestMapping(value="/projects/{projectId:[\\d]+}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getProjectById(
            @PathVariable Integer projectId
    ) {
            ProjectDTO project = sampleTrackingProjectService.getProjectById(projectId);
            BrApiMasterPayload<ProjectDTO> payload = new BrApiMasterPayload<>(project);
            return ResponseEntity.ok(payload);
    }

    /**
     * Endpoint to create new project.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param newProject - New project to be created.
     *                   Json request body automatically deserialized to ProjectDTO.
     * @return ResponseEntity with http status code respective of successful creation or failure.
     * Response body contains created resource.
     */
    @RequestMapping(value="/projects", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity createProject(@RequestBody ProjectDTO newProject) {
        ProjectDTO createdProject = sampleTrackingProjectService.createProject(newProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @RequestMapping(value="/experiments", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity listExperiments(
            HttpServletRequest request,
            HttpServletResponse response) {

        try {

            List<ExperimentDTO> experimentsList = sampleTrackingExperimentService.getExperiments();
            ListPayload<ExperimentDTO> payload = new ListPayload<ExperimentDTO>();
            payload.setData(experimentsList);

            return ResponseEntity.ok(payload);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    @RequestMapping(value="/experiments", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity createExperiment(
            @RequestBody ExperimentDTO newExperiment,
            HttpServletRequest request,
            HttpServletResponse response){

        try {

            sampleTrackingExperimentService.createExperiment(newExperiment);
            return ResponseEntity.ok(newExperiment);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/experiments/{experimentId:[\\d]+}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity getExperimentById(
            @PathVariable Integer experimentId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        try {

            ExperimentDTO experimentDTO = sampleTrackingExperimentService.getExperimentById(experimentId);
            return ResponseEntity.ok(experimentDTO);

        }
        catch (GobiiException gobiiE) {
            throw gobiiE;
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    @RequestMapping(value = "/projects/{projectId:[\\d]+}/samples", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity createSamples(
        @RequestBody ProjectSamplesDTO newProjectSamples,
        @PathVariable Integer projectId,
        HttpServletRequest request,
        HttpServletResponse response) {

        try {
            //ProjectSamplesDTO createdProjectSamples = .createSamples(newProjectSamples);
            newProjectSamples.setProjectId(projectId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProjectSamples);

        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }

    }

    @RequestMapping(value = "/germplasm", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity createGermplasm(
            @RequestBody GermplasmListDTO germplasmListDTO,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {

            // temporary only
            int idCount = 0;
            for (GermplasmDTO germplasmDTO: germplasmListDTO.getGermplasms()) {
                germplasmDTO.setId(idCount++);

            }

            return ResponseEntity.ok(germplasmListDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }

    }

    @RequestMapping(value = "/germplasm", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity listGermplasms(
            HttpServletRequest request,
            HttpServletResponse response) {
        try {

            List<GermplasmDTO> germplasmDTOList = new ArrayList<>();

            return ResponseEntity.ok(germplasmDTOList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }

    }

    @RequestMapping(value = "/germplasm/{germplasmId:[\\d]+}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity listGermplasms(
            @PathVariable Integer germplasmId,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {

            GermplasmDTO germplasmDTO = new GermplasmDTO();

            return ResponseEntity.ok(germplasmDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }

    }

}
