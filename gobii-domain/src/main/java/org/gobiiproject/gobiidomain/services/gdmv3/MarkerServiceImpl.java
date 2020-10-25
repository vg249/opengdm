package org.gobiiproject.gobiidomain.services.gdmv3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.*;
import org.gobiiproject.gobiimodel.dto.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.modelmapper.EntityFieldBean;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.LoaderTemplateDao;
import org.gobiiproject.gobiisampletrackingdao.PlatformDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MarkerServiceImpl implements MarkerService {

    @Autowired
    private LoaderTemplateDao loaderTemplateDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private CvDao cvDao;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public JobDTO uploadMarkerFile(
        byte[] markerFile,
        MarkerUploadRequestDTO markerUploadRequest,
        String cropType) throws GobiiException {

        BufferedReader br;
        LoaderInstruction loaderInstruction = new LoaderInstruction();
        loaderInstruction.setAspects(new HashMap<>());

        String fileHeader;
        Map<String, List<String>> markerTemplateMap;

        loaderInstruction.setCropType(cropType);
        MarkerTable markerTable = new MarkerTable();
        LinkageGroupTable linkageGroupTable = new LinkageGroupTable();
        MarkerLinkageGroupTable markerLinkageGroupTable = new MarkerLinkageGroupTable();


        // Set Platform Id in Table Aspects
        Platform platform = platformDao.getPlatform(markerUploadRequest.getPlatformId());
        if(platform == null) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Invalid Platform");
        }
        markerTable.setPlatformId(platform.getPlatformId().toString());
        markerLinkageGroupTable.setPlatformId(platform.getPlatformId().toString());

        //Get new status
        Cv status = cvDao.getCvs(
            "new",
            CvGroupTerm.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);


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
        try {
            Contact createdBy = contactDao.getContactByUsername(userName);
        }
        catch (Exception e) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.NONE,
                "Unable to find user in system");
        }
        String jobName = UUID.randomUUID().toString().replace("-", "");

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


        //Read Header
        InputStream sampleInputStream = new ByteArrayInputStream(markerFile);
        try {
            br = new BufferedReader(new InputStreamReader(sampleInputStream, "UTF-8"));
            fileHeader = br.readLine();
        }
        catch (IOException io) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "No able to read file header");
        }

        Map<String, Object> aspects = new HashMap<>();

        String[] fileColumns = fileHeader.split("\t");
        for(int i = 0; i < fileColumns.length; i++) {
            String fileColumn = fileColumns[i];
            if(templateFieldEntityMap.containsKey(fileColumn)) {
                EntityFieldBean entityFieldBean = templateFieldEntityMap.get(fileColumn);
                String tableName = entityFieldBean.getTableName();

                ColumnCoordinates columnCoordinates = new ColumnCoordinates(0, i);
                ColumnAspect columnAspect = new ColumnAspect();
                columnAspect.setColumnCoordinates(columnCoordinates);

                if(!aspects.containsKey(tableName)) {
                    aspects.put(tableName, new HashMap<>());
                    if(tableName.equals("marker")) {
                        ((HashMap<String, String>)aspects.get(tableName))
                            .put("platformId", platform.getPlatformId().toString());
                        ((HashMap<String, String>)aspects.get(tableName))
                            .put("status", status.getCvId().toString());
                    }
                    else if(tableName.equals("marker_linkage_group")) {
                        ((HashMap<String, String>)aspects.get(tableName))
                            .put("platformId", platform.getPlatformId().toString());
                    }
                }

                ((HashMap<String, ColumnAspect>)aspects.get(tableName))
                    .put(entityFieldBean.getColumnName(), columnAspect);

            }
        }

        loaderInstruction.setAspects(aspects);

        System.out.println(dtoEntityMap);
        try {
            String loaderInstructionText = mapper.writeValueAsString(loaderInstruction);
            Utils.writeByteArrayToFile(
                "/home/vg249/inst.json",
                loaderInstructionText.getBytes());
        }
        catch (JsonProcessingException jE) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.NONE,
                "Unable to submit job file");
        }


        return new JobDTO();
    }

    private Map<String, EntityFieldBean> getTemplateFieldEntityMap(
        Map<String, List<String>> markerTemplateMap,
        Map<String, EntityFieldBean> dtoEntityMap
    ) {
        Map<String, EntityFieldBean> templateFieldsEntityMap = new HashMap<>();
        for(String apiField : markerTemplateMap.keySet()) {
            if(markerTemplateMap.get(apiField).size() > 0) {
                templateFieldsEntityMap.put(
                    markerTemplateMap.get(apiField).get(0),
                    dtoEntityMap.get(apiField));
            }
        }

        return templateFieldsEntityMap;
    }

}
