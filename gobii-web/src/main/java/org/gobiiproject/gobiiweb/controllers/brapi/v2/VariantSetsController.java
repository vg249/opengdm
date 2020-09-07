package org.gobiiproject.gobiiweb.controllers.brapi.v2;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobidomain.services.brapi.CallSetService;
import org.gobiiproject.gobidomain.services.brapi.GenotypeCallsService;
import org.gobiiproject.gobidomain.services.brapi.VariantService;
import org.gobiiproject.gobidomain.services.brapi.VariantSetsService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.*;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterListPayload;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiResult;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.ErrorPayload;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.dto.system.PagedResultTyped;
import org.gobiiproject.gobiimodel.types.BrapiDefaults;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Scope(value = "request")
@Controller
@RequestMapping("/brapi/v2/variantsets")
@CrossOrigin
@Api
@Slf4j
public class VariantSetsController {

    private final VariantSetsService variantSetsService;
    private final CallSetService callSetService;
    private final VariantService variantService;
    private final GenotypeCallsService genotypeCallsService;

    private class GenotypeCallsListResponse
        extends BrApiMasterPayload<BrApiResult<GenotypeCallsDTO>>{}
    private class VariantListResponse extends BrApiMasterPayload<BrApiResult<VariantDTO>>{}
    private class CallSetListResponse extends BrApiMasterPayload<BrApiResult<CallSetDTO>>{}
    private class VariantSetResponse extends BrApiMasterPayload<VariantSetDTO>{}
    private class VariantSetListResponse extends BrApiMasterPayload<BrApiResult<VariantSetDTO>>{}

    @Autowired
    public VariantSetsController(final VariantSetsService variantSetsService,
                                 final CallSetService callSetService,
                                 final VariantService variantService,
                                 final GenotypeCallsService genotypeCallsService) {
        this.variantSetsService = variantSetsService;
        this.callSetService = callSetService;
        this.variantService = variantService;
        this.genotypeCallsService = genotypeCallsService;
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
    @RequestMapping(value="/", method= RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<BrApiMasterListPayload<VariantSetDTO>> getVariantSets(
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

            PagedResult<VariantSetDTO> pagedResult = variantSetsService.getVariantSets(
                pageSize,
                pageNum,
                variantSetDbId,
                null,
                studyDbId,
                studyName);

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
    @RequestMapping(value="/{variantSetDbId:[\\d]+}", method=RequestMethod.GET,
        produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterPayload<VariantSetDTO>> getVariantSetById(
        @ApiParam(value = "ID of the VariantSet to be extracted", required = true)
        @PathVariable("variantSetDbId") Integer variantSetDbId)
    {
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
    @RequestMapping(value="/{variantSetDbId:[\\d]+}/variants", method=RequestMethod.GET,
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

            PagedResult<VariantDTO> pagedResult = variantService.getVariants(
                pageSize,
                pageToken,
                variantDbId,
                variantSet.getVariantSetDbId());

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
                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Entity does not exist"
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
    @RequestMapping(value="/{variantSetDbId}/calls", method=RequestMethod.GET,
        produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterPayload<GenotypeCallsResult>>
    getCallsByVariantSetDbId(
        @ApiParam(value = "ID of the VariantSet", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
        @ApiParam(value = "Name of the mapset")
            @RequestParam(value = "mapName", required = false) String mapName,
        @ApiParam(value = "Name of the linkagage group")
            @RequestParam(value = "linkageGroupName", required = false) String linkageGroupName,
        @ApiParam(value = "Minimum marker position")
            @RequestParam(value = "minPosition", required = false) BigDecimal minPosition,
        @ApiParam(value = "Maximum marker position")
            @RequestParam(value = "maxPosition", required = false) BigDecimal maxPosition,
        @ApiParam(value = "Page Token to fetch a page. " +
            "Value is $metadata.pagination.nextPageToken form previous page.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
        @ApiParam(value = "Size of the page to be fetched. Default is 100000.")
            @RequestParam(value = "pageSize", required = false,
                defaultValue = BrapiDefaults.genotypesPageSize) Integer pageSize) {
        try {

            PagedResultTyped<GenotypeCallsResult> genotypeCallsPaged =
                genotypeCallsService.getGenotypeCallsByVariantSetDbId(
                    variantSetDbId,
                    mapName,
                    linkageGroupName,
                    minPosition,
                    maxPosition,
                    pageSize,
                    pageToken);

            BrApiMasterPayload<GenotypeCallsResult> payload = new BrApiMasterPayload<>(
                genotypeCallsPaged.getResult(),
                genotypeCallsPaged.getCurrentPageSize(),
                genotypeCallsPaged.getNextPageToken());

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
     * Lists the callsets for a given VariantSetDbId by page size and page number
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
        })})
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
    @RequestMapping(value="/{variantSetDbId:[\\d]+}/callsets", method=RequestMethod.GET)
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

            PagedResult<CallSetDTO> callSets = callSetService.getCallSets(
                pageSize,
                page,
                variantSet.getVariantSetDbId(),
                callSetsFilter);

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
            log.error(e.getMessage(), e);
            throw new GobiiException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage()
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
        value="/{variantSetDbId:[\\d]+}/calls/download", method=RequestMethod.GET,
        produces = "text/csv")
    public ResponseEntity<ResponseBodyEmitter> handleRbe(
        @ApiParam(value = "Id of the variantset to download")
        @PathVariable("variantSetDbId") Integer variantSetDbId
    ) throws GobiiException {

        //Giving Response emitter to finish the download within 30 mins.
        //The request thread will be terminated once 30 mins is done.
        ResponseBodyEmitter emitter = new ResponseBodyEmitter((long)1800000);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                int pageNum = 0;

                String genotypesResult = genotypeCallsService.getGenotypeCallsAsString(
                    variantSetDbId,
                    pageNum);

                while(genotypesResult != null && genotypesResult.length() != 0) {

                    emitter.send(genotypesResult, MediaType.TEXT_PLAIN);

                    pageNum++;

                    genotypesResult = genotypeCallsService.getGenotypeCallsAsString(
                        variantSetDbId,
                        pageNum);

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
                "Content-Disposition",
                "attachment; filename=" + variantSetDbId.toString() + ".csv"
            )
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(emitter);
    }


}
