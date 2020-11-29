package org.gobiiproject.gobiidomain.services.gdmv3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.DnaRunTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.*;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.modelmapper.AspectMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiLoaderPayloadTypes;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.IntegerUtils;
import org.gobiiproject.gobiisampletrackingdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("unchecked")
@Transactional
public class DnaRunServiceImpl implements DnaRunService {

    @Autowired
    private LoaderTemplateDao loaderTemplateDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private JobDao jobDao;

    final ObjectMapper mapper = new ObjectMapper();

    final String loadType = "SAMPLES";

    /**
     * Uploads dnaruns in the file to the database.
     * Also, loads dna samples and germplasms when provided in the same file.
     *
     * @param dnaRunFile            Input file byte array
     * @param dnaRunUploadRequest   Request object with meta data and template
     * @param cropType              Crop type to which the dnaruns need to uploaded
     * @return  {@link JobDTO}
     * @throws GobiiException   Gobii Exception for bad request or if any run time system error
     */
    @Override
    public JobDTO loadDnaRuns(byte[] dnaRunFile,
                              DnaRunUploadRequestDTO dnaRunUploadRequest,
                              String cropType) throws GobiiException {
        BufferedReader br;
        LoaderInstruction loaderInstruction = new LoaderInstruction();
        loaderInstruction.setLoadType(loadType);
        loaderInstruction.setAspects(new HashMap<>());

        String fileHeader;
        Map<String, Object> dnaRunTemplateMap;
        DnaRunTemplateDTO dnaRunTemplate;

        // Set Marker Aspects
        Map<String, Object> aspects = new HashMap<>();

        loaderInstruction.setCropType(cropType);



        return new JobDTO();
    }

    private Job getNewJob() throws GobiiException {
        String jobName = UUID.randomUUID().toString().replace("-", "");
        Job job = new Job();
        job.setJobName(jobName);
        job.setMessage("Submitted dnarun upload job");
        // Get payload type
        Cv payloadType = cvDao.getCvs(GobiiLoaderPayloadTypes.DNARUNS.getTerm(),
            CvGroupTerm.CVGROUP_PAYLOADTYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        job.setPayloadType(payloadType);
        job.setSubmittedDate(new Date());
        // Get load type
        Cv jobType = cvDao.getCvs(
            JobType.CV_JOBTYPE_LOAD.getCvName(),
            CvGroupTerm.CVGROUP_JOBTYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        job.setType(jobType);
        return job;
    }


}
