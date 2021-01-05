package org.gobiiproject.gobiiweb.controllers.gdm.v3;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobiidomain.services.gdmv3.DnaRunService;
import org.gobiiproject.gobiidomain.services.gdmv3.MarkerService;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;
import org.gobiiproject.gobiiweb.security.CropAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static org.gobiiproject.gobiimodel.config.Roles.CURATOR;

@Scope(value = "request")
@Controller
@RequestMapping("/crops/{cropType}/gobii/v3/dnaruns")
@CrossOrigin
@Api
@Slf4j
public class DnaRunController {


    private final DnaRunService dnaRunService;

    /**
     * Constructor
     *
     * @param dnaRunService {@link DnaRunService}
     */
    @Autowired
    public DnaRunController(final DnaRunService dnaRunService) {
        this.dnaRunService = dnaRunService;
    }


    @CropAuth(CURATOR)
    @PostMapping(value = "/file-upload",
        consumes = "multipart/form-data",
        produces = "application/json")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<JobDTO>> uploadDnaRuns(
        @PathVariable final String cropType,
        @RequestPart("dnaRunFile") MultipartFile dnaRunFile,
        @RequestPart("fileProperties") final DnaRunUploadRequestDTO dnaRunUploadRequest
    ) throws Exception {

        InputStream dnaRunFileInputStream = dnaRunFile.getInputStream();

        JobDTO job = dnaRunService.loadDnaRuns(
            dnaRunFileInputStream,
            dnaRunUploadRequest,
            cropType);

        BrApiMasterPayload<JobDTO> payload = ControllerUtils.getMasterPayload(job);

        return ResponseEntity.accepted().body(payload);

    }





}
