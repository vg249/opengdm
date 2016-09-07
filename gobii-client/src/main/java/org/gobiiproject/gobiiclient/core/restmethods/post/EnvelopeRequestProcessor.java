package org.gobiiproject.gobiiclient.core.restmethods.post;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class EnvelopeRequestProcessor<T> {

    Logger LOGGER = LoggerFactory.getLogger(EnvelopeRequestProcessor.class);


    public ResultEnvelope<T> processEnvelope(RequestEnvelope<T> requestEnvelope, Class<T> DtoType,
                                             ControllerType controllerType,
                                             ServiceRequestId requestId) throws Exception {

        String token = ClientContext.getInstance(null, false).getUserToken();
        String host = ClientContext.getInstance(null, false).getCurrentCropDomain();
        Integer port = ClientContext.getInstance(null, false).getCurrentCropPort();

        ResultEnvelope<T> returnVal = null;

        EnvelopeRestRequest<T> envelopeRestRequest= new EnvelopeRestRequest<>(host, port, DtoType);


        if (null == token || token.isEmpty()) {
            throw (new Exception("there is no user token; user must log in"));
        }

        String url = Urls.getRequestUrl(controllerType,
                requestId);



        returnVal = envelopeRestRequest.getTypedHtppResponseForDtoEnvelope(url,
                requestEnvelope,
                token);

        return returnVal;
    }

}
