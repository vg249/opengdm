package org.gobiiproject.gobiiweb.controllers.gdm.v3;

import io.swagger.annotations.Api;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiidomain.services.gdmv3.JobService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        @PathVariable Integer jobId
    ) throws GobiiException {
        JobDTO jobDTO = jobService.getJobById(jobId);
        BrApiMasterPayload<JobDTO> result = ControllerUtils.getMasterPayload(jobDTO);
        return ResponseEntity.ok(result);
    }

}
