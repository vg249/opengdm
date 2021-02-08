package org.gobiiproject.gobiidomain.services.gdmv3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.*;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.modelmapper.AspectMapper;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.IntegerUtils;
import org.gobiiproject.gobiimodel.validators.FieldValidator;
import org.gobiiproject.gobiisampletrackingdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
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
     * @param inputFileStream      marker file input stream
     * @param markerUploadRequest   Request object with meta data and template
     * @param cropType              Crop type to which the markers need to uploaded
     * @return  {@link JobDTO}
     * @throws GobiiException   Gobii Exception for bad request or if any run time system error
     */
    @Override
    public JobDTO loadMarkerData(InputStream inputFileStream,
                                 MarkerUploadRequestDTO markerUploadRequest,
                                 String cropType) throws GobiiException {

        LoaderInstruction loaderInstruction = new LoaderInstruction();
        loaderInstruction.setLoadType(loadType);
        loaderInstruction.setAspects(new HashMap<>());

        Map<String, Object> markerTemplateMap;
        MarkerTemplateDTO markerTemplate;

        loaderInstruction.setCropType(cropType);

        // Verify platform Id
        if(!IntegerUtils.isNullOrZero(markerUploadRequest.getPlatformId())) {
            Platform platform = platformDao.getPlatform(markerUploadRequest.getPlatformId());
            if (platform == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid Platform");
            }
        }

        // Verify Mapset
        if(!IntegerUtils.isNullOrZero(markerUploadRequest.getMapId())) {
            Mapset mapset = mapsetDao.getMapset(markerUploadRequest.getMapId());
            if(mapset == null) {
                throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid mapset id");
            }
        }

        // Get user submitting the load
        String userName = ContactService.getCurrentUserName();
        Contact createdBy = contactDao.getContactByUsername(userName);

        // Create loader job
        JobDTO jobDTO = new JobDTO();
        jobDTO.setPayload(GobiiLoaderPayloadTypes.MARKERS.getTerm());
        JobDTO job = jobService.createLoaderJob(jobDTO);

        String jobName = job.getJobName();

        // Set contact email in loader instruction
        loaderInstruction.setContactEmail(createdBy.getEmail());

        //Set output dir
        String outputFilesDir = Utils.getOutputDir(jobName, cropType);
        loaderInstruction.setOutputDir(outputFilesDir);


        // Write instruction file
        Utils.writeInstructionFile(loaderInstruction, jobName, cropType);

        return jobDTO;
    }

}
