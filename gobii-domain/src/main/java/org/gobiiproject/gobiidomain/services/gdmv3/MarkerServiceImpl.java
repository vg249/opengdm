package org.gobiiproject.gobiidomain.services.gdmv3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.*;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.modelmapper.EntityFieldBean;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiisampletrackingdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("unchecked")
@Transactional
public class MarkerServiceImpl implements MarkerService {

    @Autowired
    private LoaderTemplateDao loaderTemplateDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private MapsetDao mapsetDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private JobDao jobDao;

    final ObjectMapper mapper = new ObjectMapper();

    final String loadType = "MARKER";

    @Override
    public JobDTO uploadMarkerFile(
        byte[] markerFile,
        MarkerUploadRequestDTO markerUploadRequest,
        String cropType) throws GobiiException {

        BufferedReader br;
        LoaderInstruction loaderInstruction = new LoaderInstruction();
        loaderInstruction.setLoadType(loadType);
        loaderInstruction.setAspects(new HashMap<>());

        String fileHeader;
        Map<String, Object> markerTemplateMap;

        loaderInstruction.setCropType(cropType);

        // Tables loaded in marker upload
        MarkerTable markerTable = new MarkerTable();
        LinkageGroupTable linkageGroupTable = new LinkageGroupTable();
        MarkerLinkageGroupTable markerLinkageGroupTable = new MarkerLinkageGroupTable();

        // Get tables names in database
        String markerTableName = DaoUtils.getTableName(Marker.class);
        String linkageGroupTableName = DaoUtils.getTableName(LinkageGroup.class);
        String markerLinkageTableName = DaoUtils.getTableName(MarkerLinkageGroup.class);


        // Set Platform Id in Table Aspects
        Platform platform = platformDao.getPlatform(markerUploadRequest.getPlatformId());
        if(platform == null) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Invalid Platform");
        }
        markerTable.setPlatformId(platform.getPlatformId().toString());
        markerLinkageGroupTable.setPlatformId(platform.getPlatformId().toString());


        // Set mapSet for linkage group and marker linkage group
        if(markerUploadRequest.getMapId() != null) {
            Mapset mapset = mapsetDao.getMapset(markerUploadRequest.getMapId());
            if(mapset == null) {
                throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid mapset id");
            }
            linkageGroupTable.setMapId(mapset.getMapsetId().toString());
            markerLinkageGroupTable.setMapId(mapset.getMapsetId().toString());
        }

        //Set new status for marker table
        Cv status = cvDao.getCvs(
            "new",
            CvGroupTerm.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        markerTable.setStatus(status.getCvId().toString());


        // Read marker template
        LoaderTemplate loaderTemplate = loaderTemplateDao.getById(
            markerUploadRequest.getMarkerTemplateId());
        try {
            markerTemplateMap = mapper.treeToValue(
                loaderTemplate.getTemplate(), HashMap.class);
        }
        catch (JsonProcessingException jE) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Invalid marker template object.");
        }

