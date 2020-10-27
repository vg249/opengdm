package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;

public interface JobService {
    JobDTO getJobById(Integer jobId) throws GobiiDomainException;
}
