package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobidomain.services.brapi.*;
import org.gobiiproject.gobidomain.services.brapi.MapsetService;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiResult;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.ErrorPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseMapCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.brapi.*;
import org.gobiiproject.gobiimodel.dto.noaudit.*;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.automation.RestResourceLimits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Scope(value = "request")
@Controller
@EnableAsync
@RequestMapping(GobiiControllerType.SERVICE_PATH_BRAPI_V2)
@CrossOrigin
@Api
@SuppressWarnings("unused")
public class BRAPIIControllerV2 {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BRAPIIControllerV2.class);

    private final Integer brapiDefaultPageSize = 1000; //TODO: remove 

    @Autowired
    private GenotypeCallsService genotypeCallsService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private ConfigSettingsService configSettingsService; //TODO: remove

    @Autowired
    private MapsetService mapsetService;

    @Autowired
    private SamplesService samplesBrapiService;

    @Autowired
    private VariantSetsService variantSetsService;

    @Autowired
    private CallSetService callSetService;

    @Autowired
    private VariantService variantService;

    @Autowired
    private MarkerPositionsService markerPositionsService;

    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT); //TODO: remove


    private class CallSetResponse extends BrApiMasterPayload<CallSetDTO>{}
    private class CallSetListResponse extends BrApiMasterPayload<BrApiResult<CallSetDTO>>{}
    private class GenotypeCallsResponse extends BrApiMasterPayload<GenotypeCallsDTO>{}
    private class GenotypeCallsListResponse extends BrApiMasterPayload<BrApiResult<GenotypeCallsDTO>>{}
    private class VariantResponse extends BrApiMasterPayload<VariantDTO>{}
    private class VariantListResponse extends BrApiMasterPayload<BrApiResult<VariantDTO>>{}
    private class VariantSetResponse extends BrApiMasterPayload<VariantSetDTO>{}
    private class VariantSetListResponse extends BrApiMasterPayload<BrApiResult<VariantSetDTO>>{}


    /**
     * List all BrApi compliant web services in GDM system
     *
     * @param request - request object
     * @return Json object with list of brapi calls in GDM
     * @throws Exception
     */
    @RequestMapping(value = "/serverinfo",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation(
            value = "Get ServerInfo",
            notes = "List of all calls",
            tags = {"ServerInfo"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="ServerInfo"),
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful",
                            response = BrapiResponseMapCalls.class
                    )
            }
    )
    @ResponseBody
    public ResponseEntity<String> getCalls(
            HttpServletRequest request) throws Exception {


        return ResponseEntity.ok("");

    }



    /**
     * Lists the dnaruns by page size and page token
     * @param page - page number to be fetched for callsets
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size
     * @param variantSetDbId - Variant Set Db Id
     * @param callSetsFilter - CallsetBrapiDTO model to map the filters
     * @return Brapi response with list of dna runs/call sets
     */
    @ApiOperation(
            value = "List CallSets",
            notes = "List of all Callsets.",
            tags = {"CallSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Callsets")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of CallSets",
                            response = CallSetListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required=true,
                paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/callsets", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<CallSetDTO>> getCallSets(
            @ApiParam(value = "Used to request a specific page of data to be returned. " +
                    "The page indexing starts at 0 (the first page is 'page'= 0). " +
                    "Default is 0")
            @RequestParam(value  = "page", required = false, defaultValue = BrapiDefaults.pageNum) Integer page,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId,
            CallSetDTO callSetsFilter
    ) {
        try {

            PagedResult<CallSetDTO> callSets = callSetService.getCallSets(
                    pageSize, page,
                    variantSetDbId, callSetsFilter);

            BrApiMasterListPayload<CallSetDTO> payload = new BrApiMasterListPayload<>(
                    callSets.getResult(),
                    callSets.getCurrentPageSize(),
                    callSets.getCurrentPageNum());

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage());
        }
    }

    /**
     * Endpoint for getting a specific callset with a given callSetDbId
     *
     * @param callSetDbId ID of the requested callsets
     * @return ResponseEntity with http status code specifying if retrieval of the callset is successful.
     *
     * Response body contains the requested callset information
     */
    @ApiOperation(
            value = "Get CallSet by callsetId",
            notes = "Retrieves the Callset entity having the specified ID",
            tags = {"CallSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Callsets : callSetDbId")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of CallSets", response = CallSetResponse.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
            paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/callsets/{callSetDbId:[\\d]+}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterPayload<CallSetDTO>> getCallSetsByCallSetDbId(
            @ApiParam(value = "ID of the Callset to be extracted", required = true)
            @PathVariable("callSetDbId") Integer callSetDbId) {

        try {

            CallSetDTO callSet = callSetService.getCallSetById(callSetDbId);

            BrApiMasterPayload<CallSetDTO> payload = new BrApiMasterPayload<>(callSet);

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal server error");
        }

    }


    /**
     * Returns the list of genotypes calls in a given DNA run id.
     * It fetches calls in all the datasets where the dnarun_id is present.
     * The calls is paged.
     *
     * @param callSetDbId - DNA run Id.
     * @param pageSize - Size of the page to fetched.
     * @param pageToken - Page token to fetch the page. User will get the pageToken
     *                       from the nextPageToken parameter in the previous response.
     *
     * @return BrApi Response entity with list of genotypes calls for given dnarun id.
     * TODO: Add page number parameter to comply BrApi standards.
     */
    @ApiOperation(
            value = "List genotype calls",
            notes = "List of all the genotype calls in a given Dna run identified by Dna run Id",
            tags = {"CallSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Genotype calls",
                            response = GenotypeCallsResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/callsets/{callSetDbId}/calls", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterPayload<List<GenotypeCallsDTO>>> getCallsByCallset(
            @ApiParam(value = "Id for dna run to be fetched")
            @PathVariable(value="callSetDbId") Integer callSetDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            HttpServletRequest request) throws Exception {


        try {


            PagedResult<GenotypeCallsDTO> genotypeCallsList = genotypeCallsService.getGenotypeCallsByCallSetId(
                    callSetDbId,
                    pageSize, pageToken);

            BrApiMasterPayload<List<GenotypeCallsDTO>> payload = new BrApiMasterPayload<>(
                    genotypeCallsList.getResult());

            if(genotypeCallsList.getNextPageToken() != null) {
                payload.getMetadata().getPagination().setNextPageToken(genotypeCallsList.getNextPageToken());
            }


            return ResponseEntity.ok(payload);
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage());
        }
    }

    /**
     * Lists the variants by page size and page token
     *
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size
     * @return Brapi response with list of variants
     */
    @ApiOperation(
            value = "List all variants",
            notes = "List of all Variants",
            tags = {"Variants"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Variants")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Variants",
                            response = VariantListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required=true,
            paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variants", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<VariantDTO>> getVariants(
            @ApiParam(value = "Page Token to fetch a page. " +
                    "Value is $metadata.pagination.nextPageToken form previous page.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page")
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            @ApiParam(value = "ID of the variant to be extracted")
            @RequestParam(value = "variantDbId", required = false) Integer variantDbId,
            @ApiParam(value = "ID of the variantSet to be extracted")
            @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId
    ) {
        try {

            PagedResult<VariantDTO> pagedResult = variantService.getVariants(pageSize, pageToken,
                    variantDbId, variantSetDbId);

            BrApiMasterListPayload<VariantDTO>  payload = new BrApiMasterListPayload<>(
                    pagedResult.getResult(),
                    pagedResult.getCurrentPageSize(),
                    pagedResult.getNextPageToken());

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }
    }

    /**
     * Endpoint for getting a specific marker with a given markerDbId
     *
     * @param variantDbId ID of the requested marker
     * @return ResponseEntity with http status code specifying if retrieval of the marker is successful.
     * Response body contains the requested marker information
     */
    @ApiOperation(
            value = "Get a variant by variantDbId",
            notes = "Retrieves the Variant entity having the specified ID",
            tags = {"Variants"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Variants: variantDbId")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Variant by Id",
                            response = VariantResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/variants/{variantDbId:[\\d]+}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterPayload<VariantDTO>> getVariantsByVariantDbId(
            @ApiParam(value = "ID of the Variant to be extracted", required = true)
            @PathVariable("variantDbId") Integer variantDbId) {

        try {

            VariantDTO variantDTO = variantService.getVariantByVariantDbId(variantDbId);

            BrApiMasterPayload<VariantDTO> payload = new BrApiMasterPayload<>(variantDTO);

            return ResponseEntity.ok(payload);

        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }
    }

    @ApiOperation(value="List samples", hidden = true)
    @RequestMapping(value="/samples", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<SamplesDTO>> getMaps(
            @RequestParam(value="sampleDbId", required=false) Integer sampleDbId,
            @RequestParam(value="observationUnitDbId", required=false) String observationUnitDbId,
            @RequestParam(value="germplasmDbId", required=false) Integer germplasmDbId,
            @RequestParam(value = "page", required = false, defaultValue = BrapiDefaults.pageNum) Integer page,
            @RequestParam(value = "pageSize", required = false,
                    defaultValue = BrapiDefaults.pageSize) Integer pageSize) {

        try {


            PagedResult<SamplesDTO> samples = samplesBrapiService.getSamples(
                    pageSize, page,
                    sampleDbId, germplasmDbId,
                    observationUnitDbId);

            BrApiMasterListPayload<SamplesDTO> payload = new BrApiMasterListPayload<>(
                    samples.getResult(),
                    samples.getCurrentPageSize(),
                    samples.getCurrentPageNum());

            return ResponseEntity.ok(payload);


        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }
    }


    @ApiOperation(
            value = "List Maps",
            notes = "List Genome maps in the database",
            tags = {"Maps"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Maps")
                    })
            }
            ,
            hidden = true
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/maps", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<MapsetDTO>> getMaps(
            @RequestParam(value = "page", required = false, defaultValue = BrapiDefaults.pageNum) Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            @RequestParam(value = "studyDbId", required = false) Integer studyDbId,
            @RequestParam(value = "type", required = false) String mapType) throws GobiiException {

        try {

            PagedResult<MapsetDTO> pagedResult = mapsetService.getMapSets(pageSize, page, studyDbId);

            BrApiMasterListPayload<MapsetDTO> payload = new BrApiMasterListPayload<>(pagedResult.getResult(),
                    pagedResult.getCurrentPageSize(),
                    pagedResult.getCurrentPageNum());

            return ResponseEntity.ok(payload);
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal server error"
            );
        }

    }

    @ApiOperation(
            value = "Get Map by mapId",
            notes = "List Genome maps in the database",
            tags = {"Maps"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Maps : mapId")
                    })
            }
            ,
            hidden = true

    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/maps/{mapId}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterPayload<MapsetDTO>> getMapByMapId(
            @RequestParam(value = "page", required = false, defaultValue = BrapiDefaults.pageNum) Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            @PathVariable(value = "mapId") Integer mapId) throws GobiiException {

        try {

            MapsetDTO mapset = mapsetService.getMapSetById(mapId);
            BrApiMasterPayload<MapsetDTO> payload = new BrApiMasterPayload<>(mapset, pageSize, page);
            return ResponseEntity.ok(payload);

        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }

    }

    @ApiOperation(
            value = "Get Markers positions",
            notes = "List Genome maps in the database",
            tags = {"Maps"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Markers")
                    })
            }
            ,
            hidden = true
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/markerpositions", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<MarkerPositions>> getMarkersByMapId(
            @RequestParam(value = "page", required = false, defaultValue = BrapiDefaults.pageNum) Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            @RequestParam(value = "minPosition", required = false) BigDecimal minPosition,
            @RequestParam(value = "maxPosition", required = false) BigDecimal maxPosition,
            MarkerPositions markerPositionsFilter) throws GobiiException {

        try {

            PagedResult<MarkerPositions> pagedResult = markerPositionsService.getMarkerPositions(pageSize, page,
                    markerPositionsFilter, minPosition, maxPosition);

            BrApiMasterListPayload<MarkerPositions> payload = new BrApiMasterListPayload<>(
                    pagedResult.getResult(),
                    pagedResult.getCurrentPageSize(),
                    pagedResult.getCurrentPageNum());

            return ResponseEntity.ok(payload);

        }
        catch(GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }

    }

    /**
     * Returns the list of genotypes calls in a given Marker id.
     * It fetches calls in all the datasets where the marker_id is present.
     * The calls is paged.
     *
     * @param variantDbId - Marker run Id.
     * @param pageSize - Size of the page to fetched.
     * @param pageToken - Page token to fetch the page. User will get the pageToken
     *                       from the nextPageToken parameter in the previous response.
     *
     * @return BrApi Response entity with list of genotypes calls for given dnarun id.
     * TODO: Add page number parameter to comply BrApi standards.
     */
    @ApiOperation(
            value = "List Genotype Calls",
            notes = "List of all the genotype calls in a given Marker identified by Marker Id",
            tags = {"Variants"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Genotype Calls",
                            response = GenotypeCallsResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variants/{variantDbId}/calls", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterPayload<List<GenotypeCallsDTO>>> getCallsByVariant(
            @ApiParam(value = "Id for marker to be fetched")
            @PathVariable(value="variantDbId") Integer variantDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) throws Exception {
        try {

            PagedResult<GenotypeCallsDTO> genotypeCallsList = genotypeCallsService.getGenotypeCallsByVariantDbId(
                    variantDbId,
                    pageSize, pageToken);

            BrApiMasterPayload<List<GenotypeCallsDTO>> payload = new BrApiMasterPayload<>(
                    genotypeCallsList.getResult());

            if(genotypeCallsList.getNextPageToken() != null) {
                payload.getMetadata().getPagination().setNextPageToken(genotypeCallsList.getNextPageToken());
            }

            return ResponseEntity.ok(payload);
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage());
        }
    }

    /**
     * Lists the variantsets by page size and page token
     *
     * @param pageNum - Page number to be fetched. 0 indexed.
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size
     * @return Brapi response with list of variantsets
     */
    @ApiOperation(
            value = "List VariantSets",
            notes = "List of all Variantsets",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="VariantSets")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success",
                            response = VariantSetListResponse.class),
                    @ApiResponse(code = 500, message = "Internal Server Error",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 400, message = "Bad Request",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 401, message = "Unauthorized",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 403, message = "Forbidden",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 500, message = "Internal Server Error",
                            response = ErrorPayload.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<VariantSetDTO>> getVariantSets(
            @ApiParam(value = "Id of the VariantSet to be fetched. Also, corresponds to dataset Id")
            @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId,
            @ApiParam(value = "Study Id for which list of Variantsets need to be fetched. study " +
                    "corresponds to experiment")
            @RequestParam(value = "studyDbId", required = false) Integer studyDbId,
            @ApiParam(value = "Study Name for which list of Variantsets need to be fetched")
            @RequestParam(value = "studyName", required = false) String studyName,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            @ApiParam(value = "Page number to be fetched")
            @RequestParam(value = "page", required = false, defaultValue = BrapiDefaults.pageNum) Integer pageNum
    ) {
        try {

            PagedResult<VariantSetDTO> pagedResult = variantSetsService.getVariantSets(pageSize, pageNum,
                    variantSetDbId, null,
                    studyDbId, studyName);

            BrApiMasterListPayload<VariantSetDTO> payload = new BrApiMasterListPayload<>(
                    pagedResult.getResult(),
                    pagedResult.getCurrentPageSize(),
                    pagedResult.getCurrentPageNum());

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }
    }

    /**
     * Endpoint for getting a specific variantSet with a given variantSetDbId
     *
     * @param variantSetDbId ID of the requested variantSet
     * @return ResponseEntity with http status code specifying if retrieval of the variantSet is successful.
     * Response body contains the requested variantSet information
     */
    @ApiOperation(
            value = "Get VariantSet by variantSetDbId",
            notes = "Retrieves the VariantSet entity having the specified ID",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="VariantSets : variantSetDbId")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of VariantSet By ID",
                            response = VariantSetResponse.class
                    ),
                    @ApiResponse(code = 500, message = "Internal Server Error",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 400, message = "Bad Request",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 401, message = "Unauthorized",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 403, message = "Forbidden",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 404, message = "Resource Not Found",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 500, message = "Internal Server Error",
                            response = ErrorPayload.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
            paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterPayload<VariantSetDTO>> getVariantSetById(
            @ApiParam(value = "ID of the VariantSet to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId) {

        try {

            VariantSetDTO variantSetDTO = variantSetsService.getVariantSetById(variantSetDbId);

            BrApiMasterPayload<VariantSetDTO> payload = new BrApiMasterPayload<>(variantSetDTO);

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }
    }

    /**
     * Lists the variants for a given VariantSetDbId by page size and page token
     *
     * @param variantSetDbId - Integer ID of the VariantSet to be fetched
     * @param pageToken - String page token
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed page size,
     *                 then the response will have maximum page size
     * @return Brapi response with list of CallSets
     */
    @ApiOperation(
            value = "List Variants by VariantSetDbId",
            notes = "List of all the Variants in a specific VariantSet",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Variants")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Variants by VariantSetId",
                            response = VariantListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}/variants", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<VariantDTO>> getVariantsByVariantSetDbId(
            @ApiParam(value = "ID of the VariantSet of the Variants to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            @RequestParam(value = "variantDbId", required = false) Integer variantDbId
    ){
        VariantSetDTO variantSet = variantSetsService.getVariantSetById(variantSetDbId);
        return getVariants(pageToken,pageSize, variantDbId, variantSet.getVariantSetDbId());
    }

    /**
     * Lists the callsets for a given VariantSetDbId by page size and page token
     *
     * @param variantSetDbId - Integer ID of the VariantSet to be fetched
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed page size, then the response will have maximum page size
     * @return Brapi response with list of CallSets
     */
    @ApiOperation(
            value = "List Callsets by VariantSetDbId",
            notes = "List of all the CallSets in a specific VariantSet",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Callsets")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of CallSets by VariantSetId",
                            response = CallSetListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}/callsets", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<CallSetDTO>> getCallSetsByVariantSetDbId(
            @ApiParam(value = "ID of the VariantSet of the CallSets to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
            @ApiParam(value = "Page number", required = false)
            @RequestParam(value = "page", required = false, defaultValue = BrapiDefaults.pageNum) Integer page,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            CallSetDTO callSetsFilter
    ) {

        try {

            VariantSetDTO variantSet = variantSetsService.getVariantSetById(variantSetDbId);
            return getCallSets(pageSize, page, variantSet.getVariantSetDbId(), callSetsFilter);
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }
    }



    @ApiOperation(
            value = "Creates a searchResultDbId for Genotype Calls search",
            notes = "Creates ",
            tags = {"GenotypeCalls"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Search")
                    })
            },
            hidden = true
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/search/calls", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<BrApiMasterPayload<SearchResultDTO>> searchGenotypeCalls(
            @Valid  @RequestBody GenotypeCallsSearchQueryDTO genotypeCallsSearchQuery,
            HttpServletRequest request
    ) {

        ObjectMapper objectMapper  = new ObjectMapper();

        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            String genotypesSearchQueryJson = objectMapper.writeValueAsString(genotypeCallsSearchQuery);

            if (!StringUtils.isEmpty(genotypesSearchQueryJson)) {

                SearchResultDTO searchResultDTO = searchService.createSearchQueryResource(
                        cropType,
                        genotypesSearchQueryJson);

                BrApiMasterPayload<SearchResultDTO> payload = new BrApiMasterPayload<>(searchResultDTO);

                return  ResponseEntity.status(HttpStatus.CREATED).body(payload);

            }
            else {
                throw new GobiiException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Missing Request body"
                );
            }

        }
        catch (GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Internal Server Error " + e.getMessage()
            );

        }
    }

    @ApiOperation(
            value = "List Genotype Calls",
            notes = "List of all the genotype calls in a given Variantset",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Genotype Calls by VariantSetId",
                            response = GenotypeCallsListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(
            value="/variantsets/{variantSetDbId}/calls",
            method=RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<GenotypeCallsDTO>> getCallsByVariantSetDbId(
            @ApiParam(value = "ID of the VariantSet of the CallSets to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSize) Integer pageSize,
            HttpServletRequest request
    ){

        try {

            PagedResult<GenotypeCallsDTO> pagedResult = new PagedResult<>();

            pagedResult = genotypeCallsService.getGenotypeCallsByVariantSetDbId(variantSetDbId, pageSize, pageToken);


            BrApiMasterListPayload<GenotypeCallsDTO> payload = new BrApiMasterListPayload<>(
                    pagedResult.getResult(),
                    pagedResult.getCurrentPageSize(),
                    pagedResult.getNextPageToken());


            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }
    }

    @ApiOperation(
            value = "Download Genotype Calls",
            notes = "Download of all the genotype calls in a given Variantset",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
            ,
            hidden = true
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(
            value="/variantsets/{variantSetDbId:[\\d]+}/calls/download",
            method=RequestMethod.GET,
            produces = "text/csv")
    public ResponseEntity<ResponseBodyEmitter> handleRbe(
            @PathVariable("variantSetDbId") Integer variantSetDbId,
            HttpServletRequest request

    ) {

        //Giving Response emitter to finish the download within 30 mins.
        //The request thread will be terminated once 30 mins is done.
        ResponseBodyEmitter emitter = new ResponseBodyEmitter((long)1800000);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {

           try {

               int pageNum = 0;

               String genotypesResult = genotypeCallsService.getGenotypeCallsAsString(
                       variantSetDbId, pageNum);

               while(genotypesResult != null &&
                       genotypesResult.length() != 0) {

                   emitter.send(genotypesResult, MediaType.TEXT_PLAIN);

                   pageNum++;

                   genotypesResult = genotypeCallsService.getGenotypeCallsAsString(
                               variantSetDbId, pageNum);

               }

               emitter.complete();

           } catch (Exception e) {

               e.printStackTrace();
               emitter.completeWithError(e);
               return;
           }

           emitter.complete();

        });

        return ResponseEntity.ok().header(
                "Content-Disposition", "attachment; filename=" + variantSetDbId.toString() + ".csv"
        ).contentType(MediaType.parseMediaType("text/csv")
        ).body(emitter);
    }

    /**
     * Brapi pages are 0 indexed and first page number is 0
     * @return First page number
     */
    public Integer getDefaultBrapiPage() {

        return 0;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        return "error";
    }

}// BRAPIController
