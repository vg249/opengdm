/**
 * ExperimentServiceImpl.java
 * 
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-28
 */

package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ExperimentServiceImpl implements ExperimentService {
    
    @Autowired
    private ExperimentDao experimentDao;

    @Override
    public PagedResult<ExperimentDTO> getExperiments(Integer page, Integer pageSize, Integer projectId) {
        // TODO Auto-generated method stub
        List<Experiment> experiments = experimentDao.getExperiments(pageSize, page * pageSize, projectId);

        List<ExperimentDTO> dtos = new ArrayList<>();

        experiments.forEach(experiment -> {
            ExperimentDTO dto = new ExperimentDTO();
            ModelMapper.mapEntityToDto(experiment, dto);
            dtos.add(dto);
        });

        PagedResult<ExperimentDTO> pagedResult = new PagedResult<>();
        pagedResult.setResult(dtos);
        pagedResult.setCurrentPageNum(page);
        pagedResult.setCurrentPageSize(pageSize);

        return pagedResult;
    }
    
}