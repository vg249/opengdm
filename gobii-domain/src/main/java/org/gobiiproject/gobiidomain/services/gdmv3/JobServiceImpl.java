package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.entity.Job;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JobServiceImpl implements JobService {

    @Autowired
    private JobDao jobDao;

    @Override
    public JobDTO getJobById(Integer jobId) throws GobiiException {
        JobDTO jobDTO = new JobDTO();
        Job job = jobDao.getById(jobId);
        ModelMapper.mapEntityToDto(job, jobDTO);
        return jobDTO;
    }

}
