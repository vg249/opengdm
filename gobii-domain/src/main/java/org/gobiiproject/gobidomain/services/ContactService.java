package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;

/**
 * Created by Angel on 5/4/2016.
 */
public interface ContactService {

    ResultEnvelope<ContactDTO> processContact(RequestEnvelope<ContactDTO> requestEnvelope);

}
