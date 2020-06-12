package org.gobiiproject.gobiiweb.controllers;

import io.swagger.annotations.*;
import org.gobiiproject.gobidomain.services.brapi.*;
import org.gobiiproject.gobidomain.services.brapi.MapsetService;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.*;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseMapCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.*;
import org.gobiiproject.gobiimodel.dto.noaudit.*;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
public class BrapiV2Controller {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BrapiV2Controller.class);

    @Autowired
    private GenotypeCallsService genotypeCallsService;

    @Autowired
    private StudiesService studiesService;

    @Autowired
    private SearchService searchService;

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

    private class CallSetResponse extends BrApiMasterPayload<CallSetDTO>{}
    private class CallSetListResponse extends BrApiMasterPayload<BrApiResult<CallSetDTO>>{}
    private class StudiesListResponse extends BrApiMasterPayload<BrApiResult<StudiesDTO>>{}
    private class SamplesListResponse extends BrApiMasterPayload<BrApiResult<SamplesDTO>>{}
    private class GenotypeCallsResponse extends BrApiMasterPayload<GenotypeCallsDTO>{}
    private class GenotypeCallsListResponse
        extends BrApiMasterPayload<BrApiResult<GenotypeCallsDTO>>{}
    private class VariantResponse extends BrApiMasterPayload<VariantDTO>{}
    private class VariantListResponse extends BrApiMasterPayload<BrApiResult<VariantDTO>>{}
    private class VariantSetResponse extends BrApiMasterPayload<VariantSetDTO>{}
    private class VariantSetListResponse extends BrApiMasterPayload<BrApiResult<VariantSetDTO>>{}
    private class MapsListResponse extends BrApiMasterListPayload<BrApiResult<MapsetDTO>>{}
    private class MapsResponse extends BrApiMasterPayload<MapsetDTO>{}
    private class SearchResultResponse extends BrApiMasterPayload<SearchResultDTO>{}


    /**
     * List all BrApi compliant web services in GDM system
     *
     * @param request - request object
     * @return Json object with list of brapi calls in GDM
     * @throws Exception
     */
    @RequestMapping(value = "/serverinfo", method = RequestMethod.GET,
        produces = "application/json")
    @ApiOperation(
        value = "Get ServerInfo", notes = "List of all calls",
        tags = {"ServerInfo"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="ServerInfo"),
            })
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = BrapiResponseMapCalls.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ResponseBody public ResponseEntity<BrApiMasterPayload> getServerInfo(
        HttpServletRequest request) {

        try {

            BrapiResponseMapCalls brapiResponseServerInfos = new BrapiResponseMapCalls(request);

            BrApiServerInfoPayload serverInfoPayload =
                new BrApiServerInfoPayload(brapiResponseServerInfos.getBrapi2ServerInfos());

            return ResponseEntity.ok(serverInfoPayload);

        }
        catch (Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage());
        }
    }

    //---------------------------------------------------------------------------------

    @ApiOperation(
        value = "List Studies", notes = "Lists Studies in GDM system",
        tags = {"Studies"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List Studies")
            })
        })
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "", response = StudiesListResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
        })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/studies", method=RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<StudiesDTO>> getStudies(
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize,
        @ApiParam(value = "Used to request a specific page of data to be returned. " +
            "The page indexing starts at 0 (the first page is 'page'= 0). Default is 0")
            @RequestParam(value  = "page", required = false,
                defaultValue = BrapiDefaults.pageNum) Integer page,
        @ApiParam(value = "Filter by Project Id")
            @RequestParam(value = "projectId", required = false) Integer projectId
    ) {
        try {

            PagedResult<StudiesDTO> studies = studiesService.getStudies(pageSize, page, projectId);

            BrApiMasterListPayload<StudiesDTO> payload =
                new BrApiMasterListPayload<>(
                    studies.getResult(), studies.getCurrentPageSize(), studies.getCurrentPageNum());

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage());
        }
    }

    //---------------------------------------------------------------------------------

    /**
     * Lists the dnaruns by page size and page token
     * @param page - page number to be fetched for callsets
     * @param pageSize - Page size set by the user.
     *                 If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size
     * @param variantSetDbId - Variant Set Db Id
     * @param callSetsFilter - CallsetBrapiDTO model to map the filters
     * @return Brapi response with list of dna runs/call sets
     */
    @ApiOperation(value = "List CallSets", notes = "Lists CallSets in GDM System.",
        tags = {"CallSets"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List CallSets")
            })
        })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = CallSetListResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/callsets", method=RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<CallSetDTO>> getCallSets(
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize,
        @ApiParam(value = "Used to request a specific page of data to be returned. " +
                "The page indexing starts at 0 (the first page is 'page'= 0). Default is 0")
            @RequestParam(value  = "page", required = false,
                defaultValue = BrapiDefaults.pageNum) Integer page,
        @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId,
        CallSetDTO callSetsFilter
    ) {
        try {

            PagedResult<CallSetDTO> callSets =
                callSetService.getCallSets(pageSize, page,
                    variantSetDbId, callSetsFilter);

            BrApiMasterListPayload<CallSetDTO> payload =
                new BrApiMasterListPayload<>(
                    callSets.getResult(), callSets.getCurrentPageSize(),
                    callSets.getCurrentPageNum());

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage());
        }
    }

    /**
     * Endpoint for getting a specific callset with a given callSetDbId
     *
     * @param callSetDbId ID of the requested callsets
     * @return ResponseEntity with http status code
     * specifying if retrieval of the callset is successful.
     *
     * Response body contains the requested callset information
     */
    @ApiOperation(
        value = "Get CallSet by callSetDbId",
        notes = "Retrieves the CallSet with given callSetDbId",
        tags = {"CallSets"},
        extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="Get CallSet By Id")
            })
        })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = CallSetResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token", required = true,
            paramType = "header", dataType = "string"),
    })
    @RequestMapping(
        value="/callsets/{callSetDbId:[\\d]+}", method=RequestMethod.GET,
        produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterPayload<CallSetDTO>> getCallSetsByCallSetDbId(
        @ApiParam(value = "ID of the Callset to be extracted", required = true)
            @PathVariable("callSetDbId") Integer callSetDbId
    ) {
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

            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
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
     * @param pageToken - Page token to fetch the page.
     *                  User will get the pageToken from the nextPageToken
     *                  parameter in the previous response.
     *
     * @return BrApi Response entity with list
     * of genotypes calls for given dnarun id.
     */
    @ApiOperation(
        value = "List Genotypes by CallSet", notes = "List of all the genotype calls in a " +
                "CallSet with callSetDbId",
        tags = {"CallSets"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List Genotypes by CallSet")
            })
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = GenotypeCallsResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/callsets/{callSetDbId}/calls", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterPayload<List<GenotypeCallsDTO>>>
    getCallsByCallset(
        @ApiParam(value = "Id for dna run to be fetched")
            @PathVariable(value="callSetDbId") Integer callSetDbId,
        @ApiParam(value = "Page Token to fetch a page. " +
                "Value is $metadata.pagination.nextPageToken form previous page.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize) throws GobiiException {

        try {


            PagedResult<GenotypeCallsDTO> genotypeCallsList =
                genotypeCallsService.getGenotypeCallsByCallSetId(callSetDbId, pageSize, pageToken);

            BrApiMasterPayload<List<GenotypeCallsDTO>> payload =
                new BrApiMasterPayload<>(genotypeCallsList.getResult());

            if(genotypeCallsList.getNextPageToken() != null) {
                payload
                    .getMetadata()
                    .getPagination()
                    .setNextPageToken(genotypeCallsList.getNextPageToken());
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

    //---------------------------------------------------------------------------------

    /**
     * Lists the variants by page size and page token
     *
     * @param pageSize - Page size set by the user.
     *                 If page size is more than maximum allowed page size,
     *                 then the response will have maximum page size
     * @return Brapi response with list of variants
     */
    @ApiOperation(
            value = "List Variants",
            notes = "Lists Variants in GDM System.",
            tags = {"Variants"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(
                                    name="summary", value="List Variants")
                    })
            }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = VariantListResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variants", method=RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<VariantDTO>> getVariants(
        @ApiParam(value = "Size of the page")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize,
        @ApiParam(value = "Page Token to fetch a page. " +
            "Value is $metadata.pagination.nextPageToken form previous page.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
        @ApiParam(value = "ID of the variant to be extracted")
            @RequestParam(value = "variantDbId", required = false) Integer variantDbId,
        @ApiParam(value = "ID of the variantSet to be extracted")
            @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId
    ) {
        try {

            PagedResult<VariantDTO> pagedResult =
                variantService.getVariants(pageSize, pageToken, variantDbId, variantSetDbId);

            BrApiMasterListPayload<VariantDTO>  payload =
                new BrApiMasterListPayload<>(
                    pagedResult.getResult(), pagedResult.getCurrentPageSize(),
                    pagedResult.getNextPageToken());

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage()
            );
        }
    }

    /**
     * Endpoint for getting a specific marker with a given markerDbId
     *
     * @param variantDbId ID of the requested marker
     * @return ResponseEntity with http status code specifying
     * if retrieval of the marker is successful.
     * Response body contains the requested marker information
     */
    @ApiOperation(
        value = "Get Variant by variantDbId", notes = "Retrieves the Variant with variantDbId",
        tags = {"Variants"},
        extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="Get Variant by Id")
            })
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "", response = VariantResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
        }
    )
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required = true, paramType = "header", dataType = "string"
        )
    })
    @RequestMapping(
        value="/variants/{variantDbId:[\\d]+}", method=RequestMethod.GET,
        produces = "application/json"
    )
    public @ResponseBody ResponseEntity<BrApiMasterPayload<VariantDTO>> getVariantsByVariantDbId(
        @ApiParam(value = "ID of the Variant to be extracted", required = true)
            @PathVariable("variantDbId") Integer variantDbId
    ) {
        try {
            VariantDTO variantDTO = variantService.getVariantByVariantDbId(variantDbId);
            BrApiMasterPayload<VariantDTO> payload = new BrApiMasterPayload<>(variantDTO);
            return ResponseEntity.ok(payload);
        }
        catch (Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
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
     * @param pageToken - Page token to fetch the page. User will get the pageToken from
     *                  the nextPageToken parameter in the previous response.
     * @return BrApi Response entity with
     * list of genotypes calls for given dnarun id.
     */
    @ApiOperation(
        value = "List Genotypes by Variant", notes = "List of all the genotype calls " +
        "in the marker with variantDbId",
        tags = {"Variants"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="Get Genotypes by Variant")
            })
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "", response = GenotypeCallsResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
        }
    )
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string"
        )
    })
    @RequestMapping(value="/variants/{variantDbId}/calls", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterPayload<List<GenotypeCallsDTO>>>
    getCallsByVariant(
        @ApiParam(value = "Id for marker to be fetched")
            @PathVariable(value="variantDbId") Integer variantDbId,
        @ApiParam(value = "Page Token to fetch a page. " +
            "Value is $metadata.pagination.nextPageToken from previous page.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize
    ) throws GobiiException {

        try {

            PagedResult<GenotypeCallsDTO> genotypeCallsList =
                    genotypeCallsService.getGenotypeCallsByVariantDbId(
                            variantDbId, pageSize, pageToken);

            BrApiMasterPayload<List<GenotypeCallsDTO>> payload =
                    new BrApiMasterPayload<>(genotypeCallsList.getResult());

            if(genotypeCallsList.getNextPageToken() != null) {
                payload
                    .getMetadata()
                    .getPagination()
                    .setNextPageToken(genotypeCallsList.getNextPageToken());
            }

            return ResponseEntity.ok(payload);
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage());
        }
    }


    @ApiOperation(
        value = "List Samples", notes = "Lists Dna Samples in GDM System.",
        tags = {"Samples"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List Samples")
            })
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = VariantListResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header",
            dataType = "string")
    })
    @RequestMapping(value="/samples", method=RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<SamplesDTO>> getSamples(
        @ApiParam(value = "Id of the Sample to be fetched")
            @RequestParam(value="sampleDbId", required=false) Integer sampleDbId,
        @ApiParam(value = "Filter samples by observationUnitDbId.")
            @RequestParam(value="observationUnitDbId", required=false) String observationUnitDbId,
        @ApiParam(value = "Filter list of samples by germplasmDbId.")
            @RequestParam(value="germplasmDbId", required=false) Integer germplasmDbId,
        @ApiParam(value = "Used to request a specific page of data to be returned. " +
                "The page indexing starts at 0 (the first page is 'page'= 0). Default is 0")
            @RequestParam(value = "page", required = false,
                defaultValue = BrapiDefaults.pageNum) Integer page,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize) {
        try {
            PagedResult<SamplesDTO> samples =
                samplesBrapiService.getSamples(pageSize, page, sampleDbId,
                    germplasmDbId, observationUnitDbId);

            BrApiMasterListPayload<SamplesDTO> payload =
                new BrApiMasterListPayload<>(samples.getResult(), samples.getCurrentPageSize(),
                    samples.getCurrentPageNum());

            return ResponseEntity.ok(payload);

        }
        catch(Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Entity does not exist"
            );
        }
    }


    @ApiOperation(
        value = "List Genome Maps", notes = "Lists Genome maps in GDM system.",
        tags = {"Genome Maps"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List Genome Maps")
            })
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "", response = MapsListResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
        }
    )
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token", required = true,
            paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/maps", method=RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<MapsetDTO>> getMaps(
        @ApiParam(value = "Used to request a specific page of data to be returned. " +
                "The page indexing starts at 0 (the first page is 'page'= 0). Default is 0")
            @RequestParam(value = "page", required = false,
                defaultValue = BrapiDefaults.pageNum) Integer page,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize,
        @ApiParam(value = "Filter by studyDbId.")
            @RequestParam(value = "studyDbId", required = false) Integer studyDbId
    ) throws GobiiException {
        try {

            PagedResult<MapsetDTO> pagedResult =
                mapsetService.getMapSets(pageSize, page, studyDbId);

            BrApiMasterListPayload<MapsetDTO> payload =
                new BrApiMasterListPayload<>(
                    pagedResult.getResult(), pagedResult.getCurrentPageSize(),
                    pagedResult.getCurrentPageNum());

            return ResponseEntity.ok(payload);
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal server error");
        }

    }

    @ApiOperation(
        value = "Get Map by mapDbId", notes = "Gets a genome map by map id",
        tags = {"Genome Maps"}, extensions = {
            @Extension(properties = {@ExtensionProperty(name="summary", value="Get Map by Id")})
        })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required = true, paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/maps/{mapDbId}", method=RequestMethod.GET,
        produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterPayload<MapsetDTO>> getMapByMapId(
        @ApiParam(value = "ID of the Genome Map", required = true)
        @PathVariable(value = "mapDbId") Integer mapDbId) throws GobiiException
    {
        try {

            MapsetDTO mapset = mapsetService.getMapSetById(mapDbId);
            BrApiMasterPayload<MapsetDTO> payload =
                    new BrApiMasterPayload<>(mapset);
            return ResponseEntity.ok(payload);

        }
        catch(Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Entity does not exist"
            );
        }
    }


    @ApiOperation(
        value = "List Marker positions", notes = "Lists Marker positions in GDM system.",
        tags = {"Genome Maps"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List Marker positions")
            })
        })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required = true, paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/markerpositions", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<MarkerPositions>> getMarkersByMapId(
        @ApiParam(value = "Used to request a specific page of data to be returned. " +
           "The page indexing starts at 0 (the first page is 'page'= 0). Default is 0")
            @RequestParam(value = "page", required = false,
                defaultValue = BrapiDefaults.pageNum) Integer page,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize,
        @ApiParam(value = "Filter positions greater than given value")
            @RequestParam(value = "minPosition", required = false) BigDecimal minPosition,
        @ApiParam(value = "Filter positions less than given value")
            @RequestParam(value = "maxPosition", required = false) BigDecimal maxPosition,
        @ApiParam(value = "Filter By VariantSet Db Id")
            @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId,
        MarkerPositions markerPositionsFilter) throws GobiiException {

        try {

            PagedResult<MarkerPositions> pagedResult =
                markerPositionsService.getMarkerPositions(
                    pageSize, page, markerPositionsFilter, minPosition,
                    maxPosition, variantSetDbId);

            BrApiMasterListPayload<MarkerPositions> payload =
                new BrApiMasterListPayload<>(
                    pagedResult.getResult(), pagedResult.getCurrentPageSize(),
                    pagedResult.getCurrentPageNum());

            return ResponseEntity.ok(payload);

        }
        catch(GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Entity does not exist"
            );
        }
    }

    /**
     * Lists the variantsets by page size and page token
     *
     * @param pageNum - Page number to be fetched. 0 indexed.
     * @param pageSize - Page size set by the user.
     *                 If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size
     * @return Brapi response with list of variantsets
     */
    @ApiOperation(
        value = "List VariantSets", notes = "Lists VariantSets in GDM system.",
        tags = {"VariantSets"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List VariantSets")
            })
        })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = VariantSetListResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorPayload.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets", method=RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<VariantSetDTO>> getVariantSets(
        @ApiParam(value = "Id of the VariantSet to be fetched. Corresponds to dataset Id")
            @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId,
        @ApiParam(value = "Study Id for which list of VariantSets need to be fetched. " +
            "Study corresponds to experiment")
            @RequestParam(value = "studyDbId", required = false) Integer studyDbId,
        @ApiParam(value = "Study Name for which list of VariantSets need to be fetched")
            @RequestParam(value = "studyName", required = false) String studyName,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize,
        @ApiParam(value = "Page number to be fetched")
            @RequestParam(value = "page", required = false,
                defaultValue = BrapiDefaults.pageNum) Integer pageNum) {
        try {

            PagedResult<VariantSetDTO> pagedResult =
                    variantSetsService.getVariantSets(
                        pageSize, pageNum, variantSetDbId, null,
                        studyDbId, studyName);

            BrApiMasterListPayload<VariantSetDTO> payload =
                    new BrApiMasterListPayload<>(
                        pagedResult.getResult(), pagedResult.getCurrentPageSize(),
                        pagedResult.getCurrentPageNum());

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage()
            );
        }
    }

    /**
     * Endpoint for getting a specific variantSet with a given variantSetDbId
     *
     * @param variantSetDbId ID of the requested variantSet
     * @return ResponseEntity with http status code specifying,
     *          if retrieval of the variantSet is successful.
     * Response body contains the requested variantSet information
     */
    @ApiOperation(
        value = "Get VariantSet by variantSetDbId",
        notes = "Retrieves the VariantSet entity having the specified ID",
        tags = {"VariantSets"},
        extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="Get VariantSet by Id")
            })
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = VariantSetResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 404, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required = true, paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}", method=RequestMethod.GET,
        produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterPayload<VariantSetDTO>> getVariantSetById(
        @ApiParam(value = "ID of the VariantSet to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId)
    {
        try {

            VariantSetDTO variantSetDTO =
                    variantSetsService.getVariantSetById(variantSetDbId);
            BrApiMasterPayload<VariantSetDTO> payload =
                    new BrApiMasterPayload<>(variantSetDTO);
            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Entity does not exist"
            );
        }
    }

    /**
     * Lists the variants for a given VariantSetDbId by page size and page token
     *
     * @param variantSetDbId - Integer ID of the VariantSet to be fetched
     * @param pageToken - String page token
     * @param pageSize - Page size set by the user.
     *                 If page size is more than maximum allowed page size,
     *                 then the response will have maximum page size
     * @return Brapi response with list of CallSets
     */
    @ApiOperation(
        value = "List Variants in VariantSet",
        notes = "Lists all the Variants in VariantSet with variantSetDbId",
        tags = {"VariantSets"},
        extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List Variants in VariantSet")
            })
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = VariantListResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 404, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required = true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}/variants", method=RequestMethod.GET,
        produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<VariantDTO>>
    getVariantsByVariantSetDbId(
        @ApiParam(value = "ID of the VariantSet of the Variants to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
        @ApiParam(value = "Page Token to fetch a page. Value is " +
            "$metadata.pagination.nextPageToken form previous page.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize,
        @ApiParam(value = "Get Variant with given variantDbId.")
            @RequestParam(value = "variantDbId", required = false) Integer variantDbId
    ){
        try {
            VariantSetDTO variantSet = variantSetsService.getVariantSetById(variantSetDbId);
            return getVariants(pageSize, pageToken, variantDbId, variantSet.getVariantSetDbId());
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Entity does not exist"
            );
        }
    }

    /**
     * Lists the callsets for a given VariantSetDbId by page size and page token
     *
     * @param variantSetDbId - Integer ID of the VariantSet to be fetched
     * @param pageSize - Page size set by the user. If page size is more
     *                 than maximum allowed page size,
     *                 then the response will have maximum page size
     * @return Brapi response with list of CallSets
     */
    @ApiOperation(
        value = "List CallSets in VariantSet", notes = "Lists CallSets in VariantSet",
        tags = {"VariantSets"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List CallSets in VariantSet")
            })
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = CallSetListResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 404, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                name="Authorization", value="Authentication Token",
                required = true, paramType = "header", dataType = "string"
            )
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}/callsets", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<CallSetDTO>>
    getCallSetsByVariantSetDbId(
        @ApiParam(value = "ID of the VariantSet", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
        @ApiParam(value = "Page number", defaultValue = BrapiDefaults.pageNum)
            @RequestParam(value = "page", required = false,
                defaultValue = BrapiDefaults.pageNum) Integer page,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize,
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
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage()
            );
        }
    }

    @ApiOperation(
        value = "List Genotypes in VariantSet",
        notes = "List of all the genotype calls in a given VariantSet",
        tags = {"VariantSets"},
        extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List Genotypes in VariantSet")
            })
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = GenotypeCallsListResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 404, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets/{variantSetDbId}/calls", method=RequestMethod.GET,
        produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterListPayload<GenotypeCallsDTO>>
    getCallsByVariantSetDbId(
        @ApiParam(value = "ID of the VariantSet", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
        @ApiParam(value = "Page Token to fetch a page. " +
            "Value is $metadata.pagination.nextPageToken form previous page.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize) {
        try {

            PagedResult<GenotypeCallsDTO> pagedResult =
                genotypeCallsService
                    .getGenotypeCallsByVariantSetDbId(variantSetDbId, pageSize, pageToken);


            BrApiMasterListPayload<GenotypeCallsDTO> payload =
                new BrApiMasterListPayload<>(
                    pagedResult.getResult(), pagedResult.getCurrentPageSize(),
                    pagedResult.getNextPageToken());

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage());
        }
    }



    @ApiOperation(
        value = "Search Genotypes", notes = "Creates a search query for genotypes search",
        tags = {"Genotype Calls"}, extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="Search Genotypes")
            })
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "", response = SearchResultResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 404, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/search/calls", method = RequestMethod.POST,
        consumes = "application/json", produces = "application/json")
    public ResponseEntity<BrApiMasterPayload<SearchResultDTO>> searchGenotypeCalls(
        @Valid @RequestBody GenotypeCallsSearchQueryDTO genotypeCallsSearchQuery,
        HttpServletRequest request) {

        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            if (genotypeCallsSearchQuery != null) {

                SearchResultDTO searchResultDTO =
                    searchService.createSearchQueryResource(cropType, genotypeCallsSearchQuery);

                BrApiMasterPayload<SearchResultDTO> payload =
                    new BrApiMasterPayload<>(searchResultDTO);

                return  ResponseEntity.status(HttpStatus.CREATED).body(payload);

            }
            else {
                throw new GobiiException(
                    GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Missing Request body"
                );
            }

        }
        catch (GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.NONE,
                "Internal Server Error " + e.getMessage()
            );
        }
    }

    @ApiOperation(
        value = "List Genotypes for SearchQuery",
        notes = "List of all the genotype calls for given search query",
        tags = {"Genotype Calls"},
        extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="List Genotypes for SearchQuery")
            })
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "", response = GenotypeCallsListResponse.class),
        @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 404, message = "", response = ErrorPayload.class),
        @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/search/calls/{searchResultDbId}", method = RequestMethod.GET,
        produces = "application/json")
    public ResponseEntity<BrApiMasterListPayload<GenotypeCallsDTO>> getGenotypeCallsBySearchQuery(
        @ApiParam(value = "Search Query Id for which genotypes need to be fetched.")
            @PathVariable String searchResultDbId,
        @ApiParam(value = "Page Token to fetch a page. " +
            "Value is $metadata.pagination.nextPageToken form previous page.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
        @ApiParam(value = "Size of the page to be fetched. Default is 1000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.pageSize) Integer pageSize,
        HttpServletRequest request
    ) {

        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            GenotypeCallsSearchQueryDTO genotypeCallsSearchQueryDTO =
                searchService.getGenotypesSearchQuery(searchResultDbId, cropType);

            PagedResult<GenotypeCallsDTO> pagedResult =
                genotypeCallsService
                    .getGenotypeCallsByExtractQuery(
                        genotypeCallsSearchQueryDTO, pageSize, pageToken);

            BrApiMasterListPayload<GenotypeCallsDTO> payload =
                new BrApiMasterListPayload<>(
                    pagedResult.getResult(), pagedResult.getCurrentPageSize(),
                    pagedResult.getNextPageToken());


            return ResponseEntity.ok(payload);

        }
        catch (GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.NONE,
                "Internal Server Error " + e.getMessage()
            );

        }
    }

    @ApiOperation(
        value = "Download Genotypes in VariantSet",
        notes = "Download of all the genotype calls in a given VariantSet",
        tags = {"VariantSets"},
        extensions = {
            @Extension(properties = {
                @ExtensionProperty(name="summary", value="Download Genotypes in VariantSet")
            })
        }
    )
    @ApiImplicitParams({
        @ApiImplicitParam(
            name="Authorization", value="Authentication Token",
            required=true, paramType = "header", dataType = "string")
    })
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 401, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 404, message = "", response = ErrorPayload.class),
            @ApiResponse(code = 500, message = "", response = ErrorPayload.class)
        }
    )
    @RequestMapping(
        value="/variantsets/{variantSetDbId:[\\d]+}/calls/download", method=RequestMethod.GET,
        produces = "text/csv")
    public ResponseEntity<ResponseBodyEmitter> handleRbe(
        @ApiParam(value = "Id of the variantset to download")
            @PathVariable("variantSetDbId") Integer variantSetDbId
    ) {

        //Giving Response emitter to finish the download within 30 mins.
        //The request thread will be terminated once 30 mins is done.
        ResponseBodyEmitter emitter = new ResponseBodyEmitter((long)1800000);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {

           try {

               int pageNum = 0;

               String genotypesResult =
                   genotypeCallsService.getGenotypeCallsAsString(variantSetDbId, pageNum);

               while(genotypesResult != null && genotypesResult.length() != 0) {

                   emitter.send(genotypesResult, MediaType.TEXT_PLAIN);

                   pageNum++;

                   genotypesResult =
                       genotypeCallsService.getGenotypeCallsAsString(variantSetDbId, pageNum);

               }

               emitter.complete();

           } catch (Exception e) {

               e.printStackTrace();
               emitter.completeWithError(e);
               return;
           }

           emitter.complete();

        });

        return ResponseEntity
            .ok()
            .header(
                "Content-Disposition", "attachment; filename=" + variantSetDbId.toString() + ".csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(emitter);
    }

}
