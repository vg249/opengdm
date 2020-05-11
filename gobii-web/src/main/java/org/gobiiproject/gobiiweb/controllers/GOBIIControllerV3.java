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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.gobiiproject.gobidomain.services.gdmv3.AnalysisService;
import org.gobiiproject.gobidomain.services.gdmv3.ContactService;
import org.gobiiproject.gobidomain.services.gdmv3.DatasetService;
import org.gobiiproject.gobidomain.services.gdmv3.ExperimentService;
import org.gobiiproject.gobidomain.services.gdmv3.MapsetService;
import org.gobiiproject.gobidomain.services.gdmv3.ProjectService;
import org.gobiiproject.gobidomain.services.gdmv3.ReferenceService;
import org.gobiiproject.gobidomain.services.gdmv3.VendorProtocolService;
import org.gobiiproject.gobiiapimodel.payload.HeaderAuth;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.VendorProtocolDTO;
import org.gobiiproject.gobiimodel.dto.request.AnalysisTypeRequest;
import org.gobiiproject.gobiimodel.dto.request.ExperimentPatchRequest;
import org.gobiiproject.gobiimodel.dto.request.ExperimentRequest;
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
import org.springframework.validation.annotation.Validated;
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
    private ProjectService projectService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private VendorProtocolService vendorProtocolService;

    @Autowired
    private ReferenceService referenceService;

    @Autowired
    private MapsetService mapsetService;

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
        this.checkBindingErrors(bindingResult);
        BrApiMasterPayload<GobiiProjectDTO> result = new BrApiMasterPayload<>();

        //Get the current user
        String userName = this.getCurrentUser();

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
        this.checkBindingErrors(bindingResult);
        String userName = this.getCurrentUser();
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

    /**
     * Get Experiment endpoint handler
     * 
     * @param experimentId
     * @return
     * @throws Exception
     */
    @GetMapping("/experiments/{experimentId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<ExperimentDTO>> getExperiment(
        @PathVariable Integer experimentId
    ) throws Exception {
        BrApiMasterPayload<ExperimentDTO> result = new BrApiMasterPayload<>();
        ExperimentDTO experiment = experimentService.getExperiment(experimentId);
        result.setResult(experiment);
        result.setMetadata(null);
        return ResponseEntity.ok(result);
    }

    /**
     * createExperiment
     * 
     * Create new project
     * @since 2020-03-13
     */
    @PostMapping("/experiments")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<ExperimentDTO>> createProject(
            @RequestBody @Valid final ExperimentRequest experiment,
            BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        BrApiMasterPayload<ExperimentDTO> result = new BrApiMasterPayload<>();

        //Get the current user
        String userName = this.getCurrentUser();
        ExperimentDTO createdDTO = experimentService.createExperiment(experiment, userName);
        result.setResult(createdDTO);
        result.setMetadata(null);
        return ResponseEntity.created(null).body(result);
    }


    /**
     * Update Experiment endpoint handler
     * 
     * @param experimentId
     * @return
     * @throws Exception
     */
    @PatchMapping("/experiments/{experimentId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<ExperimentDTO>> updateExperiment(
        @PathVariable Integer experimentId,
        @RequestBody @Valid final ExperimentPatchRequest request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        BrApiMasterPayload<ExperimentDTO> result = new BrApiMasterPayload<>();
        String user = this.getCurrentUser();
        ExperimentDTO experiment = experimentService.updateExperiment(experimentId, request, user);
        result.setResult(experiment);
        result.setMetadata(null);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete Experiment endpoint handler
     * 
     * @param experimentId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/experiments/{experimentId}")
    @ResponseBody
    public ResponseEntity<String> deleteExperiment(
        @PathVariable Integer experimentId
    ) throws Exception {
        experimentService.deleteExperiment(experimentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * List Vendor Protocols
     * @return
     */
    @GetMapping("/vendorprotocols")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<VendorProtocolDTO>> getVendorProtocols(
            @RequestParam(required=false, defaultValue = "0") Integer page,
            @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {

        Integer pageSizeToUse = getPageSize(pageSize);

        PagedResult<VendorProtocolDTO> pagedResult =
                vendorProtocolService.getVendorProtocols(
                        Math.max(0, page),
                        pageSizeToUse
        );
        BrApiMasterListPayload<VendorProtocolDTO> payload =
                new BrApiMasterListPayload<>(
                        pagedResult.getResult(),
                        pagedResult.getCurrentPageSize(),
                        pagedResult.getCurrentPageNum()
        );
        return ResponseEntity.ok(payload);
    }

    /**
     * List Analyses
     * 
     * @return
     */
    @GetMapping("/analyses")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<AnalysisDTO>> getAnalyses(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        Integer pageSizeToUse = getPageSize(pageSize);
        PagedResult<AnalysisDTO> pagedResult = analysisService.getAnalyses(page, pageSizeToUse);

        BrApiMasterListPayload<AnalysisDTO> payload = new BrApiMasterListPayload<>(
            pagedResult.getResult(),
            pagedResult.getCurrentPageSize(),
            pagedResult.getCurrentPageNum()
        );

        return ResponseEntity.ok(payload);
    }

    /**
     * Create Analyses
     * 
     * @return
     */
    @PostMapping("/analyses")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<AnalysisDTO>> createAnalysis(
        @RequestBody @Validated(AnalysisDTO.Create.class) final AnalysisDTO analysisRequest,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();
        AnalysisDTO result = analysisService.createAnalysis(analysisRequest, user);

        BrApiMasterPayload<AnalysisDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(result);

        return ResponseEntity.created(null).body(payload);
    }

    /**
     * Update Analysis  By Id
     */
    @PatchMapping("/analyses/{analysisId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<AnalysisDTO>> patchAnalysis(
        @PathVariable Integer analysisId,
        @RequestBody final AnalysisDTO analysisRequest,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();

        AnalysisDTO result = analysisService.updateAnalysis(analysisId, analysisRequest, user);

        BrApiMasterPayload<AnalysisDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(result);

        return ResponseEntity.ok(payload);
    }

    /**
     * Get Analysis By Id
     */
    @GetMapping("/analyses/{analysisId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<AnalysisDTO>> getAnalysis(
        @PathVariable Integer analysisId
    ) throws Exception {
        AnalysisDTO analysisDTO = analysisService.getAnalysis(analysisId);
        BrApiMasterPayload<AnalysisDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(analysisDTO);

        return ResponseEntity.ok(payload);
    }

    /**
     * Get Analysis By Id
     */
    @DeleteMapping("/analyses/{analysisId}")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteAnalysis(
        @PathVariable Integer analysisId
    ) throws Exception {
        analysisService.deleteAnalysis(analysisId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Create Analysis Type
     * @return
     */
    @PostMapping("/analyses/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<AnalysisTypeDTO>> createAnalysisType(
        @RequestBody @Valid final AnalysisTypeRequest analysisTypeRequest,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();
        AnalysisTypeDTO result = analysisService.createAnalysisType(analysisTypeRequest, user);
        BrApiMasterPayload<AnalysisTypeDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(result);

        return ResponseEntity.created(null).body(payload);
    }

    /**
     * List Analysis Types
     * @return
     */
    @GetMapping("/analyses/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<AnalysisTypeDTO>> getAnalysisTypes(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        Integer pageSizeToUse = getPageSize(pageSize);
        PagedResult<AnalysisTypeDTO> result = analysisService.getAnalysisTypes(page, pageSizeToUse);
        BrApiMasterListPayload<AnalysisTypeDTO> payload = new BrApiMasterListPayload<>(
            result.getResult(),
            result.getCurrentPageNum(),
            result.getCurrentPageSize()
        );
        return ResponseEntity.ok(payload);
    }

    //-----Dataset API Handlers section

    /**
     * Create Dataset
     * @return
     */
    @PostMapping("/datasets")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<DatasetDTO>> createDataset(
        @RequestBody @Validated(DatasetRequestDTO.Create.class) final DatasetRequestDTO request,
        BindingResult bindingResult
    ) throws Exception {
       this.checkBindingErrors(bindingResult);
       String user = this.getCurrentUser();
       DatasetDTO result = datasetService.createDataset(request, user);
       BrApiMasterPayload<DatasetDTO> payload = new BrApiMasterPayload<>();
       payload.setResult(result);
       payload.setMetadata(null);
       return ResponseEntity.created(null).body(payload);
           
    }

    /**
     * Datasets listing
     * @return
     */
    @GetMapping("/datasets")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<DatasetDTO>> getDatasets(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize,
        @RequestParam(required=false) Integer experimentId,
        @RequestParam(required=false) Integer datasetTypeId
    ) throws Exception {
        Integer pageSizeToUse = getPageSize(pageSize);
        try {
        PagedResult<DatasetDTO> pagedResult = datasetService.getDatasets(
            page, pageSizeToUse, experimentId, datasetTypeId
        );

        BrApiMasterListPayload<DatasetDTO> payload = new BrApiMasterListPayload<>(
            pagedResult.getResult(),
            pagedResult.getCurrentPageSize(),
            pagedResult.getCurrentPageNum()
        );

        return ResponseEntity.ok(payload);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Get datasets by Id
     * 
     */
    @GetMapping("/datasets/{datasetId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<DatasetDTO>> getDataset(
        @PathVariable Integer datasetId
    ) throws Exception {
        DatasetDTO datasetDTO = datasetService.getDataset(datasetId);
        BrApiMasterPayload<DatasetDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(datasetDTO);
        return ResponseEntity.ok(payload);
    }

    /**
     * Update dataset by Id
     * @return
     */
    @PatchMapping("/datasets/{datasetId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<DatasetDTO>> updateDataset(
        @PathVariable Integer datasetId,
        @RequestBody @Valid final DatasetRequestDTO request,
        BindingResult bindingResult
    ) throws Exception {
        String user = this.getCurrentUser();
        DatasetDTO datasetDTO = datasetService.updateDataset(datasetId, request, user);
        BrApiMasterPayload<DatasetDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(datasetDTO);
        return ResponseEntity.ok(payload);
    }

    /**
     * Delete dataset
     * @return
     */
    @DeleteMapping("/datasets/{datasetId}")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteDataset(
        @PathVariable Integer datasetId
    ) throws Exception {
        datasetService.deleteDataset(datasetId);
        return ResponseEntity.noContent().build();
    }

    //------ DatasetTypes
    /**
     * List dataset types
     * @return
     */
    @GetMapping("/datasets/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<DatasetTypeDTO>> getDatasetTypes(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        Integer pageSizeToUse = this.getPageSize(pageSize);

        PagedResult<DatasetTypeDTO> pagedResult = datasetService.getDatasetTypes(page, pageSizeToUse);
        
        BrApiMasterListPayload<DatasetTypeDTO> payload = new BrApiMasterListPayload<>(
            pagedResult.getResult(),
            pagedResult.getCurrentPageSize(),
            pagedResult.getCurrentPageNum()
        );

        return ResponseEntity.ok(payload);
    }

    /**
     * Create Analysis Type
     * @return
     */
    @PostMapping("/datasets/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<DatasetTypeDTO>> createDatasetType(
        @RequestBody @Validated(DatasetTypeDTO.Create.class) final DatasetTypeDTO datasetTypeRequest,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();
        DatasetTypeDTO result = datasetService.createDatasetType(
            datasetTypeRequest.getDatasetTypeName(),
            datasetTypeRequest.getDatasetTypeDescription(),
            user
        );
        BrApiMasterPayload<DatasetTypeDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(result);

        return ResponseEntity.created(null).body(payload);
    }

    /**
     * Get References
     * @return
     */
    @GetMapping("/references")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<ReferenceDTO>> getReferences(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        Integer pageSizeToUse = this.getPageSize(pageSize);
        PagedResult<ReferenceDTO> pagedResult = referenceService.getReferences(page, pageSizeToUse);
        BrApiMasterListPayload<ReferenceDTO> payload = new BrApiMasterListPayload<>(
            pagedResult.getResult(),
            pagedResult.getCurrentPageSize(),
            pagedResult.getCurrentPageNum()
        );

        return ResponseEntity.ok(payload);
        
    }

    //-------- Mapsets ------------

    /**
     * Get Mapsets list
     * @return
     */
    @GetMapping("/mapsets")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<MapsetDTO>> getMapsets(
        @RequestParam(required=false, defaultValue="0") Integer page,
        @RequestParam(required=false, defaultValue="1000") Integer pageSize,
        @RequestParam(required=false) Integer mapsetTypeId
    ) throws Exception {
        Integer pageSizeToUse = this.getPageSize(pageSize);
        PagedResult<MapsetDTO> pagedResult = mapsetService.getMapsets(page, pageSizeToUse, mapsetTypeId);

        BrApiMasterListPayload<MapsetDTO> payload = new BrApiMasterListPayload<>(
            pagedResult.getResult(),
            pagedResult.getCurrentPageSize(),
            pagedResult.getCurrentPageNum()
        );
        return ResponseEntity.ok(payload);
    }

    /**
     * Create mapset entry
     * @return
     */
    @PostMapping("/mapsets")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<MapsetDTO>> createMapset(
        @RequestBody @Validated(MapsetDTO.Create.class) final MapsetDTO mapset,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();
        MapsetDTO mapsetDTO = mapsetService.createMapset(mapset, user);
        BrApiMasterPayload<MapsetDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(mapsetDTO);
        return ResponseEntity.created(null).body(payload);
    }

    /**
     * Get mapset entry by id
     * @return
     */
    @GetMapping("/mapsets/{mapsetId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<MapsetDTO>> getMapset(
        @PathVariable Integer mapsetId
    ) throws Exception {
        MapsetDTO result = mapsetService.getMapset(mapsetId);
        BrApiMasterPayload<MapsetDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(result);
        return ResponseEntity.ok(payload);
    }

    /**
     * Update mapset
     * @return
     */
    @PatchMapping("/mapsets/{mapsetId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<MapsetDTO>> updateMapset(
        @PathVariable Integer mapsetId,
        @RequestBody @Validated(MapsetDTO.Update.class) final MapsetDTO patchData,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String editedBy = this.getCurrentUser();

        MapsetDTO result = mapsetService.updateMapset(mapsetId, patchData, editedBy);
        BrApiMasterPayload<MapsetDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(result);
        return ResponseEntity.ok(payload);
    }

    /**
     * Delete mapset
     */
    @DeleteMapping("/mapsets/{mapsetId}")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteMapset(
        @PathVariable Integer mapsetId
    ) throws Exception {
        mapsetService.deleteMapset(mapsetId);
        return ResponseEntity.noContent().build();
    }

    //----Mapset Type
    @PostMapping("/mapsets/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<MapsetTypeDTO>> createMapsetType(
        @RequestBody @Validated(MapsetTypeDTO.Create.class) final MapsetTypeDTO mapsetTypeRequest,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();
        MapsetTypeDTO result = mapsetService.createMapsetType(
            mapsetTypeRequest.getMapsetTypeName(),
            mapsetTypeRequest.getMapsetTypeDescription(),
            user
        );

        BrApiMasterPayload<MapsetTypeDTO> payload = new BrApiMasterPayload<>();
        payload.setMetadata(null);
        payload.setResult(result);

        return ResponseEntity.created(null).body(payload);
    }

    
    public ProjectService getProjectService() {
        return projectService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    private String getCurrentUser() {
        return projectService.getDefaultProjectEditor();
    }

    private Integer getPageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) return 1000;
        return pageSize;
    }

    private void checkBindingErrors(BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            
            List<String> info = new ArrayList<String>();
        
            bindingResult.getFieldErrors().forEach(
                objErr -> {
                    info.add(objErr.getField() + " " + objErr.getDefaultMessage());
                }
            );
            throw new ValidationException("Bad Request. " + String.join(", ", info.toArray(new String[info.size()])));
        } 
    }

}

