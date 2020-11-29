package org.gobiiproject.gobiiweb.controllers.gdm.v3;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobiidomain.services.gdmv3.MarkerService;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;
import org.gobiiproject.gobiiweb.security.CropAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.gobiiproject.gobiimodel.config.Roles.CURATOR;

@Scope(value = "request")
@Controller
@RequestMapping("/crops/{cropType}/gobii/v3/markers")
@CrossOrigin
@Api
@Slf4j
public class MarkerController {


    private final MarkerService markerService;

    /**
     * Constructor
     *
     * @param markerService {@link MarkerService}
     */
    @Autowired
    public MarkerController(final MarkerService markerService) {
        this.markerService = markerService;
    }


    @CropAuth(CURATOR)
    @PostMapping(value = "/file-upload",
        consumes = "multipart/form-data",
        produces = "application/json")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<JobDTO>> uploadMarkers(
        @PathVariable final String cropType,
        @RequestPart("markerFile") MultipartFile markerFile,
        @RequestPart("fileProperties") final MarkerUploadRequestDTO markerUploadRequest
    ) throws Exception {

        byte[] markerFileBytes = markerFile.getBytes();

        JobDTO job = markerService.loadMarkerData(
            markerFileBytes,
            markerUploadRequest,
            cropType);

        BrApiMasterPayload<JobDTO> payload = ControllerUtils.getMasterPayload(job);

        return ResponseEntity.accepted().body(payload);

    }





}
