package org.gobiiproject.gobiidomain.services.gdmv3;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.FileDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Paths;
import java.util.Date;

@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private ContactService contactService;

    private String fileUploadUrlFormat = "/files/%s";

    private final String manifestFileName = "MANIFEST.json";

    ObjectMapper mapper = new ObjectMapper();

    public FileDTO initiateFileUpload(String cropType) throws GobiiDomainException {

        FileDTO fileDTO = new FileDTO();

        try {
            ContactDTO createdBy = contactService.getCurrentUser();
            fileDTO.setCreatedBy(createdBy.getContactId().toString());
            fileDTO.setCreatedDate(new Date());
            fileDTO.setFileId(Utils.getUniqueName());
            fileDTO.setFileUploadUrl(String.format(fileUploadUrlFormat, fileDTO.getFileId()));

            String fileDir = Utils.getRawUserFilesDir(fileDTO.getFileId(), cropType);

            fileDTO.setSystemFilePath(fileDir);

            String manifestFilePath = Paths.get(fileDir, manifestFileName).toString();
            File manifestFile = new File(manifestFilePath);

            mapper.writeValue(manifestFile, fileDTO);

            return fileDTO;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new GobiiDomainException(e);
        }
    }

    public FileDTO updateFileChunk(FileDTO fileToUpdate,
                                   String cropType,
                                   InputStream inputStream) throws GobiiDomainException {

        FileDTO fileManifestDTO;

        // Get file manifest
        String fileDir = Utils.getRawUserFilesDir(fileToUpdate.getFileId(), cropType);
        String manifestFilePath = Paths.get(fileDir, manifestFileName).toString();
        File manifestFile = new File(manifestFilePath);
        if(!manifestFile.exists()) {
            log.error("Invalid file upload id");
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "file upload is not initiated or invalid file upload id");
        }
        try {
            fileManifestDTO = mapper.readValue(manifestFile, FileDTO.class);

            // Update the manifest with file name for first upload
            if(StringUtils.isEmpty(fileManifestDTO.getFileName())) {
                fileManifestDTO.setFileName(fileToUpdate.getFileName());
            }

            // Update the manifest with mime type for first upload
            if(StringUtils.isEmpty(fileManifestDTO.getMimeType())) {
                fileManifestDTO.setMimeType(fileToUpdate.getMimeType());
            }
        }
        catch (IOException e) {
            log.error("Invalid file upload id");
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "uninitiated file upload");
        }

        String filePath = Paths.get(
            Utils.getRawUserFilesDir(fileToUpdate.getFileId(), cropType),
            fileManifestDTO.getFileName()
        ).toString();

        File destFile = new File(filePath);

        // Write the file. append if existing.
        try(OutputStream outputStream = new FileOutputStream(destFile, true)) {

            inputStream.transferTo(outputStream);
            fileManifestDTO.setModifiedDate(new Date());
            fileManifestDTO
                .setModifiedBy(contactService.getCurrentUser().getContactId().toString());

            // update the manifest
            mapper.writeValue(manifestFile, fileManifestDTO);

            return fileManifestDTO;
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new GobiiDomainException(e);
        }
    }
}
