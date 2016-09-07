// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.restmethods.post.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestReference {

    public ReferenceDTO process(ReferenceDTO referenceDTO) throws Exception {

        return new DtoRequestProcessor<ReferenceDTO>().process(referenceDTO,
                ReferenceDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_REFERENCE);

    } // getPing()

}
