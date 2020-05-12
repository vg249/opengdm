/**
 * ReferenceServiceImpl.java
 * 
 * @author Rodolfo N. Duldulao, Jr.
 */
package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.List;

import org.gobiiproject.gobiimodel.dto.gdmv3.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Reference;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.ReferenceDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ReferenceServiceImpl implements ReferenceService {

    @Autowired
    private ReferenceDao referenceDao;

    @Override
    public PagedResult<ReferenceDTO> getReferences(Integer page, Integer pageSize) {
        Integer offset = page * pageSize;
        List<Reference> references = referenceDao.getReferences(offset, pageSize);

        List<ReferenceDTO> referenceDTOs = new java.util.ArrayList<>();
        references.forEach(reference -> {
            ReferenceDTO referenceDTO = new ReferenceDTO();
            ModelMapper.mapEntityToDto(reference, referenceDTO);
            referenceDTOs.add(referenceDTO);
        });

        PagedResult<ReferenceDTO> pagedResult = new PagedResult<>();
        pagedResult.setResult(referenceDTOs);
        pagedResult.setCurrentPageNum(page);
        pagedResult.setCurrentPageSize(referenceDTOs.size());
        return pagedResult;

    }
    
}