// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.restmethods.post.EnvelopeRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestContact {

    public ResultEnvelope<ContactDTO> process(RequestEnvelope<ContactDTO> requestEnvelope) throws Exception {

        return new EnvelopeRequestProcessor<ContactDTO>().processEnvelope(requestEnvelope,
                ContactDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_CONTACT);
    }
}
