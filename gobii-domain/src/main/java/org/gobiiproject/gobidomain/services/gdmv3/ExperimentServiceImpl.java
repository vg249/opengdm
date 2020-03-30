/**
 * ExperimentServiceImpl.java
 * 
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-28
 */

package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public class ExperimentServiceImpl implements ExperimentService {
    @Autowired
    private ExperimentDao experimentDao;
    
    @Override
    public PagedResult<ExperimentDTO> getExperiments(Integer page, Integer pageSize, Integer projectId) {
        // TODO Auto-generated method stub
        return null;
    }
    
}