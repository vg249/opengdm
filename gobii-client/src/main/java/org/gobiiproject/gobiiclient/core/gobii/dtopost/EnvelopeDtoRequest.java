package org.gobiiproject.gobiiclient.core.gobii.dtopost;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;

import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class EnvelopeDtoRequest<T> {

    Logger LOGGER = LoggerFactory.getLogger(EnvelopeDtoRequest.class);


    public PayloadEnvelope<T> processEnvelope(PayloadEnvelope<T> payloadEnvelope,
                                              Class<T> DtoType,
                                              GobiiControllerType gobiiControllerType,
                                              GobiiServiceRequestId requestId) throws Exception {

        String token = GobiiClientContext.getInstance(null, false).getUserToken();
        String host = GobiiClientContext.getInstance(null, false).getCurrentCropDomain();
        Integer port = GobiiClientContext.getInstance(null, false).getCurrentCropPort();
        String cropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();

        PayloadEnvelope<T> returnVal = null;

        EnvelopeRestRequest<T> envelopeRestRequest = new EnvelopeRestRequest<>(cropContextRoot,
                DtoType);


        if (null == token || token.isEmpty()) {
            throw (new Exception("there is no user token; user must log in"));
        }

        String url = requestId.getRequestUrl(cropContextRoot, gobiiControllerType.getControllerPath());


        returnVal = envelopeRestRequest.getTypedHtppResponseForDtoEnvelope(url,
                payloadEnvelope,
                token);

        return returnVal;
    }

}
