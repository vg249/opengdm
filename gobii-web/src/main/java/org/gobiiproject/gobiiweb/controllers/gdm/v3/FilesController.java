package org.gobiiproject.gobiiweb.controllers.gdm.v3;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiidomain.services.gdmv3.FileService;
import org.gobiiproject.gobiidomain.services.gdmv3.JobService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.gdmv3.FileDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypesUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiiweb.security.CropAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.gobiiproject.gobiimodel.config.Roles.CURATOR;

@Scope(value = "request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_GOBII_V3)
@Api()
@CrossOrigin
@Slf4j
public class FilesController {

    private final FileService fileService;

    /**
     * Constructor
     * @param fileService    {@link FileService}
     */
    @Autowired
    public FilesController(final FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Initiate file upload request
     * @return {@link FileDTO}
     * @throws Exception
     */
    @CropAuth(CURATOR)
    @PostMapping("/files")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<FileDTO>>
    initiateFileUploadRequest(@PathVariable final String cropType) throws GobiiException {
        FileDTO fileDTO = fileService.initiateFileUpload(cropType);
        BrApiMasterPayload<FileDTO> result = ControllerUtils.getMasterPayload(fileDTO);
        return ResponseEntity.ok(result);
    }

    @CropAuth(CURATOR)
    @PostMapping("/files/{fileUploadId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<FileDTO>>
    updateFileChunk(@PathVariable final String cropType,
                    @PathVariable final String fileUploadId,
                    @RequestPart("file") MultipartFile file) throws GobiiException {

        FileDTO fileToUpdate = new FileDTO();
        fileToUpdate.setFileName(file.getOriginalFilename());
        fileToUpdate.setMimeType(file.getContentType());
        fileToUpdate.setFileId(fileUploadId);

        InputStream fileChunkInputStream;
        try {
            fileChunkInputStream = file.getInputStream();
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new GobiiException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Error in rading file chunk");
        }

        FileDTO updatedFile = fileService.updateFileChunk(
            fileToUpdate,
            cropType,
            fileChunkInputStream);

        BrApiMasterPayload<FileDTO> result = ControllerUtils.getMasterPayload(updatedFile);

        return ResponseEntity.ok(result);
    }
}
