package org.gobiiproject.gobiiweb.controllers.gdm.v3;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobiidomain.services.gdmv3.DnaRunService;
import org.gobiiproject.gobiidomain.services.gdmv3.GenotypeService;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypesUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
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
@RequestMapping("/crops/{cropType}/gobii/v3/genotypes")
@CrossOrigin
@Api
@Slf4j
public class GenotypesController {


    private final GenotypeService genotypeService;

    /**
     * Constructor
     *
     * @param genotypeService {@link GenotypeService}
     */
    @Autowired
    public GenotypesController(final GenotypeService genotypeService) {
        this.genotypeService = genotypeService;
    }


    @CropAuth(CURATOR)
    @PostMapping(value = "/file-upload",
        consumes = "multipart/form-data",
        produces = "application/json")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<JobDTO>> uploadGenotypes(
        @PathVariable final String cropType,
        @RequestPart("genotypesFile") MultipartFile genotypeFile,
        @RequestPart("fileProperties") final GenotypesUploadRequestDTO genotypesUploadRequest
        ) throws Exception {

        InputStream dnaRunFileInputStream = genotypeFile.getInputStream();

        JobDTO job = genotypeService.loadGenotypes(
            dnaRunFileInputStream,
            genotypeFile.getOriginalFilename(),
            genotypesUploadRequest,
            cropType);

        BrApiMasterPayload<JobDTO> payload = ControllerUtils.getMasterPayload(job);

        return ResponseEntity.accepted().body(payload);

    }





}
