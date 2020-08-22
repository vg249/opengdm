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

import org.gobiiproject.gobidomain.services.gdmv3.*;
import org.gobiiproject.gobiiapimodel.payload.HeaderAuth;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterListPayload;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.*;
import org.gobiiproject.gobiimodel.dto.system.AuthDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Protocol;
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

    @Autowired
    private MarkerGroupService markerGroupService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private CvService cvService;

    @Autowired
    private PlatformService platformService;

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
    public ResponseEntity<BrApiMasterListPayload<ProjectDTO>> getProjectsList(
            @RequestParam(required=false, defaultValue = "0") Integer page,
            @RequestParam(required=false, defaultValue = "1000") Integer pageSize,
            @RequestParam(required=false) Integer piContactId) throws Exception {
        log.debug("Querying projects List");
        Integer pageSizeToUse = getPageSize(pageSize);

        PagedResult<ProjectDTO> pagedResult =  projectService.getProjects(
            Math.max(0, page),
            pageSizeToUse,
            piContactId
        );
        BrApiMasterListPayload<ProjectDTO> payload = this.getMasterListPayload(pagedResult);   
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
    public ResponseEntity<BrApiMasterPayload<ProjectDTO>> createProject(
            @RequestBody @Validated(ProjectDTO.Create.class) final ProjectDTO project,
            BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);

        //Get the current user
        String userName = this.getCurrentUser();

        ProjectDTO createdDTO = projectService.createProject(project, userName);
        BrApiMasterPayload<ProjectDTO> result = this.getMasterPayload(createdDTO);
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
    public ResponseEntity<BrApiMasterPayload<ProjectDTO>> getProject(
        @PathVariable Integer projectId
    ) throws Exception {
        ProjectDTO project = projectService.getProject(projectId);
        if (project == null) {
            throw new NullPointerException("Project does not exist");
        }
        BrApiMasterPayload<ProjectDTO> result = this.getMasterPayload(project);
        return ResponseEntity.ok(result);
    }


    /**
     * For Patch Project
     * @return
     */
    @PatchMapping("/projects/{projectId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<ProjectDTO>> patchProject(
        @PathVariable Integer projectId,
        @RequestBody @Validated(ProjectDTO.Update.class) final ProjectDTO project,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String userName = this.getCurrentUser();
        ProjectDTO dto = projectService.patchProject(projectId, project, userName);
        BrApiMasterPayload<ProjectDTO> payload = this.getMasterPayload(dto);
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
        BrApiMasterListPayload<CvPropertyDTO> payload = this.getMasterListPayload(pagedResult);   
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
        BrApiMasterListPayload<ContactDTO> payload = this.getMasterListPayload(pagedResult);
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
        BrApiMasterListPayload<ExperimentDTO> payload = this.getMasterListPayload(pagedResult);
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
        ExperimentDTO experiment = experimentService.getExperiment(experimentId);
        BrApiMasterPayload<ExperimentDTO> result = this.getMasterPayload(experiment);
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
            @RequestBody @Validated(ExperimentDTO.Create.class) final ExperimentDTO experiment,
            BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        //Get the current user
        String userName = this.getCurrentUser();

        ExperimentDTO createdDTO = experimentService.createExperiment(experiment, userName);
        BrApiMasterPayload<ExperimentDTO> result = this.getMasterPayload(createdDTO);
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
        @RequestBody @Validated(ExperimentDTO.Update.class) final ExperimentDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();

        ExperimentDTO experiment = experimentService.updateExperiment(experimentId, request, user);
        BrApiMasterPayload<ExperimentDTO> result = this.getMasterPayload(experiment);
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
        BrApiMasterListPayload<VendorProtocolDTO> payload = this.getMasterListPayload(pagedResult);
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

        BrApiMasterListPayload<AnalysisDTO> payload = this.getMasterListPayload(pagedResult);
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

        BrApiMasterPayload<AnalysisDTO> payload = this.getMasterPayload(result);
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

        BrApiMasterPayload<AnalysisDTO> payload = this.getMasterPayload(result);
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
        BrApiMasterPayload<AnalysisDTO> payload = this.getMasterPayload(analysisDTO);
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
    public ResponseEntity<BrApiMasterPayload<CvTypeDTO>> createAnalysisType(
        @RequestBody @Validated(CvTypeDTO.Create.class) final CvTypeDTO analysisTypeRequest,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();
        CvTypeDTO result = analysisService.createAnalysisType(analysisTypeRequest, user);
        BrApiMasterPayload<CvTypeDTO> payload = this.getMasterPayload(result);
        return ResponseEntity.created(null).body(payload);
    }

    /**
     * List Analysis Types
     * @return
     */
    @GetMapping("/analyses/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<CvTypeDTO>> getAnalysisTypes(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        Integer pageSizeToUse = getPageSize(pageSize);
        PagedResult<CvTypeDTO> result = analysisService.getAnalysisTypes(page, pageSizeToUse);
        BrApiMasterListPayload<CvTypeDTO> payload = this.getMasterListPayload(result);
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
       BrApiMasterPayload<DatasetDTO> payload = this.getMasterPayload(result);
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
        PagedResult<DatasetDTO> pagedResult = datasetService.getDatasets(
            page, pageSizeToUse, experimentId, datasetTypeId
        );

        BrApiMasterListPayload<DatasetDTO> payload = this.getMasterListPayload(pagedResult);
        return ResponseEntity.ok(payload);
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
        BrApiMasterPayload<DatasetDTO> payload = this.getMasterPayload(datasetDTO);
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
        BrApiMasterPayload<DatasetDTO> payload = this.getMasterPayload(datasetDTO);
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
    public ResponseEntity<BrApiMasterListPayload<CvTypeDTO>> getDatasetTypes(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        Integer pageSizeToUse = this.getPageSize(pageSize);

        PagedResult<CvTypeDTO> pagedResult = datasetService.getDatasetTypes(page, pageSizeToUse);
        
        BrApiMasterListPayload<CvTypeDTO> payload = this.getMasterListPayload(pagedResult);
        return ResponseEntity.ok(payload);
    }

    /**
     * Create Analysis Type
     * @return
     */
    @PostMapping("/datasets/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<CvTypeDTO>> createDatasetType(
        @RequestBody @Validated(CvTypeDTO.Create.class) final CvTypeDTO datasetTypeRequest,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();
        CvTypeDTO result = datasetService.createDatasetType(
            datasetTypeRequest.getTypeName(),
            datasetTypeRequest.getTypeDescription(),
            user
        );
        BrApiMasterPayload<CvTypeDTO> payload = this.getMasterPayload(result);
        return ResponseEntity.created(null).body(payload);
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

        BrApiMasterListPayload<MapsetDTO> payload = this.getMasterListPayload(pagedResult);
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
        BrApiMasterPayload<MapsetDTO> payload = this.getMasterPayload(mapsetDTO);
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
        BrApiMasterPayload<MapsetDTO> payload = this.getMasterPayload(result);
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
        BrApiMasterPayload<MapsetDTO> payload = this.getMasterPayload(result);
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
    /**
     * Create Mapset Type
     * @param mapsetTypeRequest
     * @param bindingResult
     * @return
     * @throws Exception
     */
    @PostMapping("/mapsets/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<CvTypeDTO>> createMapsetType(
        @RequestBody @Validated(CvTypeDTO.Create.class) final CvTypeDTO mapsetTypeRequest,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();
        CvTypeDTO result = mapsetService.createMapsetType(
            mapsetTypeRequest.getTypeName(),
            mapsetTypeRequest.getTypeDescription(),
            user
        );

        BrApiMasterPayload<CvTypeDTO> payload = this.getMasterPayload(result);
        return ResponseEntity.created(null).body(payload);
    }

    /**
     * Get Mapset Types
     * @return
     */
    @GetMapping("/mapsets/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<CvTypeDTO>> getMapsetTypes(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        Integer pageSizeToUse = getPageSize(pageSize);
        PagedResult<CvTypeDTO> result = mapsetService.getMapsetTypes(page, pageSizeToUse);
        BrApiMasterListPayload<CvTypeDTO> payload = this.getMasterListPayload(result);
        return ResponseEntity.ok(payload);
    }

    //-------Organizations----------
    /**
     * Get Organizations
     */
    @GetMapping("/organizations")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<OrganizationDTO>> getOrganizations(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        Integer pageSizetoUse = getPageSize(pageSize);
        PagedResult<OrganizationDTO> result = organizationService.getOrganizations(page, pageSizetoUse);
        BrApiMasterListPayload<OrganizationDTO> payload = this.getMasterListPayload(result);
        return ResponseEntity.ok(payload);
    }

    /**
     * Get Organization by Id
     */
    @GetMapping("/organizations/{organizationId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<OrganizationDTO>> getOrganizationById(
        @PathVariable Integer organizationId
    ) throws Exception {
        OrganizationDTO organizationDTO = organizationService.getOrganization(organizationId);
        BrApiMasterPayload<OrganizationDTO> payload = this.getMasterPayload(organizationDTO);
        return ResponseEntity.ok(payload);
    }

    /**
     * Create Organization
     * @return
     */
    @PostMapping("/organizations")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<OrganizationDTO>> createOrganization(
        @RequestBody @Validated(OrganizationDTO.Create.class) final OrganizationDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user  = this.getCurrentUser();

        OrganizationDTO createdOrganization = organizationService.createOrganization(request, user);
        BrApiMasterPayload<OrganizationDTO> payload = this.getMasterPayload(createdOrganization);
        return ResponseEntity.created(null).body(payload);
    }

    /**
     * Patch organization
     */
    @PatchMapping("/organizations/{organizationId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<OrganizationDTO>> updateOrganization(
        @PathVariable Integer organizationId,
        @RequestBody @Validated final OrganizationDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();
        OrganizationDTO updatedOrganization = organizationService.updateOrganization(organizationId, request, user);
        BrApiMasterPayload<OrganizationDTO> payload = this.getMasterPayload(updatedOrganization);
        return ResponseEntity.ok(payload);
    }

    /**
     * Delete organization
     */
    @DeleteMapping("/organizations/{organizationId}")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteOrganiztion(
        @PathVariable Integer organizationId
    ) throws Exception {
        organizationService.deleteOrganization(organizationId);
        return ResponseEntity.noContent().build();
    }

    //--- Cv 

    @PostMapping("/cvs")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<CvDTO>> createCv(
        @RequestBody @Validated(CvDTO.Create.class) final CvDTO requestCvDTO,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        CvDTO createdCvDTO = cvService.createCv(requestCvDTO);
        BrApiMasterPayload<CvDTO> payload = this.getMasterPayload(createdCvDTO);
        return ResponseEntity.created(null).body(payload);
    }

    @PatchMapping("/cvs/{cvId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<CvDTO>> updateCv(
        @PathVariable Integer cvId,
        @RequestBody @Validated(CvDTO.Update.class) final CvDTO requestCvDTO,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        CvDTO updatedCvDTO = cvService.updateCv(cvId, requestCvDTO);
        BrApiMasterPayload<CvDTO> payload = this.getMasterPayload(updatedCvDTO);
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/cvs")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<CvDTO>> getCvs(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize,
        @RequestParam(required=false) String cvGroupName,
        @RequestParam(required=false) String cvGroupType
    ) throws Exception {
        PagedResult<CvDTO> pagedResult = cvService.getCvs(page, pageSize, cvGroupName, cvGroupType);
        BrApiMasterListPayload<CvDTO> payload = this.getMasterListPayload(pagedResult);
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/cvs/{cvId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<CvDTO>> getCv(
        @PathVariable Integer cvId
    ) throws Exception {
        CvDTO cvDTO = cvService.getCv(cvId);
        BrApiMasterPayload<CvDTO> payload = this.getMasterPayload(cvDTO);
        return ResponseEntity.ok(payload);

    }

    @GetMapping("/cvs/properties")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<CvPropertyDTO>> getCvProperties(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        PagedResult<CvPropertyDTO> pagedResult = cvService.getCvProperties(page, pageSize);
        BrApiMasterListPayload<CvPropertyDTO> payload = this.getMasterListPayload(pagedResult);
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/cvs/properties")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<CvPropertyDTO>> createCvProperty(
        @RequestBody @Validated(CvPropertyDTO.Create.class) final CvPropertyDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        CvPropertyDTO cvPropertyDTO = cvService.addCvProperty(request);
        BrApiMasterPayload<CvPropertyDTO> payload = this.getMasterPayload(cvPropertyDTO);
        return ResponseEntity.created(null).body(payload);

    }

    @DeleteMapping("/cvs/{cvId}")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteCv(
        @PathVariable Integer cvId
    ) throws Exception {
        cvService.deleteCv(cvId);
        return ResponseEntity.noContent().build();
    } 

    // --- Platforms
    @PostMapping("/platforms")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<PlatformDTO>> addPlatform(
        @RequestBody @Validated(PlatformDTO.Create.class) final PlatformDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String user = this.getCurrentUser();

        PlatformDTO platformDTO = platformService.createPlatform(request, user);
        BrApiMasterPayload<PlatformDTO> payload = this.getMasterPayload(platformDTO);
        return ResponseEntity.created(null).body(payload);
    }

    @GetMapping("/platforms")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<PlatformDTO>> getPlatforms(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize,
        @RequestParam(required=false) Integer platformTypeId
    ) throws Exception {
        PagedResult<PlatformDTO> results = platformService.getPlatforms(page, pageSize, platformTypeId);
        BrApiMasterListPayload<PlatformDTO> payload = this.getMasterListPayload(results);
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/platforms/{platformId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<PlatformDTO>> getPlatform(
        @PathVariable Integer platformId
    ) throws Exception {
        PlatformDTO platformDTO = platformService.getPlatform(platformId);
        BrApiMasterPayload<PlatformDTO> payload = this.getMasterPayload(platformDTO);
        return ResponseEntity.ok(payload);
    }

    @PatchMapping("/platforms/{platformId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<PlatformDTO>> updatePlatform(
        @PathVariable Integer platformId,
        @RequestBody @Validated(PlatformDTO.Update.class) final PlatformDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String updatedBy = this.getCurrentUser();

        PlatformDTO platformDTO = platformService.updatePlatform(platformId, request, updatedBy);
        BrApiMasterPayload<PlatformDTO> payload = this.getMasterPayload(platformDTO);
        return ResponseEntity.ok(payload);
    }

    @DeleteMapping("/platforms/{platformId}")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ResponseEntity deletePlatform(
        @PathVariable Integer platformId
    ) throws Exception {
        platformService.deletePlatform(platformId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/platforms/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<CvTypeDTO>> createPlatformType(
        @RequestBody @Validated(CvTypeDTO.Create.class) final CvTypeDTO request 
    ) throws Exception {
        CvTypeDTO cvTypeDTO = platformService.createPlatformType(request);
        BrApiMasterPayload<CvTypeDTO> payload = this.getMasterPayload(cvTypeDTO);
        return ResponseEntity.created(null).body(payload);
    }

    @GetMapping("/platforms/types")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<CvTypeDTO>> getPlatformTypes(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        PagedResult<CvTypeDTO> results = platformService.getPlatformTypes(page, pageSize);
        BrApiMasterListPayload<CvTypeDTO> payload = this.getMasterListPayload(results);
        return ResponseEntity.ok(payload);
    }

    // -- References
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
        BrApiMasterListPayload<ReferenceDTO> payload = this.getMasterListPayload(pagedResult);
        return ResponseEntity.ok(payload);  
    }

    /**
     * Create reference
     * @return
     */
    @PostMapping("/references")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<ReferenceDTO>> createReference(
        @RequestBody @Validated(ReferenceDTO.Create.class) final ReferenceDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String createdBy = this.getCurrentUser();
        ReferenceDTO referenceDTO = referenceService.createReference(request, createdBy);
        BrApiMasterPayload<ReferenceDTO> payload =  this.getMasterPayload(referenceDTO);
        return ResponseEntity.created(null).body(payload);
    }

    /**
     * Get Reference by Id
     * @return
     */
    @GetMapping("/references/{referenceId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<ReferenceDTO>> getReference(
        @PathVariable Integer referenceId
    ) throws Exception {
        ReferenceDTO referenceDTO = referenceService.getReference(referenceId);
        BrApiMasterPayload<ReferenceDTO> payload = this.getMasterPayload(referenceDTO);
        return ResponseEntity.ok(payload);
    }

    /**
     * Update
     */
    @PatchMapping("/references/{referenceId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<ReferenceDTO>> updateReference(
        @PathVariable Integer referenceId,
        @RequestBody @Validated(ReferenceDTO.Update.class) final ReferenceDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String updatedBy = this.getCurrentUser();
        ReferenceDTO referenceDTO = referenceService.updateReference(referenceId, request, updatedBy);
        BrApiMasterPayload<ReferenceDTO> payload = this.getMasterPayload(referenceDTO);
        return ResponseEntity.ok(payload);
    }

    /**
     * Delete
     * @return
     */
    @DeleteMapping("/references/{referenceId}")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteReference(
        @PathVariable Integer referenceId
    ) throws Exception {
        referenceService.deleteReference(referenceId);
        return ResponseEntity.noContent().build();
    }

    // -- Protocols

    /**
     * Get Protocol
     * @return
     */
    @GetMapping("/protocols")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<ProtocolDTO>> getProtocols(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize,
        @RequestParam(required = false) Integer platformId
    ) throws Exception {
        pageSize = this.getPageSize(pageSize);
        PagedResult<ProtocolDTO> pagedResult = protocolService.getProtocols(
            pageSize, page, platformId);
        BrApiMasterListPayload<ProtocolDTO> payload = this.getMasterListPayload(pagedResult);
        return ResponseEntity.ok(payload);
    }

    /**
     * Create protocol
     * @return
     */
    @PostMapping("/protocols")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<ProtocolDTO>> createProtocol(
        @RequestBody @Validated(ReferenceDTO.Create.class) final ProtocolDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        ProtocolDTO protocolDTO = protocolService.createProtocol(request);
        BrApiMasterPayload<ProtocolDTO> payload =  this.getMasterPayload(protocolDTO);
        return ResponseEntity.created(null).body(payload);
    }


    //---- Marker Group

    @PostMapping("/markergroups")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<MarkerGroupDTO>> createMarkerGroup(
        @RequestBody @Validated(MarkerGroupDTO.Create.class) final MarkerGroupDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String creator = this.getCurrentUser();
        MarkerGroupDTO markerGroupDTO = markerGroupService.createMarkerGroup(request, creator);
        BrApiMasterPayload<MarkerGroupDTO> payload = this.getMasterPayload(markerGroupDTO);
        return ResponseEntity.created(null).body(payload);
    }

    @GetMapping("/markergroups")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<MarkerGroupDTO>> getMarkerGroups(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        Integer pageSizeToUse = this.getPageSize(pageSize);
        PagedResult<MarkerGroupDTO> results = markerGroupService.getMarkerGroups(page, pageSizeToUse);
        BrApiMasterListPayload<MarkerGroupDTO> payload = this.getMasterListPayload(results);
        return ResponseEntity.ok(payload);

    }

    @GetMapping("/markergroups/{markerGroupId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<MarkerGroupDTO>> getMarkerGroupById(
        @PathVariable Integer markerGroupId
    ) throws Exception {
        MarkerGroupDTO markerGroupDTO = markerGroupService.getMarkerGroup(markerGroupId);
        BrApiMasterPayload<MarkerGroupDTO> payload = this.getMasterPayload(markerGroupDTO);
        return ResponseEntity.ok(payload);
    }

    @PatchMapping("/markergroups/{markerGroupId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<MarkerGroupDTO>> updateMarkerGroup(
        @PathVariable Integer markerGroupId,
        @RequestBody final MarkerGroupDTO request,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String updatedBy = this.getCurrentUser();
        MarkerGroupDTO markerGroupDTO = markerGroupService.updateMarkerGroup(markerGroupId, request, updatedBy);
        BrApiMasterPayload<MarkerGroupDTO> payload = this.getMasterPayload(markerGroupDTO);
        return ResponseEntity.ok(payload);
    }

    @DeleteMapping("/markergroups/{markerGroupId}")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteMarkerGroup(
        @PathVariable Integer markerGroupId
    ) throws Exception {
        markerGroupService.deleteMarkerGroup(markerGroupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/markergroups/{markerGroupId}/markerscollection")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<MarkerDTO>> mapMarkers(
        @PathVariable Integer markerGroupId,
        @RequestBody final List<MarkerDTO> markers,
        BindingResult bindingResult
    ) throws Exception {
        this.checkBindingErrors(bindingResult);
        String editedBy = this.getCurrentUser();
        PagedResult<MarkerDTO> results = markerGroupService.mapMarkers(markerGroupId, markers, editedBy);
        BrApiMasterListPayload<MarkerDTO> payload = this.getMasterListPayload(results);
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/markergroups/{markerGroupId}/markers")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<MarkerDTO>> getMarkerGroupMarkers(
        @PathVariable Integer markerGroupId,
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        PagedResult<MarkerDTO> results = markerGroupService.getMarkerGroupMarkers(markerGroupId, page, pageSize);
        BrApiMasterListPayload<MarkerDTO> payload = this.getMasterListPayload(results);
        return ResponseEntity.ok(payload);
    }

    //-- cv group
    @GetMapping("/cvs/groups")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<CvGroupDTO>> getCvGroups(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize
    ) throws Exception {
        PagedResult<CvGroupDTO> results = cvService.getCvGroups(page, pageSize);
        BrApiMasterListPayload<CvGroupDTO> payload = this.getMasterListPayload(results);
        return ResponseEntity.ok(payload);
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
            throw new ValidationException("Bad Request. "
                + String.join(", ", info.toArray(new String[info.size()])));
        } 
    }

    private <T> BrApiMasterPayload<T> getMasterPayload(T dtoObject) {
        BrApiMasterPayload<T> masterPayload = new BrApiMasterPayload<>();
        masterPayload.setMetadata(null);
        masterPayload.setResult(dtoObject);
        return masterPayload;
    }

    private <T> BrApiMasterListPayload<T> getMasterListPayload(PagedResult<T> objectList) {
        return new BrApiMasterListPayload<T>(
            objectList.getResult(),
            objectList.getCurrentPageSize(),
            objectList.getCurrentPageNum()
        );
    }
}