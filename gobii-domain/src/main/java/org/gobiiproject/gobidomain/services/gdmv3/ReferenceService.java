/**
 * ReferenceServiceImpl.java
 * 
 * @author Rodolfo N. Duldulao, Jr.
 */
package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface ReferenceService {

    PagedResult<ReferenceDTO> getReferences(Integer page, Integer pageSize);
    
}