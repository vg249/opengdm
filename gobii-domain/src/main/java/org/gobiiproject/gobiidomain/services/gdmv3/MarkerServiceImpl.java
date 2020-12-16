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
import org.gobiiproject.gobiimodel.modelmapper.AspectMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.IntegerUtils;
import org.gobiiproject.gobiisampletrackingdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
    private JobService jobService;

    final ObjectMapper mapper = new ObjectMapper();

    final String loadType = "MARKER";


    /**
     * Uploads markers in the file to the database.
     * Also, loads marker positions and linkage groups when provided in the same file.
     *
     * @param markerFile            Input marker file byte array
     * @param markerUploadRequest   Request object with meta data and template
     * @param cropType              Crop type to which the markers need to uploaded
     * @return  {@link JobDTO}
     * @throws GobiiException   Gobii Exception for bad request or if any run time system error
     */
    @Override
    public JobDTO loadMarkerData(byte[] markerFile,
                                 MarkerUploadRequestDTO markerUploadRequest,
                                 String cropType) throws GobiiException {

        LoaderInstruction loaderInstruction = new LoaderInstruction();
        loaderInstruction.setLoadType(loadType);
        loaderInstruction.setAspects(new HashMap<>());

        Map<String, Object> markerTemplateMap;
        MarkerTemplateDTO markerTemplate;

        loaderInstruction.setCropType(cropType);

        // Set Marker Aspects
        Map<String, Object> aspects = new HashMap<>();

        // Tables loaded in marker upload
        MarkerTable markerTable = new MarkerTable();
        LinkageGroupTable linkageGroupTable = new LinkageGroupTable();
        MarkerLinkageGroupTable markerLinkageGroupTable = new MarkerLinkageGroupTable();
        MarkerGroupTable markerGroupTable = new MarkerGroupTable();

        // Get tables names in database
        String markerTableName = Utils.getTableName(MarkerTable.class);
        String linkageGroupTableName = Utils.getTableName(LinkageGroupTable.class);
        String markerLinkageTableName = Utils.getTableName(MarkerLinkageGroupTable.class);
        String markerGroupTableName = Utils.getTableName(MarkerGroupTable.class);


        // Set Platform Id in Table Aspects
        if(!IntegerUtils.isNullOrZero(markerUploadRequest.getPlatformId())) {
            Platform platform = platformDao.getPlatform(markerUploadRequest.getPlatformId());
            if (platform == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid Platform");
            }
            markerTable.setPlatformId(platform.getPlatformId().toString());
            markerLinkageGroupTable.setPlatformId(platform.getPlatformId().toString());
            markerGroupTable.setPlatformId(platform.getPlatformId().toString());
        }

        // Set mapSet for linkage group and marker linkage group
        if(!IntegerUtils.isNullOrZero(markerUploadRequest.getMapId())) {
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

        // Read marker template
        LoaderTemplate loaderTemplate = loaderTemplateDao.getById(
            markerUploadRequest.getMarkerTemplateId());
        try {
            markerTemplateMap = mapper.treeToValue(
                loaderTemplate.getTemplate(), HashMap.class);
            markerTemplate = mapper.treeToValue(
                loaderTemplate.getTemplate(),
                MarkerTemplateDTO.class);
        }
        catch (JsonProcessingException jE) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Invalid marker template object.");
        }

        // Get user submitting the load
        String userName = ContactService.getCurrentUser();
        Contact createdBy = contactDao.getContactByUsername(userName);

        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroupTerm.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        // Set new status for marker table
        markerTable.setStatus(newStatus.getCvId().toString());

        JobDTO jobDTO = new JobDTO();
        jobDTO.setPayload(GobiiLoaderPayloadTypes.MARKERS.getTerm());
        JobDTO job = jobService.createLoaderJob(jobDTO);

        String jobName = job.getJobName();

        // Set contact email in loader instruction
        loaderInstruction.setContactEmail(createdBy.getEmail());

        //Set Input file
        String inputFilePath =
            Utils.writeInputFile(markerFile, "markers.txt", jobName, cropType);
        loaderInstruction.setInputFile(inputFilePath);

        //Set output dir
        String outputFilesDir = Utils.getOutputDir(jobName, cropType);
        loaderInstruction.setOutputDir(outputFilesDir);

        //Get API fields Entity Mapping
        HashSet<String> propertyFields = new HashSet<String>(){{add("markerProperties");}};
        Map<String, List<String>> fileColumnsApiFieldsMap =
            Utils.getFileColumnsApiFieldsMap(markerTemplateMap, propertyFields);

        Map<String, Object> aspectValues = new HashMap<>();

        // Get Header
        String[] fileColumns = Utils.getHeaders(markerFile);

        Map<String, Cv> markerPropertiesCvsMap = new HashMap<>();
        Map<String, ColumnAspect> markerPropertiesAspects = new HashMap<>();

        for(int i = 0; i < fileColumns.length; i++) {
            String fileColumn = fileColumns[i];
            ColumnAspect columnAspect = new ColumnAspect(1, i);

            // ignore file columns not mapped by template.
            if(!fileColumnsApiFieldsMap.containsKey(fileColumn)) {
                continue;
            }

            for(String apiFieldName : fileColumnsApiFieldsMap.get(fileColumn)) {

                // Check for properties fields
                if (apiFieldName.startsWith("markerProperties.")) {
                    if (markerTable.getMarkerProperties() == null) {
                        // Initialize and set json aspect for properties field.
                        JsonAspect jsonAspect = new JsonAspect();
                        jsonAspect.setJsonMap(markerPropertiesAspects);
                        aspectValues.put("markerProperties", jsonAspect);

                        // Get list of properties as (cvTerm -> cv) map, so it is easy to map
                        // cv name to id
                        List<Cv> markerPropertiesCvList = cvDao.getCvListByCvGroup(
                            CvGroupTerm.CVGROUP_MARKER_PROP.getCvGroupName(),
                            null);
                        markerPropertiesCvsMap = Utils.mapCvNames(markerPropertiesCvList);
                    }
                    String propertyName = apiFieldName.replace("markerProperties.", "");

                    // Map cv id to properties map
                    if (markerPropertiesCvsMap.containsKey(propertyName)) {
                        String propertyId = markerPropertiesCvsMap
                            .get(propertyName)
                            .getCvId().toString();
                        markerPropertiesAspects.put(propertyId, columnAspect);
                    }
                } else {
                    aspectValues.put(apiFieldName, columnAspect);
                }
            }
        }

        boolean markerTableMapped =
            AspectMapper.mapTemplateToAspects(markerTemplate, markerTable, aspectValues);

        boolean linkageGroupTableMapped =
            AspectMapper.mapTemplateToAspects(markerTemplate, linkageGroupTable, aspectValues);

        AspectMapper.mapTemplateToAspects(markerTemplate, markerGroupTable, aspectValues);


        if(markerTableMapped) {
            validateMarkerTable(markerTable);
            aspects.put(markerTableName, markerTable);
        }

        if(linkageGroupTableMapped) {
            validateLinkageGroupTable(linkageGroupTable);
            aspects.put(linkageGroupTableName, linkageGroupTable);
        }

        if(markerTableMapped && linkageGroupTableMapped) {
            AspectMapper.mapTemplateToAspects(
                markerTemplate,
                markerLinkageGroupTable,
                aspectValues);
            validateMarkerLinkageGroupTable(markerLinkageGroupTable);
            aspects.put(markerLinkageTableName, markerLinkageGroupTable);
        }

        if(markerTableMapped && markerGroupTable.getMarkerGroupName() != null) {
            FieldValidator.validate(markerGroupTable);
            aspects.put(markerGroupTableName, markerGroupTable);
        }

        loaderInstruction.setAspects(aspects);

        // Write instruction file
        Utils.writeInstructionFile(loaderInstruction, jobName, cropType);

        return jobDTO;
    }

    private void validateMarkerTable(MarkerTable markerTable) throws GobiiDomainException {
        if(markerTable.getPlatformId() == null && markerTable.getPlatformName() == null) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Neither PlatformId nor Platform name mapped");
        }
        FieldValidator.validate(markerTable);
    }

    private void validateLinkageGroupTable(
        LinkageGroupTable linkageGroupTable) throws GobiiDomainException {
        if(linkageGroupTable.getMapId() == null && linkageGroupTable.getMapName() == null) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Neither MapId nor Genome Map name ");
        }
        FieldValidator.validate(linkageGroupTable);
    }

    private void validateMarkerLinkageGroupTable(MarkerLinkageGroupTable markerLinkageGroupTable) throws GobiiDomainException {
        if(markerLinkageGroupTable.getPlatformId() == null &&
            markerLinkageGroupTable.getPlatformName() == null) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Neither PlatformId nor Platform name mapped");
        }
        if(markerLinkageGroupTable.getMapId() == null &&
            markerLinkageGroupTable.getMapName() == null) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Neither MapId nor Genome Map name mapped");
        }
        FieldValidator.validate(markerLinkageGroupTable);
    }


}
