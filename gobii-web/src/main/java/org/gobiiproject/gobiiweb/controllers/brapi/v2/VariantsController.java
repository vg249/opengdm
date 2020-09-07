package org.gobiiproject.gobiiweb.controllers.brapi.v2;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobidomain.services.brapi.GenotypeCallsService;
import org.gobiiproject.gobidomain.services.brapi.VariantService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsResult;
import org.gobiiproject.gobiimodel.dto.brapi.VariantDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Scope(value = "request")
@Controller
@RequestMapping("/brapi/v2/variants")
@CrossOrigin
@Api
@Slf4j
public class VariantsController {


    private class VariantListResponse extends BrApiMasterPayload<BrApiResult<VariantDTO>>{}
    private class VariantResponse extends BrApiMasterPayload<VariantDTO>{}
    private class GenotypeCallsResponse extends BrApiMasterPayload<GenotypeCallsDTO>{}

    private final VariantService variantService;
    private final GenotypeCallsService genotypeCallsService;

    @Autowired
    public VariantsController(final VariantService variantService,
                              final GenotypeCallsService genotypeCallsService) {
        this.variantService = variantService;
        this.genotypeCallsService = genotypeCallsService;
    }

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
                @ExtensionProperty(name="summary", value="List Variants")
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
    @GetMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity<BrApiMasterListPayload<VariantDTO>> getVariants(
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

            PagedResult<VariantDTO> pagedResult = variantService.getVariants(
                pageSize,
                pageToken,
                variantDbId,
                variantSetDbId);

            BrApiMasterListPayload<VariantDTO>  payload =
                new BrApiMasterListPayload<>(
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
        value = "Get Variant by variantDbId",
        notes = "Retrieves the Variant with variantDbId",
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
    @GetMapping(value="/{variantDbId:[\\d]+}", produces = "application/json")
    public @ResponseBody ResponseEntity<BrApiMasterPayload<VariantDTO>>
    getVariantsByVariantDbId(
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
    @RequestMapping(value="/{variantDbId}/calls", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<BrApiMasterPayload<GenotypeCallsResult>>
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

            PagedResultTyped<GenotypeCallsResult> genotypeCallsPaged =
                genotypeCallsService.getGenotypeCallsByVariantDbId(
                    variantDbId,
                    pageSize,
                    pageToken);

            BrApiMasterPayload<GenotypeCallsResult> payload =
                new BrApiMasterPayload<>(
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
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                "Internal Server Error" + e.getMessage());
        }
    }


}
