package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Bytes;
import io.swagger.annotations.*;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobidomain.services.SampleService;
import org.gobiiproject.gobidomain.services.impl.sampletracking.ExperimentServiceImpl;
import org.gobiiproject.gobiiapimodel.payload.HeaderAuth;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.ListPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.*;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GermplasmListDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.ProjectSamplesDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SampleMetadataDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.modelmapper.EntityFieldBean;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.automation.RestResourceLimits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(value="request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_SAMPLE_TRACKING)
public class SampleTrackingController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SampleTrackingController.class);

    @Autowired
    private ProjectService<ProjectDTO> sampleTrackingProjectService = null;

    @Autowired
    private ExperimentServiceImpl sampleTrackingExperimentService = null;

    /**
     * Authenticates the User.
     * @param noContentExpected
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(
            value = "Authentication",
            notes = "Authentication",
            tags = {"Authentication"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Authentication")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Username", value="User Id", required=true,
                    paramType = "header", dataType = "string"),
            @ApiImplicitParam(name="X-Password", value="User password", required=true,
                    paramType = "header", dataType = "string"),
    })
    @ApiResponses(value={
            @ApiResponse(code=200, message = "OK", responseHeaders=@ResponseHeader(
                    name="X-Auth-Token ", description = "API key to authenticate GDM api calls",
                    response = String.class
            ))
    })
    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity authenticate(@RequestBody(required = false) String noContentExpected,
                               HttpServletRequest request,
                               HttpServletResponse response) throws GobiiException {

        HeaderAuth dtoHeaderAuth = new HeaderAuth();

        try {


            dtoHeaderAuth.setToken(response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_TOKEN));
            dtoHeaderAuth.setGobiiCropType(response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_GOBII_CROP));
            dtoHeaderAuth.setUserName(response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_USERNAME));


        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Authentication Failure");
        }

        return ResponseEntity.ok(dtoHeaderAuth);
    }

    /**
     * Lists the projects by page size and page token.
     *
     * @param pageSize - Page Size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size.
     * @param pageNum - Page number for the list defined by pageSize.
     * @return Brapi response with list of projects.
     */
    @ApiOperation(
            value = "List all projects",
            notes = "List of all Projects.",
            tags = {"Project"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Projects")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string"),
    })
    @ApiResponses(value={@ApiResponse(code=200, message="successful operation",
            response=ProjectDTO.class)})
    @RequestMapping(value="/projects", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity listProjects(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum
    ) {
        try {

            if(pageSize == null) {
                //Default Brapi Page Size.
                //TODO: Add it to the Gobii Web XML resource limit
                pageSize = 1000;
            }

            if(pageNum == null) {
                pageNum = 0;
            }

            List<ProjectDTO> projectsList = sampleTrackingProjectService.getProjects(pageNum, pageSize);

            Map<String, List<ProjectDTO>> brapiDataList = new HashMap<>();
            brapiDataList.put("data", projectsList);

            BrApiMasterPayload<Object> payload = new BrApiMasterPayload<>(brapiDataList);


            payload.getMetaData().getPagination().setPageSize(pageSize);
            payload.getMetaData().getPagination().setCurrentPage(pageNum);

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
            tags = {"Project"},
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
    @RequestMapping(value="/projects/{projectId:[\\d]+}", method=RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity getProjectById(
            @ApiParam(value = "ID of the Project to be extracted", required = true)
            @PathVariable Integer projectId
    ) throws GobiiException {
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
            value = "Create new project",
            notes = "Creates a new project in the system.",
            tags = {"Project"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Projects")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/projects", method=RequestMethod.POST, consumes = "application/json")
    public @ResponseBody ResponseEntity createProject(
            @ApiParam(required = true)
            @RequestBody ProjectDTO newProject) throws GobiiException {

        ProjectDTO createdProject = sampleTrackingProjectService.createProject(newProject);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }


    /**
     * Lists the experiments by page size and page token.
     *
     * @param pageTokenParam - String page token.
     * @param pageSize - Page Size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size.
     * @param pageNum - Page number for the list defined by pageSize.
     * @return Brapi response with list of experiments.
     */
    @ApiOperation(
            value = "List all experiments",
            notes = "List of all Experiments.",
            tags = {"Experiment"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Experiments")
                    })}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string"),
    })
    @ApiResponses(value={@ApiResponse(code=200, message="successful operation",
            response=ExperimentDTO.class)})
    @RequestMapping(value="/experiments", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody ResponseEntity listExperiment(
            @RequestParam(value = "pageToken", required = false) String pageTokenParam,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum
    ) {
        try {
            List<ExperimentDTO> experimentsList = sampleTrackingExperimentService.getExperiments();
            ListPayload<ExperimentDTO> payload = new ListPayload<ExperimentDTO>();
            payload.setData(experimentsList);
            return ResponseEntity.ok(payload);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }


    /**
     * Endpoint to create new experiment.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param newExperiment - New experiment to be created.
     *                   Json request body automatically deserialized to ExperimentDTO.
     * @param dataFile - File associated with Experiment to be created.
     * @return ResponseEntity with http status code respective of successful creation or failure.
     * Response body contains created resource if experiment creation is successful.
     */
    @ApiOperation(
            value = "Create new experiment",
            notes = "Creates new experiment in the system.",
            tags = {"Experiment"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Experiments"),
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="experimentMetaData", value="The file to be uploaded",
                    required = true, dataType = "string",
                    paramType = "formData"),
            @ApiImplicitParam(name="dataFile", value="The file to be uploaded",
                    dataType = "string", paramType = "formData"),
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/experiments", method=RequestMethod.POST, consumes="multipart/form-data")
    public @ResponseBody ResponseEntity createExperiment(
            @ApiParam(hidden = true)
            @RequestPart(name = "dataFile", required = false) MultipartFile dataFile,
            @ApiParam(hidden = true)
            @RequestPart(name="experimentMetaData") ExperimentDTO newExperiment,
            HttpServletRequest request) {


        try {

            byte[] dataFileBytes = dataFile.getBytes();

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            String dataFileName = dataFile.getName();

            if(dataFileName.isEmpty()) {

                dataFileName = "experimentDataFile";

            }

            sampleTrackingExperimentService.saveExperimentDataFile(cropType, dataFileName, dataFileBytes);

            sampleTrackingExperimentService.createExperiment(newExperiment);

            return ResponseEntity.ok(newExperiment);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint for getting a specific experiment with the given experiment ID
     *
     * @param experimentId ID of the requested experiment
     * @return ResponseEntity with http status code specifying if retrieval of the Experiment is successful.
     * Response body contains the requested Experiment information
     */
    @ApiOperation(
            value = "Get an experiment by experimentId",
            notes = "Retrieves the Experiment entity having the specified ID.",
            tags = {"Experiment"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Experiment : experimentId")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value = "/experiments/{experimentId:[\\d]+}", method = RequestMethod.GET,
            produces = "application/json")
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


    /**
     * Endpoint to create new samples for given projectId.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param newProjectSamples - List samples to be created to project.
     *                          Json request body automatically deserialized to ProjectSamplesDTO.
     *                          projectId and Samples[] will be the request body.
     * @return ResponseEntity with http status code 200 for OK accepted.
     * Response body contains jobId and related details to check the success/failure of asynchronous operation.
     */
    @ApiOperation(
            value = "Create new samples",
            notes = "Creates a new samples in the system.",
            tags = {"Sample"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Samples")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/samples", method=RequestMethod.POST, consumes = "application/json")
    public @ResponseBody ResponseEntity createSamples(
        @RequestBody ProjectSamplesDTO newProjectSamples) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(newProjectSamples);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }


    /**
     * Endpoint to upload samples for given project Id.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param sampleFile - Tab delimited sample file, with respective columns as per design document.
     * @return ResponseEntity with http status code 200 for OK accepted.
     * Response body contains jobId and related details to check the success of asynchronous operation.
     *
     */
    @ApiOperation(
            value = "Upload new samples",
            notes = "Creates a new samples in the system.",
            tags = {"Sample"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Upload Samples"),
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="sampleMetaData", value="The file to be uploaded",
                    dataType = "string", paramType = "formData"),
            @ApiImplicitParam(name="sampleFile", value="The file to be uploaded",
                    dataType = "string", paramType = "formData", required = true),
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/samples/upload", method=RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody ResponseEntity uploadSamples(
            @ApiParam(hidden = true)
            @RequestPart("sampleFile") MultipartFile sampleFile,
            @ApiParam(hidden = true)
            @RequestPart("sampleMetaData") SampleMetadataDTO sampleMetaData) {

        InputStream is;

        BufferedReader br;

        Map<String, List<GobiiFileColumn>> fileColumnByTableMap = new HashMap<>();

        try {

            is = sampleFile.getInputStream();

            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String fileHeader = br.readLine();

            String[] fileHeaderList = fileHeader.split("\t");

            Map<String, EntityFieldBean> dtoEntityMap = ModelMapper.getDtoEntityMap(DnaSampleDTO.class);

            for(int i = 0; i < fileHeaderList.length; i++) {

                GobiiFileColumn gobiiFileColumn = new GobiiFileColumn();

                String columnHeader = fileHeaderList[i];

                EntityFieldBean entityField = null;

                if(sampleMetaData.getMap().containsKey(columnHeader)) {

                    String dtoProp = sampleMetaData.getMap().get(columnHeader);

                    if(dtoEntityMap.containsKey(dtoProp) && dtoEntityMap.get(dtoProp) != null) {

                        entityField = dtoEntityMap.get(dtoProp);

                    }
                    else {

                        if(dtoProp.substring(dtoProp.lastIndexOf(".")) == "properties"
                                && dtoProp.substring(0, dtoProp.lastIndexOf(".")) == "germplasm") {

                            entityField = new EntityFieldBean();

                            entityField.setColumnName("49");

                            entityField.setTableName(CvGroup.CVGROUP_GERMPLASM_PROP.getCvGroupName());

                        }
                        else if (dtoProp.substring(dtoProp.lastIndexOf(".")) == "properties"
                                      && dtoProp.substring(0, dtoProp.lastIndexOf(".")) == "") {

                            entityField = new EntityFieldBean();

                            entityField.setColumnName("50");

                            entityField.setTableName(CvGroup.CVGROUP_DNASAMPLE_PROP.getCvGroupName());

                        }
                        else if (dtoProp == "germplasmSpecies") {

                            entityField = new EntityFieldBean();

                            entityField.setColumnName("species_name");

                            entityField.setTableName("germplasm");

                        }
                        else if(dtoProp == "germplasmType") {

                            entityField = new EntityFieldBean();

                            entityField.setColumnName("type_name");

                            entityField.setTableName("germplasm");
                        }
                    }

                }
                else {

                    if(dtoEntityMap.containsKey(columnHeader)) {

                        entityField = dtoEntityMap.get(columnHeader);

                    }
                }

                if(entityField != null) {

                    gobiiFileColumn.setName(entityField.getColumnName());

                    gobiiFileColumn.setCCoord(i);

                    gobiiFileColumn.setRCoord(1);

                    gobiiFileColumn.setGobiiColumnType(GobiiColumnType.CSV_COLUMN);

                    gobiiFileColumn.setSubcolumn(false);

                    if(entityField.getTableName() != null) {

                        if (!fileColumnByTableMap.containsKey(entityField.getTableName())) {

                            fileColumnByTableMap.put(entityField.getTableName(), new ArrayList<>());

                        }

                        fileColumnByTableMap.get(entityField.getTableName()).add(gobiiFileColumn);
                    }

                }

            }

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("");

        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    /**
     * Endpoint to create dataset under given experiment Id.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param newDataset - Dataset metadata.
     * @return ResponseEntity with http status code 201 if successful.
     */
    @ApiOperation(
            value = "Create new dataset",
            notes = "Creates a new dataset in the system.",
            tags = {"Dataset"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Dataset"),
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/dataset", method=RequestMethod.POST, consumes = "application/json")
    public @ResponseBody ResponseEntity createDataset(@RequestBody DataSetDTO newDataset) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    /**
     * Endpoint to upload data to given Dataset Id.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param genotypeFile - Genotype files as application/zip
     * @param datasetMetaData - Meta data related to dataset and Vendor file urls to
     *                        download data from external sources.
     * @return ResponseEntity with http status code 200 for OK accepted.
     * Response body contains jobId and related details to check the success of asynchronous operation.
     */
    @ApiOperation(
            value = "Upload data to dataset",
            notes = "Upload data to the dataset.",
            tags = {"Dataset"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Upload Data"),
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="datasetMetaData", value="Metadata associated with data",
                    dataType = "string", paramType = "formData", required = true),
            @ApiImplicitParam(name="genotypeFile", value="The file to be uploaded",
                    dataType = "string", paramType = "formData", required = true),
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/dataset/{datasetId}/data", method=RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody ResponseEntity uploadDatasetData(
            @ApiParam(hidden = true)
            @RequestPart("genotypeFile") MultipartFile genotypeFile,
            @ApiParam(hidden = true)
            @RequestPart("genotypeMetaData") DataSetDataDTO datasetMetaData,
            @PathVariable Integer datasetId) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }


    @ApiOperation(value="dummy", hidden = true)
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

    @ApiOperation(value="dummy", hidden = true)
    @RequestMapping(value = "/germplasm", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity listGermplasms(
            HttpServletRequest request,
            HttpServletResponse response) {
        try {

            List<GermplasmDTO> germplasmDTOList = new ArrayList<>();

            GermplasmDTO germplasmDTO = new GermplasmDTO();
            germplasmDTO.setGermplasmName("foo germplasm");
            germplasmDTO.setExternalCode("external bar code");
            germplasmDTO.setGermplasmSpecies("foo species");
            germplasmDTO.setGermplasmType("foo type");

            germplasmDTOList.add(germplasmDTO);

            return ResponseEntity.ok(germplasmDTOList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }

    }

    @ApiOperation(value="dummy", hidden = true)
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
