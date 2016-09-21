// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.restmethods.dtopost.EnvelopeDtoRequest;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.PayloadEnvelope;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestContact {

    public PayloadEnvelope<ContactDTO> process(PayloadEnvelope<ContactDTO> payloadEnvelope) throws Exception {

        return new EnvelopeDtoRequest<ContactDTO>().processEnvelope(payloadEnvelope,
                ContactDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_CONTACT);
    }
}
