package org.gobiiproject.gobiiweb.controllers;

import edu.emory.mathcs.backport.java.util.Arrays;
import io.swagger.annotations.*;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.ListPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.GermplasmDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GermplasmListDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.ProjectSamplesDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.gobiiproject.gobiiweb.automation.RestResourceLimits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Scope(value="request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_SAMPLE_TRACKING)
@CrossOrigin
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

    /**
     * Lists the projects by page size and page token.
     *
     * @param pageTokenParam - String page token.
     * @param pageSize - Page Size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size.
     * @return Brapi response with list of projects.
     */
    @ApiOperation(
            value = "List all projects",
            notes = "List of all Projects.",
            tags = {"Projects"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Projects")
                    })}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/projects", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity listProjects(
            @RequestParam(value = "pageToken", required = false) String pageTokenParam,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        try {

            Integer pageToken = null;

            if(pageTokenParam != null) {
                try {
                    pageToken = Integer.parseInt(pageTokenParam);
                }
                catch(Exception e) {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Token");
                }
            }

            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_PROJECTS,
                    RestMethodType.GET);

            if(pageSize == null || pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }

            List<ProjectDTO> projectsList = sampleTrackingProjectService.getProjects(pageToken, pageSize);

            BrApiMasterPayload<List<ProjectDTO>> payload = new BrApiMasterPayload<>(projectsList);


            if(projectsList.size() > 0 ) {
                payload.getMetadata().getPagination().setPageSize(projectsList.size());
                if(projectsList.size() >= pageSize) {
                    payload.getMetadata().getPagination().setNextPageToken(
                            projectsList.get(projectsList.size() - 1).getProjectId().toString());
                }
            }

            return ResponseEntity.ok(payload);

        }
        catch(GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error");
        }
    }

    /**
     * Endpoint for getting a specific project with the given project ID
     *
     * @param projectId ID of the requested project
     * @return ResponseEntity with http status code specifying if retrieval of the project is successful.
     * Response body contains the requested Project information
     */

    @ApiOperation(
            value = "Get a project by projectId",
            notes = "Retrieves the Project entity having the specified ID.",
            tags = {"Projects"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Projects : projectId")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
            paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/projects/{projectId:[\\d]+}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getProjectById(
            @ApiParam(value = "ID of the Project to be extracted", required = true)
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
     * Response body contains created resource if project creation is successful.
     */

    @ApiOperation(
            value = "Creates a new project",
            notes = "Creates a new project in the system.",
            tags = {"Projects"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Projects"),
                            @ExtensionProperty(
                                    name="tag-description",
                                    value="A project consists of a group of samples that are, " +
                                            "or will be, genotyped. A project belings to a Principal Investigator (PI), " +
                                            "also called a PI contact. "
                            )
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                paramType = "header", dataType = "string")
    })

    @RequestMapping(value="/projects", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity createProject(
            @ApiParam(required = true)
            @RequestBody ProjectDTO newProject) {
        ProjectDTO createdProject = sampleTrackingProjectService.createProject(newProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @ApiOperation(value="List Experiments", hidden = true)
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

    @ApiOperation(value="List Experiments", hidden = true)
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

    @ApiOperation(value="List Experiments", hidden = true)
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

    @ApiOperation(value="List Experiments", hidden = true)
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

    @ApiOperation(value="List Experiments", hidden = true)
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

    @ApiOperation(value="List Experiments", hidden = true)
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

    @ApiOperation(value="List Experiments", hidden = true)
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
