package org.gobiiproject.gobiiweb.controllers.brapi.v2;

import io.swagger.annotations.*;
import org.gobiiproject.gobiidomain.services.brapi.StudiesService;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.StudiesDTO;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterListPayload;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiResult;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.ErrorPayload;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
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
@RequestMapping(GobiiControllerType.SERVICE_PATH_BRAPI_V2)
@CrossOrigin
@Api
public class StudiesController {

    private class StudiesListResponse extends BrApiMasterPayload<BrApiResult<StudiesDTO>> {}

    private final StudiesService studiesService;

    @Autowired
    public StudiesController(final StudiesService studiesService) {
        this.studiesService = studiesService;
    }

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
    @GetMapping(value="/studies", produces = "application/json")
    public @ResponseBody
    ResponseEntity<BrApiMasterListPayload<StudiesDTO>> getStudies(
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
                    studies.getResult(),
                    studies.getCurrentPageSize(),
                    studies.getCurrentPageNum());

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

}
