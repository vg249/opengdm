package org.gobiiproject.gobiiclient.core.restmethods;

import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.HttpCore;
import org.gobiiproject.gobiimodel.dto.response.PayloadEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class EnvelopeGetRequestProcessor<T> {

    Logger LOGGER = LoggerFactory.getLogger(EnvelopeGetRequestProcessor.class);


    public PayloadEnvelope<T> processGetRequest(RestUri restUri,
                                                Class<T> dtoType) throws Exception {

        PayloadEnvelope<T> returnVal = null;

        if (ClientContext.isInitialized()) {

            String token = ClientContext.getInstance(null, false).getUserToken();
            if (null == token || token.isEmpty()) {
                throw (new Exception("there is no user token; user must log in"));
            }

            String host = ClientContext.getInstance(null, false).getCurrentCropDomain();
            Integer port = ClientContext.getInstance(null, false).getCurrentCropPort();


            HttpCore httpCore = new HttpCore(host, port);

            JsonObject responseJson = httpCore.getFromGet(restUri, token);

            returnVal = new PayloadEnvelope<T>()
                    .fromJson(responseJson, dtoType);

        }

        return returnVal;
    }

}