        // Create Job
        String userName = ContactService.getCurrentUser();
        Contact createdBy;
        try {
            createdBy = contactDao.getContactByUsername(userName);
        }
        catch (Exception e) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.NONE,
                "Unable to find user in system");
        }
        String jobName = UUID.randomUUID().toString().replace("-", "");
        Job job = new Job();
        job.setJobName(jobName);
        job.setMessage("Submitted marker upload job");
        // Get payload type
        Cv payloadType = cvDao.getCvs(GobiiLoaderPayloadTypes.MARKERS.getTerm(),
            CvGroupTerm.CVGROUP_PAYLOADTYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        job.setPayloadType(payloadType);
        job.setStatus(status);
        job.setSubmittedBy(createdBy);
        job.setSubmittedDate(new Date());
        // Get load type
        Cv jobType = cvDao.getCvs(
            JobType.CV_JOBTYPE_LOAD.getCvName(),
            CvGroupTerm.CVGROUP_JOBTYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        job.setType(jobType);
        job = jobDao.create(job);

        // Set contact email
        loaderInstruction.setContactEmail(createdBy.getEmail());

        //Set Input file
        String markerFileName = "markers.txt";
        String rawFilesDir = Utils.getProcessDir(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        String inputFilePath = Paths.get(rawFilesDir, jobName, markerFileName).toString();
        Utils.writeByteArrayToFile(inputFilePath, markerFile);
        loaderInstruction.setInputFile(inputFilePath);

        //Set output dir
        String interMediateFilesDir = Utils.getProcessDir(cropType,
            GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);
        String outputFilesDir = Paths.get(interMediateFilesDir, jobName).toString();
        Utils.makeDir(outputFilesDir);
        loaderInstruction.setOutputDir(outputFilesDir);

        //Get API fields Entity Mapping
        Map<String, EntityFieldBean> dtoEntityMap = ModelMapper.getDtoEntityMap(
            MarkerTemplateDTO.class);
        Map<String, EntityFieldBean> templateFieldEntityMap = this.getTemplateFieldEntityMap(
            markerTemplateMap, dtoEntityMap);
        Map<String, String> fileColumnsApiFieldsMap = this.getFileColumnsApiFieldsMap(
            markerTemplateMap);


        //Read Header
        InputStream sampleInputStream = new ByteArrayInputStream(markerFile);
        try {

            br = new BufferedReader(
                new InputStreamReader(sampleInputStream, StandardCharsets.UTF_8));

            fileHeader = br.readLine();
        }
        catch (IOException io) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "No able to read file header");
        }

        // Set Marker Aspects
        Map<String, Object> aspects = new HashMap<>();
        String[] fileColumns = fileHeader.split("\t");
        Map<String, Cv> markerPropertiesCvsMap = new HashMap<>();
        Map<String, ColumnAspect> markerPropertiesAspects = new HashMap<>();

        for(int i = 0; i < fileColumns.length; i++) {
            String fileColumn = fileColumns[i];
            if(templateFieldEntityMap.containsKey(fileColumn)) {
                EntityFieldBean entityFieldBean = templateFieldEntityMap.get(fileColumn);
                String tableName = entityFieldBean.getTableName();

                ColumnCoordinates columnCoordinates = new ColumnCoordinates(1, i);
                ColumnAspect columnAspect = new ColumnAspect();
                columnAspect.setColumnCoordinates(columnCoordinates);

                if(tableName.equals(markerTableName) &&!aspects.containsKey(tableName)) {
                    aspects.put(tableName, markerTable);
                }
                else if(tableName.equals(markerLinkageTableName)
                    && !aspects.containsKey(tableName)) {
                    if(markerUploadRequest.getMapId() == null) {
                        throw new GobiiDomainException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Unable to load marker positions without genome map mapped");
                    }
                    aspects.put(tableName, markerLinkageGroupTable);
                }
                else if(tableName.equals(linkageGroupTableName) && !aspects.containsKey(tableName)) {
                    if(markerUploadRequest.getMapId() == null) {
                        throw new GobiiDomainException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Unable to load linkage groups without genome map mapped");
                    }
                    aspects.put(tableName, linkageGroupTable);
                }

                // Check for properties fields
                if(fileColumnsApiFieldsMap.get(fileColumn).startsWith("markerProperties.")) {
                    if(markerPropertiesAspects.size() == 0) {
                        List<Cv> markerPropertiesCvList = cvDao.getCvListByCvGroup(
                            CvGroupTerm.CVGROUP_MARKER_PROP.getCvGroupName(),
                            null);
                        markerPropertiesCvsMap = Utils.mapCyNames(markerPropertiesCvList);

                    }
                    String propertyName = fileColumnsApiFieldsMap
                        .get(fileColumn)
                        .replace("markerProperties.", "");

                    if(markerPropertiesCvsMap.containsKey(propertyName)) {
                        String propertyId = markerPropertiesCvsMap
                            .get(propertyName)
                            .getCvId().toString();
                        markerPropertiesAspects.put(propertyId, columnAspect);
                    }


                }
                else {
                    try {
                        Utils.setField(
                            aspects.get(tableName),
                            fileColumnsApiFieldsMap.get(fileColumn),
                            columnAspect);
                    }
                    catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new GobiiDomainException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.NONE,
                            "Unable to submit job file");
                    }
                }
            }
        }

        //Set JsonAspect
        if(markerPropertiesAspects.size() > 0) {
            JsonAspect jsonAspect = new JsonAspect();
            jsonAspect.setJsonMap(markerPropertiesAspects);
            ((MarkerTable)aspects.get(markerTableName)).setMarkerProperties(jsonAspect);
        }


        if(aspects.containsKey(markerLinkageTableName)) {
            ((MarkerLinkageGroupTable)aspects.get(markerLinkageTableName))
                .setMarkerName(((MarkerTable)aspects.get(markerTableName)).getMarkerName());
            ((MarkerLinkageGroupTable)aspects.get(markerLinkageTableName))
                .setLinkageGroupName(
                    ((LinkageGroupTable)aspects.get(linkageGroupTableName)).getLinkageGroupName());
        }

        loaderInstruction.setAspects(aspects);

        // Write instruction file
        try {
            String loaderInstructionText = mapper.writeValueAsString(loaderInstruction);
            String instructionFileName = jobName + ".json";

            String instructionFilesDir = Utils.getProcessDir(cropType,
                GobiiFileProcessDir.LOADER_INSTRUCTIONS);

            String instructionFilePath = Paths.get(
                instructionFilesDir,
                instructionFileName).toString();

            Utils.writeByteArrayToFile(
                instructionFilePath,
                loaderInstructionText.getBytes());
        }
        catch (JsonProcessingException jE) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.NONE,
                "Unable to submit job file");
        }

        JobDTO jobDTO = new JobDTO();
        ModelMapper.mapEntityToDto(job, jobDTO);
        return jobDTO;
    }




    private Map<String, EntityFieldBean> getTemplateFieldEntityMap(
        Map<String, Object> markerTemplateMap,
        Map<String, EntityFieldBean> dtoEntityMap
    ) {
        Map<String, EntityFieldBean> templateFieldsEntityMap = new HashMap<>();
        List<String> fileField;
        for(String apiField : markerTemplateMap.keySet()) {
            if(apiField.equals("markerProperties")) {
                Map<String, List<String>> markerProperties =
                    (HashMap<String, List<String>>) markerTemplateMap.get(apiField);
                for(String property : markerProperties.keySet()) {
                    fileField = markerProperties.get(property);
                    if(fileField.size() > 0) {
                        templateFieldsEntityMap.put(
                            fileField.get(0),
                            dtoEntityMap.get(apiField));
                    }
                }
            }
            else {
                fileField = (List<String>) markerTemplateMap.get(apiField);
                if(fileField.size() > 0) {
                    templateFieldsEntityMap.put(
                        fileField.get(0),
                        dtoEntityMap.get(apiField));
                }
            }

        }

        return templateFieldsEntityMap;
    }

    private Map<String, String> getFileColumnsApiFieldsMap(
        Map<String, Object> markerTemplateMap
    ) {
        Map<String, String> fileColumnsApiFieldsMap = new HashMap<>();
        List<String> fileField;
        for(String apiField : markerTemplateMap.keySet()) {
            if(apiField.equals("markerProperties")) {
                Map<String, List<String>> markerProperties =
                    (HashMap<String, List<String>>) markerTemplateMap.get(apiField);
                for(String property : markerProperties.keySet()) {
                    fileField = markerProperties.get(property);
                    if(fileField.size() > 0) {
                        fileColumnsApiFieldsMap.put(
                            fileField.get(0),
                            apiField+"."+property);
                    }
                }
            }
            else {

                fileField = (List<String>) markerTemplateMap.get(apiField);
                if (fileField.size() > 0) {
                    fileColumnsApiFieldsMap.put(
                        fileField.get(0),
                        apiField);
                }
            }
        }

        return fileColumnsApiFieldsMap;
    }

}
