package org.gobiiproject.gobiidomain.services.gdmv3;

import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypesUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.types.GobiiLoaderPayloadTypes;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;

public class GenotypeServiceImpl implements GenotypeService {

    final String loadType = "MATRIX";

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private JobService jobService;

    public JobDTO loadGenotypes(InputStream inputFileStream,
                                String fileOriginalName,
                                GenotypesUploadRequestDTO genotypesUploadRequest,
                                String cropType) throws GobiiException {

        LoaderInstruction loaderInstruction = new LoaderInstruction();
        loaderInstruction.setLoadType(loadType);

        // Get user submitting the load
        String userName = ContactService.getCurrentUserName();
        Contact createdBy = contactDao.getContactByUsername(userName);

        // Set contact email in loader instruction
        loaderInstruction.setContactEmail(createdBy.getEmail());

        // Get a new Job object for samples loading
        JobDTO jobDTO = new JobDTO();
        jobDTO.setPayload(GobiiLoaderPayloadTypes.MATRIX.getTerm());
        JobDTO job = jobService.createLoaderJob(jobDTO);

        String jobName = job.getJobName();

        //Set Input file
        File genotypeFile =
            Utils.writeInputFile(inputFileStream, fileOriginalName, jobName, cropType);
        loaderInstruction.setInputFile(genotypeFile.getAbsolutePath());

        //Set output dir
        String outputFilesDir = Utils.getOutputDir(jobName, cropType);
        loaderInstruction.setOutputDir(outputFilesDir);


        return jobDTO;

    }

}
