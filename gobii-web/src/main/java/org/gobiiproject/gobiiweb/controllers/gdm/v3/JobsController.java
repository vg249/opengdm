package org.gobiiproject.gobiiweb.controllers.gdm.v3;

import io.swagger.annotations.Api;

import java.io.File;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiidomain.services.gdmv3.JobService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterListPayload;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.utils.GobiiFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Scope(value = "request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_GOBII_V3)
@Api()
@CrossOrigin
public class JobsController {

    private final JobService jobService;

    /**
     * Constructor
     * @param jobService    {@link JobService}
     */
    @Autowired
    public JobsController(final JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * Get Job by Id. to check job status
     * @param jobId
     * @return
     * @throws Exception
     */
    @GetMapping("/jobs/{jobId}")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<JobDTO>> getJObById(
        @PathVariable Integer jobId,
        @PathVariable String cropType
    ) throws GobiiException {
        JobDTO jobDTO = jobService.getJobById(jobId, cropType);
        BrApiMasterPayload<JobDTO> result = ControllerUtils.getMasterPayload(jobDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/jobs")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<JobDTO>> getJobs(
        @RequestParam(required=false, defaultValue="0") Integer page,
        @RequestParam(required=false, defaultValue="1000") Integer pageSize,
        @RequestParam(required=false, defaultValue="") String username,
        @RequestParam(required=false, defaultValue="false") boolean getLoadJobs,
        @RequestParam(required=false, defaultValue="false") boolean getExtractJobs,
        @PathVariable String cropType
    ) throws GobiiException {
        Integer pageSizeToUse = ControllerUtils.getPageSize(pageSize);
        PagedResult<JobDTO> pagedResult = jobService.getJobs(
            page, pageSizeToUse, 
            username, getLoadJobs, 
            getExtractJobs, cropType
        );

        BrApiMasterListPayload<JobDTO> payload = ControllerUtils.getMasterListPayload(pagedResult);
        return ResponseEntity.ok(payload);
    }



    @GetMapping(
        value="/jobs/{jobId}/files/{jobFilesDirectoryName:[\\w]+}.zip", 
        produces="application/zip")
    public ResponseEntity<StreamingResponseBody> zipFiles(
        @PathVariable Integer jobId,
        @PathVariable String jobFilesDirectoryName,
        @PathVariable String cropType) throws Exception {

        List<File> jobOutputDirectories = jobService.getJobOutputDirectories(cropType, jobId);
        
        if (jobOutputDirectories == null ||
            jobOutputDirectories.size() == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
            .header(
                "Content-Disposition",
                String.format("attachment; filename=\"%s\"", 
                    jobFilesDirectoryName + ".zip")
            )
            .body(out -> {
                ZipOutputStream zipOut = new ZipOutputStream(out);
                if(jobOutputDirectories.size() > 1) {
                    GobiiFileUtils.streamZipFiles(jobOutputDirectories, 
                        jobFilesDirectoryName, 
                        zipOut);
                }
                else {
                    GobiiFileUtils.streamZipFile(jobOutputDirectories.get(0), 
                        jobFilesDirectoryName, 
                        zipOut);
                }
                zipOut.close();
            });

    }

}
