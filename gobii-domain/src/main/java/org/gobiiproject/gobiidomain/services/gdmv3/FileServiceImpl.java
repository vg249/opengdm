package org.gobiiproject.gobiidomain.services.gdmv3;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiidomain.services.gdmv3.exceptions.InvalidException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.FileDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.FileManifestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private ContactService contactService;

    private String fileUploadUrlFormat = "/files/%s";
    private String filePathUrlFormat = "/files/%s/%s";

    private final String manifestFileName = "MANIFEST.json";

    private final String fileType = "file";
    private final String directoryType = "directory";

    ObjectMapper mapper = new ObjectMapper();

    /**
     * Intiate file upload
     * @param cropType
     * @return {@link FileDTO} with file path 
     * @throws GobiiDomainException
     */
    public FileManifestDTO initiateFileUpload(String cropType) throws GobiiDomainException {

        FileManifestDTO fileDTO = new FileManifestDTO();

        try {
            ContactDTO createdBy = contactService.getCurrentUser();
            fileDTO.setCreatedBy(createdBy.getContactId().toString());
            fileDTO.setCreatedDate(new Date());
            fileDTO.setFileUploadId(Utils.getUniqueName());
            fileDTO.setFileUploadUrl(
                String.format(fileUploadUrlFormat, fileDTO.getFileUploadId()));

            String fileDir = Utils.getRawUserFilesDir(fileDTO.getFileUploadId(), cropType);

            fileDTO.setServerFilePath(fileDir);

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

    /**
     * 
     */
    public FileDTO updateFileChunk(FileManifestDTO fileToUpdate,
                                   String cropType,
                                   InputStream inputStream) throws GobiiDomainException {

        FileManifestDTO fileManifestDTO;
        FileDTO file = new FileDTO();

        // Get file manifest
        String fileDir = Utils.getRawUserFilesDir(fileToUpdate.getFileUploadId(), cropType);
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
            fileManifestDTO = mapper.readValue(manifestFile, FileManifestDTO.class);

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
            throw new InvalidException("requst: uninitiated filed upload");
        }

        // check file name conflict between chunks
        if(!fileToUpdate.getFileName().equals(fileManifestDTO.getFileName())) {
            throw new InvalidException("file name mismacth between files");
        }

        String filePath = Paths.get(
            Utils.getRawUserFilesDir(fileToUpdate.getFileUploadId(), cropType),
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

            String filePathUrl = 
                String.format(filePathUrlFormat, 
                              fileManifestDTO.getFileUploadId(), 
                              fileToUpdate.getFileName());

            file.setServerFilePath(filePath);
            file.setFileUrlPath(filePathUrl);
            file.setFileManifestPath(manifestFilePath);
            file.setFileName(fileToUpdate.getFileName());

            return file;
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new GobiiDomainException(e);
        }
    }

    public PagedResult<FileDTO> listFilesByFilePath(String filePath,
                                                    String cropType,
                                                    Integer pageSize,
                                                    Integer pageNum) throws GobiiException {
    
        List<FileDTO> files = new ArrayList<>();

        String userFilesDir = Utils.getProcessDir(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        File requestFile = Paths.get(userFilesDir, filePath).toFile();

        if(!requestFile.exists()) {
            throw new InvalidException("file path");
        }

        if(pageNum < 0 || pageSize < 0) {
            throw new InvalidException("page number or page size");
        }

        // If path is a directory path, list all files insde them
        if(requestFile.isDirectory()) {
            File[] childFiles = requestFile.listFiles();
            
            int startIndex = pageNum*pageSize;
            int endIndex = startIndex+pageSize;

            endIndex = endIndex > childFiles.length ? childFiles.length : endIndex;

            for(int i = startIndex; i < endIndex; i++) {
                File childFile = childFiles[i];
                FileDTO fileDTO = mapFileProperties(childFile); 
                files.add(fileDTO);
            }
        }
        else {
            files.add(mapFileProperties(requestFile));
        }

        return PagedResult.createFrom(pageNum, files);
    }

    private FileDTO mapFileProperties(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName(file.getName());
        fileDTO.setServerFilePath(file.getAbsolutePath());
        if(file.isDirectory()) {
            fileDTO.setType(directoryType);
        }
        else {
            fileDTO.setType(fileType);
        }
        return fileDTO;
    }
}